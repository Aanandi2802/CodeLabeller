package com.csci5308.codeLabeller.Enums;

/**
 * enum for any misc use.
 */
public enum MiscEnums {
    NumberOfPages (1);
    private final int value;

    MiscEnums(int i) {
        this.value = i;
    }

    public int getValue(){
        return value;
    }
}
