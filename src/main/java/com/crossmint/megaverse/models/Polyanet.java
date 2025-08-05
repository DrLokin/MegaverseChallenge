package com.crossmint.megaverse.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Polyanet extends AstralObject {

    private final static AstralName astralName = AstralName.POLYANET;

    public Polyanet(int row, int col){
        setRow(row);
        setCol(col);
    }

    @Override
    public AstralName getAstralName() {
        return astralName;
    }
}
