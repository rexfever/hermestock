package com.hermes.hermestock.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

public class Stock {
    String code;
    String name;
    String svolume;
    String bvolume;
    String pbvolume;
    String spayment;
    String bpayment;
    String pbpayment;
    String date;
    String market;
    String buyer;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSvolume() {
        return svolume;
    }

    public void setSvolume(String svolume) {
        this.svolume = svolume;
    }

    public String getBvolume() {
        return bvolume;
    }

    public void setBvolume(String bvolume) {
        this.bvolume = bvolume;
    }

    public String getPbvolume() {
        return pbvolume;
    }

    public void setPbvolume(String pbvolume) {
        this.pbvolume = pbvolume;
    }

    public String getSpayment() {
        return spayment;
    }

    public void setSpayment(String spayment) {
        this.spayment = spayment;
    }

    public String getBpayment() {
        return bpayment;
    }

    public void setBpayment(String bpayment) {
        this.bpayment = bpayment;
    }

    public String getPbpayment() {
        return pbpayment;
    }

    public void setPbpayment(String pbpayment) {
        this.pbpayment = pbpayment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public void setStock(String code, String name, String svoluem, String bvolume, String pbvolume, String spayment, String bpayment, String pbpayment, String date, String market, String buyer) {
        this.code = code;
        this.name = name;
        this.svolume = svoluem;
        this.bvolume = bvolume;
        this.pbvolume = pbvolume;
        this.spayment = spayment;
        this.bpayment = bpayment;
        this.pbpayment = pbpayment;
        this.date = date;
        this.market = market;
        this.buyer = buyer;

    }

}
