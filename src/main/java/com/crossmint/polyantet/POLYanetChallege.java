package com.crossmint.polyantet;


import com.crossmint.polyantet.models.AstralName;
import com.crossmint.polyantet.models.AstralObject;
import com.crossmint.polyantet.models.Cometh;
import com.crossmint.polyantet.models.Polyanet;
import com.crossmint.polyantet.service.ApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class POLYanetChallege {

    public static void main(String[] args) {

        ApiService apiService = new ApiService();
        List<AstralObject> astralObjects = new ArrayList<>();

        String[][] targetMap = null;
        try{
            targetMap = apiService.getGoalMap();
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }

        for(int row = 0;row<targetMap.length;row++){
            for(int col = 0;col<targetMap.length;col++){
                astralObjects.add(initAstralObject(targetMap[row][col],row,col));
            }
        }

        astralObjects.stream()
                .filter(Objects::nonNull)
                .forEach(obj ->{
                    int s = apiService.postAstralObject(obj);
                    System.out.printf("Status code for request: %d\n",s);
                    try{
                        Thread.sleep(750);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

    }



    public static AstralObject initAstralObject(String name, int row, int col){
        if(name.equals(AstralName.POLYANET.name())){
            return new Polyanet(row,col);
        }

        if(name.equals(AstralName.COMETH.name())){
            return new Cometh(row,col);
        }

        if(name.equals(AstralName.SOLOON.name())){
            return new Cometh(row,col);
        }

        return null;

    }


}