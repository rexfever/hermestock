package com.hermes.hermestock.domain;

public enum Market {
    KOSPI("4"), KOSDAQ("6");
    private final String codeOfMarket;

    Market(String codeOfMarket) { this.codeOfMarket = codeOfMarket; }
    public String getCodeOfMarket() { return codeOfMarket; }
}
