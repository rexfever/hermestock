package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.RateLog;
import com.hermes.hermestock.domain.TradeLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonTest {
    Common common = new Common();
    FileService fileService = new FileService();
    CrawlerService crawlerService = new CrawlerService();
    private static final Logger log = LoggerFactory.getLogger(Common.class);

    @Autowired
    TradeLogService tradeLogService;

    @Autowired
    RateLogService rateLogService;

    @Test
    public void 연산자테스트() {
        LocalDate eDate = LocalDate.now().minusDays(1);
        LocalDate sDate = eDate.minusDays(4);

        System.out.println(eDate);
        System.out.println(sDate);
    }


    @Test
    public void Top5마이그레이션테스트() {
        this.Top5DataMigration();
    }




    public void Top5DataMigration(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate eDate = LocalDate.now().minusDays(1);;
        LocalDate sDate = eDate.minusDays(4);
        //Top5 정보 가져와서 저장
        while(!sDate.equals(eDate.plusDays(1))){
            System.out.println(sDate + " " + sDate.getDayOfWeek());
            if(sDate.getDayOfWeek().getValue() < 6) {
                System.out.println(sDate + " " + sDate.getDayOfWeek() + " 파일 가져오기");
                boolean val = this.getTop5Files4Mig(sDate.format(dtf).toString());
                System.out.println("val = " + val);
                if(!val){
                   sDate.plusDays(1);
                   continue;
                }
                System.out.println(sDate + " " + sDate.getDayOfWeek() + " DB 저장하기");
                this.saveTop5data(sDate.format(dtf).toString());
            }
            sDate = sDate.plusDays(1);
        }
    }

    public boolean getTop5Files4Mig(String targetDate){
        int fileCount = 0;
        boolean result = true;
        while(fileCount < 4) {
            fileService.deleteFiles(fileService.originPath);
            fileService.deleteFiles(fileService.top5Path);
            result = crawlerService.getTop5Csv(targetDate);
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
        return result;
    }

    public void saveTop5data(String targetDate){
        List<TradeLog> tradeLogList = new ArrayList<>();
        List<String> fileNames =  fileService.getFileList(fileService.top5Path);
        int order = 0;
        for(String name : fileNames){
            List<TradeLog> tmpList = fileService.readFile4Mig(name,order,targetDate);
            tradeLogList.addAll(tmpList);
            order++;
        }
        List<TradeLog> savedLogList = tradeLogService.saveAllLog(tradeLogList);
    }


    @Test
    public void RateLog마이그레이션() {
        this.RateLogDataMigration();
    }


    public void RateLogDataMigration(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate eDate = LocalDate.now().minusDays(1);;
        LocalDate sDate = eDate.minusDays(4);
        while(!sDate.equals(eDate.plusDays(1))){
            if(sDate.getDayOfWeek().getValue() < 6)
                this.getRateLog4Mig(sDate.format(dtf).toString());
            sDate = sDate.plusDays(1);
        }
    }

    public void getRateLog4Mig(String targetDate){

        boolean result = false;
        fileService.deleteFiles(fileService.originPath);
        fileService.deleteFiles(fileService.conditionPath);
        // 종목 시세 period 2: 1day period 3: 1M period 4: 6M
        result = crawlerService.getStockConditionCsv(2, targetDate);
        try {
            Thread.sleep(10000L);
        } catch(Exception ex){
            log.info("ex = " + ex);
        }
        if(result) {
            List<String> fileNames = fileService.getFileList(fileService.originPath);
            String fileName = fileService.moveFile(fileNames.get(0), fileService.originPath, fileService.conditionPath);
            //<-여가까지 파일 가져와서 무브
            List<RateLog> rateLogs = fileService.readFile(fileName, targetDate);
            List<RateLog> savedList = rateLogService.saveAllLog(rateLogs);
            log.info("%d 개의 종목정보가 입력 되었습니다.", savedList.size());
        }
    }



}
