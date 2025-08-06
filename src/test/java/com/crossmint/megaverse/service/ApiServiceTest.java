package com.crossmint.megaverse.service;


import com.crossmint.megaverse.models.AstralObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiServiceTest {

    ResponseEntity<Void> mockVoidResponse;
    ResponseEntity<String> mockStringResponse;
    HttpStatusCode mockStatus;
    HttpHeaders mockResponseHeaders;

    @BeforeEach
    public void setUp(){
        mockVoidResponse = mock(ResponseEntity.class);
        mockStringResponse = mock(ResponseEntity.class);
        mockStatus = mock(HttpStatus.class);
        mockResponseHeaders = mock(HttpHeaders.class);
    }

    @AfterEach
    public void tearDown(){
        mockVoidResponse = null;
        mockStringResponse = null;
        mockStatus = null;
        mockResponseHeaders = null;
    }



    @Test
    public void testGoalApiCall() throws JsonProcessingException {

        when(mockStringResponse.getBody()).thenReturn("{\"goal\":[[\"SPACE\",\"POLYANET\"],[\"BLUE_SOLOON\",\"LEFT_COMETH\"]]}");
        when(mockStringResponse.getStatusCode()).thenReturn(mockStatus);
        when(mockStringResponse.getHeaders()).thenReturn(mockResponseHeaders);
        when(mockStatus.is3xxRedirection()).thenReturn(false).thenReturn(true);
        when(mockResponseHeaders.getFirst(HttpHeaders.LOCATION)).thenReturn("redirect_url");

        try(MockedConstruction<RestTemplate> mockTemplate = Mockito.mockConstruction(RestTemplate.class,(mock,context)->{
                doReturn(mockStringResponse).when(mock).exchange(anyString(),
                        any(HttpMethod.class),
                        (HttpEntity<?>) any(HttpEntity.class),
                        any(Class.class));
            })
        ){
            ApiService service = new ApiService();

            String[][] map1 = service.getMetaverseGoal(),
                    map2;
            Assertions.assertEquals(2,map1.length);
            Assertions.assertEquals(2,map1[0].length);

            map2 = service.getMetaverseGoal();
            Assertions.assertTrue(Arrays.deepEquals(map1,map2));


        }

    }

    @Test
    public void testGoalApiCall_ErrorCase() throws JsonProcessingException {

        lenient().when(mockStringResponse.getBody()).thenReturn("{\"goal\":[[\"SPACE\",\"POLYANET\"],[\"BLUE_SOLOON\",\"LEFT_COMETH\"]]}");
        when(mockStringResponse.getStatusCode()).thenReturn(mockStatus);
        when(mockStatus.is3xxRedirection()).thenReturn(false);
        when(mockStatus.is2xxSuccessful()).thenReturn(true);
        when(mockStatus.value()).thenReturn(200);

        try(MockedConstruction<RestTemplate> mockTemplate = Mockito.mockConstruction(RestTemplate.class,(mock,context)->{
            doThrow(new HttpClientErrorException(HttpStatusCode.valueOf(429),"Too many requests"))
                    .when(mock)
                    .exchange(anyString(),
                            any(HttpMethod.class),
                            (HttpEntity<?>) any(HttpEntity.class),
                            any(Class.class)
                    );

            doReturn(mockStringResponse)
                    .when(mock)
                    .exchange(anyString(),
                            any(HttpMethod.class),
                            (HttpEntity<?>) any(HttpEntity.class),
                            any(Class.class),any(Class.class)
                    );
        })
        ){
            ApiService service = new ApiService();

            String[][] map = service.getMetaverseGoal();
            Assertions.assertEquals(2,map.length);
            Assertions.assertEquals(2,map[0].length);


        }

    }

    @Test
    public void testPostAstralObject() {

        when(mockVoidResponse.getStatusCode()).thenReturn(mockStatus);
        when(mockVoidResponse.getHeaders()).thenReturn(mockResponseHeaders);
        when(mockStatus.is3xxRedirection()).thenReturn(false)
                .thenReturn(true)
                .thenReturn(false);
        when(mockStatus.value()).thenReturn(200);
        when(mockResponseHeaders.getFirst(HttpHeaders.LOCATION)).thenReturn("redirect_url");

        try(MockedConstruction<RestTemplate> mockTemplate = Mockito.mockConstruction(RestTemplate.class,(mock,context)->{
            doReturn(mockVoidResponse)
                    .when(mock)
                    .exchange(anyString(),
                            any(HttpMethod.class),
                            (HttpEntity<?>) any(HttpEntity.class),
                            any(Class.class),any(Class.class)
                    );
        })
        ){
            ApiService service = new ApiService();

            int postPlanetStatus = service.postAstralObject(AstralObject.createAstralObject("POLYANET",1,1)),
                    postMoonStatus = service.postAstralObject(AstralObject.createAstralObject("PURPLE_SOLOON",2,2)),
                    postCometStatus = service.postAstralObject(AstralObject.createAstralObject("DOWN_COMETH",3,3));

            Assertions.assertEquals(200,postPlanetStatus);
            Assertions.assertEquals(200,postMoonStatus);
            Assertions.assertEquals(200,postCometStatus);


        }

    }


    @Test
    public void testPostAstralObject_ErrorCase() {

        when(mockVoidResponse.getStatusCode()).thenReturn(mockStatus);
        when(mockStatus.is3xxRedirection()).thenReturn(false);
        when(mockStatus.is2xxSuccessful()).thenReturn(true);
        when(mockStatus.value()).thenReturn(200);

        try(MockedConstruction<RestTemplate> mockTemplate = Mockito.mockConstruction(RestTemplate.class,(mock,context)->{
            doThrow(new HttpClientErrorException(HttpStatusCode.valueOf(500),"Client side issue."))
                    .doThrow(new HttpClientErrorException(HttpStatusCode.valueOf(429),"Too many requests."))
                    .doReturn(mockVoidResponse)
                    .when(mock)
                    .exchange(anyString(),
                            any(HttpMethod.class),
                            (HttpEntity<?>) any(HttpEntity.class),
                            any(Class.class),any(Class.class)
                    );

        })
        ){
            ApiService service = new ApiService();

            int postPlanetStatus = service.postAstralObject(AstralObject.createAstralObject("POLYANET",1,1));
            Assertions.assertEquals(500,postPlanetStatus);

            postPlanetStatus = service.postAstralObject(AstralObject.createAstralObject("POLYANET",2,7));
            Assertions.assertEquals(200,postPlanetStatus);


        }

    }




}
