package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.RateLog;
import com.hermes.hermestock.domain.TradeLog;
import com.hermes.hermestock.repository.RateLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Common {

    FileService fileService = new FileService();
    CrawlerService crawlerService = new CrawlerService();
    TradeLogService tradeLogService;
    RateLogService rateLogService;
    private static final Logger log = LoggerFactory.getLogger(Common.class);

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

    public boolean isTest(LocalTime now){
        boolean isTest = true;

        LocalTime startTime = LocalTime.of(15,44,00);
        LocalTime endTime = LocalTime.of(16,59,59);
        System.out.println("now = " + now);
        if(now.isAfter(startTime) && now.isBefore(endTime))
            isTest = false;

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

    public void Top5DataMigration(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate eDate = LocalDate.now();
        LocalDate sDate = eDate.minusMonths(6);
        //Top5 정보 가져와서 저장
        while(!sDate.equals(eDate.plusDays(1))){
            if(!sDate.getDayOfWeek().equals(DayOfWeek.SATURDAY))
                if (!sDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    System.out.println(sDate + " " + sDate.getDayOfWeek() + " 파일 가져오기");
                    this.getTop5Files4Mig(sDate.format(dtf).toString());

                    System.out.println(sDate + " " + sDate.getDayOfWeek() + " DB 저장하기");
                    this.saveTop5data();
                }
            sDate = sDate.plusDays(1);
        }
    }

    public int getTop5Files4Mig(String targetDate){
        int fileCount = 0;
        while(fileCount < 4) {
            fileService.deleteFiles(fileService.originPath);
            fileService.deleteFiles(fileService.top5Path);
            crawlerService.getTop5Csv(targetDate);
            try {
                Thread.sleep(10000L);
            } catch(Exception ex){
                log.info("ex = " + ex);
            }
            List<String> fileNames =  fileService.getFileList(fileService.originPath);
            for(String name : fileNames){
                fileService.moveFile(name, fileService.originPath, fileService.top5Path);
            }
            fileCount = fileNames.size();
        }
        return fileCount;
    }

    public void saveTop5data(){
        List<TradeLog> tradeLogList = new ArrayList<>();
        List<String> fileNames =  fileService.getFileList(fileService.top5Path);
        int order = 0;
        for(String name : fileNames){
            List<TradeLog> tmpList = fileService.readFile(name,order);
            tradeLogList.addAll(tmpList);
            order++;
        }
        List<TradeLog> savedLogList = tradeLogService.saveAllLog(tradeLogList);
    }

    public void RateDataMigration(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate eDate = LocalDate.now();
        LocalDate sDate = eDate.minusMonths(6);
        //Top5 정보 가져와서 저장
        while(!sDate.equals(eDate.plusDays(1))){
            if(!sDate.getDayOfWeek().equals(DayOfWeek.SATURDAY))
                if (!sDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    this.getRateLog4Mig(sDate.format(dtf).toString());
                }
            sDate = sDate.plusDays(1);
        }
    }


    public void getRateLog4Mig(String targetDate){

        fileService.deleteFiles(fileService.originPath);
        fileService.deleteFiles(fileService.conditionPath);
        // 종목 시세 period 2: 1day period 3: 1M period 4: 6M
        crawlerService.getStockConditionCsv(2, targetDate);
        try {
            Thread.sleep(10000L);
        } catch(Exception ex){
            log.info("ex = " + ex);
        }
        List<String> fileNames =  fileService.getFileList(fileService.originPath);
        String fileName = fileService.moveFile(fileNames.get(0), fileService.originPath, fileService.conditionPath);
        //<-여가까지 파일 가져와서 무브
        List<RateLog> rateLogs = fileService.readFile(fileName, "");
        List<RateLog> savedList = rateLogService.saveAllLog(rateLogs);
        log.info("%d 개의 종목정보가 입력 되었습니다.", savedList.size());

    }


}
