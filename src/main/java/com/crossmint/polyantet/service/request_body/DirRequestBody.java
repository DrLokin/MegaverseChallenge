package com.crossmint.polyantet.service.request_body;

import lombok.Getter;

@Getter
public class DirRequestBody extends RequestBody{

    private final String direction;

    public DirRequestBody(String candidateId,int row,int column,String direction) {
        super(candidateId,row, column);
        this.direction = direction;
    }
}
