package com.crossmint.megaverse.service;


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




}
