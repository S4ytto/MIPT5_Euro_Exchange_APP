package com.example.mipt5asyc;

public class Currency {
    private final String code;
    private final String rate;

    public Currency(String code, String rate) {
        this.code = code;
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public String getRate() {
        return rate;
    }
}
