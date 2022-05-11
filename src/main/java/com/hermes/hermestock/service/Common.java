package com.hermes.hermestock.service;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
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

    public Date strToDate(String date){

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        Date targetDate = new Date();
        try{
            targetDate = transFormat.parse(date);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return targetDate;
    }

    public boolean isPositive(String value){
        boolean result = value.indexOf("-") >= 0? true : false;
        return result;
    }

    public boolean isTest(){
        boolean isTest = false;
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        System.out.println("now.format(formatter) = " + now.format(formatter));

        if(now.format(formatter) != "1544")isTest = true;
        if(now.format(formatter) != "1545")isTest = true;
        if(now.format(formatter) != "1546")isTest = true;
        if(now.format(formatter) != "1547")isTest = true;
        if(now.format(formatter) != "1548")isTest = true;
        if(now.format(formatter) != "1549")isTest = true;
        if(now.format(formatter) != "1550")isTest = true;
        System.out.println("isTest = " + isTest);
        return isTest; 
    }

    public static void FindEncoding(String s)
    {
        try {
            String word = s;
            System.out.println("utf-8(1) : " + new String(word.getBytes("utf-8"), "euc-kr"));
            System.out.println("utf-8(2) : " + new String(word.getBytes("utf-8"), "ksc5601"));
            System.out.println("utf-8(3) : " + new String(word.getBytes("utf-8"), "x-windows-949"));
            System.out.println("utf-8(4) : " + new String(word.getBytes("utf-8"), "iso-8859-1"));

            System.out.println("iso-8859-1(1) : " + new String(word.getBytes("iso-8859-1"), "euc-kr"));
            System.out.println("iso-8859-1(2) : " + new String(word.getBytes("iso-8859-1"), "ksc5601"));
            System.out.println("iso-8859-1(3) : " + new String(word.getBytes("iso-8859-1"), "x-windows-949"));
            System.out.println("iso-8859-1(4) : " + new String(word.getBytes("iso-8859-1"), "utf-8"));

            System.out.println("euc-kr(1) : " + new String(word.getBytes("euc-kr"), "ksc5601"));
            System.out.println("euc-kr(2) : " + new String(word.getBytes("euc-kr"), "utf-8"));
            System.out.println("euc-kr(3) : " + new String(word.getBytes("euc-kr"), "x-windows-949"));
            System.out.println("euc-kr(4) : " + new String(word.getBytes("euc-kr"), "iso-8859-1"));

            System.out.println("ksc5601(1) : " + new String(word.getBytes("ksc5601"), "euc-kr"));
            System.out.println("ksc5601(2) : " + new String(word.getBytes("ksc5601"), "utf-8"));
            System.out.println("ksc5601(3) : " + new String(word.getBytes("ksc5601"), "x-windows-949"));
            System.out.println("ksc5601(4) : " + new String(word.getBytes("ksc5601"), "iso-8859-1"));

            System.out.println("x-windows-949(1) : " + new String(word.getBytes("x-windows-949"), "euc-kr"));
            System.out.println("x-windows-949(2) : " + new String(word.getBytes("x-windows-949"), "utf-8"));
            System.out.println("x-windows-949(3) : " + new String(word.getBytes("x-windows-949"), "ksc5601"));
            System.out.println("x-windows-949(4) : " + new String(word.getBytes("x-windows-949"), "iso-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void checkEncoding(String s) throws UnsupportedEncodingException {
        String originalStr = s;
        byte[] bytes = originalStr.getBytes("utf-8");
        originalStr = new String(bytes);

        String[] charSet = {"utf-8", "euc-kr", "ksc5601", "iso-8859-1", "x-windows-949"};
        for(int i = 0; i<charSet.length; i++){
            for(int j = 0; j<charSet.length; j++){
                try{
                    System.out.println("[" + charSet[i] + "," + charSet[j] + "]" + new String(originalStr.getBytes(charSet[i]), charSet[j]));
                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
