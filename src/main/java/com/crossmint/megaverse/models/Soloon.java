package com.crossmint.megaverse.models;

import lombok.Getter;

@Getter
public class Soloon extends AstralObject {

    private final static AstralName astralName = AstralName.SOLOON;
    private final MoonColors color;

    public Soloon(int row,int col,String name){
        setRow(row);
        setCol(col);

        if(name.contains(MoonColors.BLUE.name().toUpperCase())){
            this.color = MoonColors.BLUE;
        }else if(name.contains(MoonColors.RED.name().toUpperCase())){
            this.color = MoonColors.RED;
        }else if(name.contains(MoonColors.PURPLE.name().toUpperCase())){
            this.color = MoonColors.PURPLE;
        }else {
            this.color = MoonColors.WHITE;
        }
    }

    @Override
    public AstralName getAstralName() {
        return astralName;
    }

    @Getter
    public enum MoonColors{
        BLUE,
        RED,
        PURPLE,
        WHITE
    }

}
