package com.crossmint.polyantet.service;

import com.crossmint.polyantet.models.AstralObject;
import com.crossmint.polyantet.models.Polyanet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.LaxRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
public class ApiService {

    private final static ObjectMapper mapper = new ObjectMapper();
    private final static String rootUrl = "https://challenge.crossmint.io/api/",
    clientID = "ba00ce1b-f3ec-471c-b54a-348d94917a84";
    RestTemplate restTemplate;

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

    private int requestAstralObject(AstralObject astralObj,HttpMethod method){
        HttpHeaders headers = new HttpHeaders();
        headers.setConnection("keep-alive");
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestBody body = new RequestBody(astralObj.getRow(),astralObj.getCol());

        HttpEntity<RequestBody> entity = new HttpEntity<>(body,headers);

        ResponseEntity<Void> response=null;
        try{
            response = restTemplate
                    .exchange(rootUrl+astralObj.getAstralName().getUrlSuffix(),
                            method,
                            entity,
                            Void.class
                    );

            if(response.getStatusCode().is3xxRedirection()){
                HttpHeaders responseHeaders = response.getHeaders();
                String newUrl = responseHeaders.getFirst(HttpHeaders.LOCATION);

                if (newUrl != null){
                    response = restTemplate
                            .exchange(newUrl,
                                    method,
                                    entity,
                                    Void.class
                            );
                    return response.getStatusCode().value();
                }

            }

            return response.getStatusCode().value();
        }catch (RestClientException e){
            log.error(e.getMessage());
            if(Objects.nonNull(response)){
                return response.getStatusCode().value();
            }
        }

        return 204;
    }

    public String[][] getGoalMap() throws JsonProcessingException {
        String goalURL = rootUrl + "map/" + clientID + "/goal";
        GoalMap map = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setConnection("keep-alive");
        headers.setAccept(List.of(MediaType.ALL));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(goalURL,HttpMethod.GET,entity, String.class);

        if(response.getStatusCode().is3xxRedirection()){
            HttpHeaders responseHeaders = response.getHeaders();
            String newUrl = responseHeaders.getFirst(HttpHeaders.LOCATION);

            if (newUrl != null){
                String mapJson = restTemplate.exchange(newUrl,HttpMethod.GET,null, String.class).getBody();
                map = mapper.readValue(mapJson, GoalMap.class);
                return map.getGoal();
            }

        }
        map = mapper.readValue(response.getBody(), GoalMap.class);
        return map.getGoal();
    }




    @Getter
    @Setter
    static class GoalMap{
        String[][] goal;
    }

    @Getter
    static class RequestBody{
        String candidateId = clientID;
        int row, column;

        public RequestBody(int row, int col){
            this.row = row;
            this.column = col;
        }
    }


}
