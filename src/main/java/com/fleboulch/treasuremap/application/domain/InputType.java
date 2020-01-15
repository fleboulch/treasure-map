package com.fleboulch.treasuremap.application.domain;

public enum InputType {

    C ("Map"),
    M ("Mountain"),
    T ("Treasure");

    private String value;

    InputType(String value) {
        this.value = value;
    }
}
