package com.crossmint.megaverse.service;

import com.crossmint.megaverse.models.AstralObject;
import com.crossmint.megaverse.models.Cometh;
import com.crossmint.megaverse.models.Polyanet;
import com.crossmint.megaverse.models.Soloon;
import com.crossmint.megaverse.service.request_body.ColorRequestBody;
import com.crossmint.megaverse.service.request_body.DirRequestBody;
import com.crossmint.megaverse.service.request_body.RequestBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.LaxRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
public class ApiService {

    private final static ObjectMapper mapper = new ObjectMapper();
    private final static String rootUrl;
    private final static String clientID;
    private final static int maxRetry;
    private static final long wait;
    RestTemplate restTemplate;

    static{
        Dotenv dotenv = Dotenv.load();
        rootUrl = dotenv.get("ROOT_URL");
        clientID = dotenv.get("CANDIDATE_ID");
        maxRetry = Integer.parseInt(dotenv.get("MAX_RETRY"));
        wait = Long.parseLong(dotenv.get("WAIT_TIME"));
    }


    public ApiService(){
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate = new RestTemplate(requestFactory);

    }

    public int postAstralObject(AstralObject astralObj){
        return requestAstralObject(astralObj,HttpMethod.POST);
    }

    public int deleteAstralObject(AstralObject astralObj){
        return requestAstralObject(astralObj,HttpMethod.DELETE);
    }

    //Method for generating and handling POST and DELETE requests
    private int requestAstralObject(AstralObject astralObj, HttpMethod method){
        ResponseEntity<Void> response;
        String url = rootUrl+astralObj.getAstralName().getUrlSuffix();
        HttpEntity<?> entity = resolveHttpEntity(astralObj);

        try{
            response = initAstralObjectRequest(url,method,entity);
            return response.getStatusCode().value();
        }catch (HttpClientErrorException e){
            System.out.println(e.getMessage());
            return handle400StatusErrorsAndAbove(e,url,method,entity);
        }
    }



    //Method for building the correct request body based on the AstralObject provided
    private static HttpEntity<? extends RequestBody> resolveHttpEntity(AstralObject astralObj){

        HttpHeaders headers = new HttpHeaders();
        headers.setConnection("keep-alive");
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(astralObj instanceof Polyanet){
            RequestBody body = new RequestBody(clientID,astralObj.getRow(),astralObj.getCol());
            return new HttpEntity<>(body, headers);
        }

        if(astralObj instanceof Cometh cometh){
            DirRequestBody body = new DirRequestBody(clientID,
                    cometh.getRow(),
                    cometh.getCol(),
                    cometh.getDirection().name().toLowerCase());
            return new HttpEntity<>(body, headers);
        }

        if(astralObj instanceof Soloon moon){
            ColorRequestBody body = new ColorRequestBody(clientID,
                    moon.getRow(),
                    moon.getCol(),
                    moon.getColor().name().toLowerCase());
            return new HttpEntity<>(body, headers);
        }

        return null;
    }

    //Method for generating the GET request for goal Metaverse.
    //Separate from the other request methods due the URL structure, headers, error handling, and return type.
    public String[][] getMetaverseGoal() throws JsonProcessingException {
        String goalURL = rootUrl + "map/" + clientID + "/goal";
        HttpMethod method = HttpMethod.GET;
        ResponseEntity<String> response = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setConnection("keep-alive");
        headers.setAccept(List.of(MediaType.ALL));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try{
            response = restTemplate.exchange(goalURL,method,entity, String.class);

            if(response.getStatusCode().is3xxRedirection()){
                HttpHeaders responseHeaders = response.getHeaders();
                String goalUrl = responseHeaders.getFirst(HttpHeaders.LOCATION);

                if (goalUrl != null){
                    response = restTemplate.exchange(goalUrl,method,entity, String.class);
                }

            }
            return mapper.readValue(response.getBody(), MegaverseMap.class).getGoal();
        }catch (HttpClientErrorException e){
            System.out.println(e.getMessage());
            assert response != null;
            if(handle400StatusErrorsAndAbove(e,goalURL,method,entity)!=200){
                return new String[0][0];
            }
            return mapper.readValue(response.getBody(), MegaverseMap.class).getGoal();
        }
    }

    //Method for initiating the the request and handling potential re-directs
    private ResponseEntity<Void> initAstralObjectRequest(String url,HttpMethod method,HttpEntity<?> entity){

        //Initial attempt at calling the API.
        ResponseEntity<Void> response = restTemplate
                .exchange(url,
                        method,
                        entity,
                        Void.class
                );

        //Handling redirect response by extracting the new URL and calling the API with it.
        if(response.getStatusCode().is3xxRedirection()){
            HttpHeaders responseHeaders = response.getHeaders();
            url = responseHeaders.getFirst(HttpHeaders.LOCATION);

            if (url != null){
                response = restTemplate
                        .exchange(url,
                                method,
                                entity,
                                Void.class
                        );
            }

        }

        return response;
    }

    //Method for handling API request errors.
    private int handle400StatusErrorsAndAbove(HttpClientErrorException err, String url,HttpMethod method,HttpEntity<?> entity) {
        ResponseEntity<?> response = null;

        //Handling bad request errors.
        //Specifically the 429 by retrying the request after a delay for a number of times.
        if(err.getStatusCode().is4xxClientError()){
            if (err.getStatusCode().value() == 429){
                //delay =  Long.parseLong(err.getResponseHeaders().get("Retry-After").getFirst());
                try{
                    System.out.println("Too many requests to the URL.");
                    for(int i = 0;i<maxRetry;i++){
                        System.out.printf("Trying again after %d milliseconds.\n", wait);
                        Thread.sleep(wait);
                        response = initAstralObjectRequest(url,method,entity);
                        if(response.getStatusCode().is2xxSuccessful()){
                            return response.getStatusCode().value();
                        }
                    }
                }catch (InterruptedException|HttpClientErrorException e){
                    System.out.println(e.getMessage());
                    return (e instanceof HttpClientErrorException er)?er.getStatusCode().value():400;
                }
            }else{
                System.out.println("Consider checking the request URL, body, or headers.");
            }
        }


        //Handling internal server errors.
        if(Objects.nonNull(response) && response.getStatusCode().is5xxServerError()){
            System.out.println("Consider double-checking the candidate ID.");
            return response.getStatusCode().value();
        }

        return err.getStatusCode().value();
    }




    @Getter
    @Setter
    static class MegaverseMap{
        String[][] goal;
    }


}
