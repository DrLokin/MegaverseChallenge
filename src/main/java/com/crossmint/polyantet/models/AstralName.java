package com.crossmint.polyantet.models;

import lombok.Getter;

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
