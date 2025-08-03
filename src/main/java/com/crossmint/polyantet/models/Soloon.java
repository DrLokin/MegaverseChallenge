package com.crossmint.polyantet.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
public class Soloon extends AstralObject{

    private final static AstralName astralName = AstralName.SOLOON;

    @Override
    public AstralName getAstralName() {
        return astralName;
    }
}
