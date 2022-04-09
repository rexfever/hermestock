package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.TradeLog;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final MessageService messageService;
    private final TradeLogService tradeLogService;
    private final CrawlerService crawlerService;
    private final Common common;
    private final FileService fileService;




    //@Scheduled(fixedRate = 100000)
    //@Scheduled(cron="30 00 19 * * MON-FRI")
    public void getTodayStockCondition(){
        /**
         * 종목 시세
         * period 2: 1day
         * period 3: 1M
         */
        crawlerService.getStockConditionCsv(2);
    }

    @Scheduled(fixedRate = 300000)
    public void printTime(){
        LocalTime now = LocalTime.now();
        System.out.println(now); //
    }

    //@Scheduled(fixedRate = 10000000)//테스트용
    @Scheduled(cron="30 45 15 * * MON-FRI")
    public int getFiles(){

        int fileCount = 0;
        while(fileCount < 4) {

            fileService.deleteFiles();
            crawlerService.getTop5Csv("20220228");
            //crawlerService.getTop5Csv(common.getToday());
            try {
                Thread.sleep(10000L);
            } catch(Exception ex){
                log.info("ex = " + ex);
            }
            List<String> fileNames =  fileService.getFileList();
            fileCount = fileNames.size();
            System.out.println("in while fileCount = " + fileCount);
        }
        System.out.println("fileCount = " + fileCount);
        this.sendStockInfo();
        return fileCount;
    }
    private void sendStockInfo(){
        List<TradeLog> tradeLogList = new ArrayList<>();
        List<String> fileNames =  fileService.getFileList();
        StringBuilder slackMessage = new StringBuilder();
        int order = 0, start = 0, next = 0;
        for(String name : fileNames){
            List<TradeLog> tmpList = fileService.readFile(name,order);
            System.out.println("tmpList.size() = " + tmpList.size());
            if(order == 2) {
                start = tradeLogList.size();
            }
            tradeLogList.addAll(tmpList);
            if(order==1 || order==3){
                //slackMessage = messageService.makeContents(tradeLogList.subList(start+1 ,start+6), tradeLogList.subList(next+1, next+6), order);
                slackMessage = messageService.makeContents(tradeLogList.subList(start ,start+5), tradeLogList.subList(next, next+5), order);
                System.out.println("slackMessage = " + slackMessage);
                messageService.sendMessages(slackMessage);
            }

            next = start + tmpList.size();
            System.out.println("order = " + order + " start = " + start + " next = " + next +" stockLisrt = " + tradeLogList.size());
            order++;

        }
        System.out.println("stockList = " + tradeLogList.toArray().length);
    }


    //@Scheduled(fixedRate = 100000)
    //@Scheduled(cron="30 45 15 * * MON-FRI")
    public int saveRateLog(){

        int fileCount = 0;

        // 종목 시세 period 2: 1day period 3: 1M
        crawlerService.getStockConditionCsv(3);

        log.info("fileCount = " + fileCount);
        return fileCount;
    }


    @Scheduled(cron="30 45 18 * * MON-FRI")
    public static void saveTradeLogs(){

    }



}
