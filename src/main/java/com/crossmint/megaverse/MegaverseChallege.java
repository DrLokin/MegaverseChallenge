package com.crossmint.megaverse;


import com.crossmint.megaverse.models.AstralObject;
import com.crossmint.megaverse.service.ApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class MegaverseChallege {

    public static void main(String[] args) {

        ApiService apiService = new ApiService();
        List<AstralObject> astralObjects = new ArrayList<>();

        String[][] targetMap;
        try{
            targetMap = apiService.getMetaverseGoal();
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }

        for(int row = 0;row<targetMap.length;row++){
            for(int col = 0;col<targetMap.length;col++){
                astralObjects.add(AstralObject.createAstralObject(targetMap[row][col],row,col));
            }
        }

        astralObjects.stream()
                .filter(Objects::nonNull)
                .forEach(obj ->{
                    int s = apiService.postAstralObject(obj);
                    System.out.printf("Status code for %s %d %d request: %d\n",
                            obj.getAstralName(),
                            obj.getRow(),
                            obj.getCol(),s);
                    try{
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

    }


}