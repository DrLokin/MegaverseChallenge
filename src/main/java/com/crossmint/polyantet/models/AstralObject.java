package com.crossmint.polyantet.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class AstralObject {
    int row,col;

    protected AstralObject() {
    }

    public abstract AstralName getAstralName();
}
