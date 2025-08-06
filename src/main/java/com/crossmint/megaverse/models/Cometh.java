package com.crossmint.megaverse.models;

import lombok.Getter;

@Getter
public class Cometh extends AstralObject {

    private final static AstralName astralName = AstralName.COMETH;
    private final ComethDirs direction;

    public Cometh(int row,int col,String name){
        setRow(row);
        setCol(col);
        if(name.contains(ComethDirs.UP.name().toUpperCase())){
            this.direction = ComethDirs.UP;
        }else if(name.contains(ComethDirs.DOWN.name().toUpperCase())){
            this.direction = ComethDirs.DOWN;
        }else if(name.contains(ComethDirs.LEFT.name().toUpperCase())){
            this.direction = ComethDirs.LEFT;
        }else {
            this.direction = ComethDirs.RIGHT;
        }
    }

    @Override
    public AstralName getAstralName() {
        return astralName;
    }


    @Getter
    public enum ComethDirs{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

}
