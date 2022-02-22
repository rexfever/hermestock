package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.Stock;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Common {
    public String getDate(){
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return now.format(formatter);
    }

    public String getToday(){
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM 월 dd일 E요일");
        return now.format(formatter);
    }

}
