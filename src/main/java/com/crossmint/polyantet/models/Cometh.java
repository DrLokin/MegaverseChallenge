package com.crossmint.polyantet.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
public class Cometh extends AstralObject {
    private final static AstralName astralName = AstralName.COMETH;

    public Cometh(int row,int col){
        setRow(row);
        setCol(col);

    }

    @Override
    public AstralName getAstralName() {
        return astralName;
    }
}
