package com.crossmint.polyantet.service.request_body;

import lombok.Getter;

@Getter
public class ColorRequestBody extends RequestBody {
    private final String color;

    public ColorRequestBody(String candidateId,int row, int column,String color) {
        super(candidateId,row, column);
        this.color = color;
    }
}
