package com.crossmint.polyantet.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AstralObject {

    //Basic fields that all AstralObject should have.
    int row,col;

    //Each subclass will define which enum it will use as a name.
    public abstract AstralName getAstralName();


    //Enums of the subclasses with the string to attach to the URL.
    @Getter
    public enum AstralName {
        POLYANET("polyanets"),
        SOLOON("soloons"),
        COMETH("comeths");

        private final String urlSuffix;

        AstralName(String urlSuffix) {
            this.urlSuffix = urlSuffix;
        }
    }

    //Abstract class also has method for the construction of a subclass object based on the name parameter.
    public static AstralObject createAstralObject(String name, int row, int col){

        if(name.contains(AstralName.POLYANET.name().toUpperCase())){
            return new Polyanet(row,col);
        }

        if(name.contains(AstralName.SOLOON.name().toUpperCase())){
            return new Soloon(row,col,name);
        }

        if(name.contains(AstralName.COMETH.name().toUpperCase())){
            return new Cometh(row,col,name);
        }

        return null;

    }




}
