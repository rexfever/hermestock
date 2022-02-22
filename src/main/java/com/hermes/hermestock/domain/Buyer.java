package com.hermes.hermestock.domain;

public enum Buyer {
    INSTITUTIONAL("7050"),
    FOREIGN("9000");

    private String codeOfBuyer;

    Buyer(String codeOfBuyer) {
        this.codeOfBuyer = codeOfBuyer;
    }

    public String getCodeOfBuyer() {
        return codeOfBuyer;
    }
}
