package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.RateLog;
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
    private final RateLogService rateLogService;
    private final CrawlerService crawlerService;
    private final Common common;
    private final FileService fileService;

    @Scheduled(fixedRate = 100000)
    public void printTime(){
        LocalTime now = LocalTime.now();
        System.out.println(now); //
    }

    @Scheduled(fixedRate = 10000000)//테스트용
    //@Scheduled(cron="30 45 15 * * MON-FRI")
    public int getTradeLogs(){

        int fileCount = 0;
        String targetDate = common.getDate();
        if(common.isTest()) targetDate = "20220228";
        while(fileCount < 4) {

            fileService.deleteFiles(fileService.originPath);
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
            System.out.println("in while fileCount = " + fileCount);
        }
        System.out.println("fileCount = " + fileCount);
        this.sendStockInfo();
        return fileCount;
    }
    private void sendStockInfo(){
        List<TradeLog> tradeLogList = new ArrayList<>();
        List<String> fileNames =  fileService.getFileList(fileService.top5Path);
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
                //slackMessage = messageService.makeContents(tradeLogList.subList(start+1 ,start+6), tradeLogList.subList(next+1, next+6), order); 옛날 코드
                slackMessage = messageService.makeContents(tradeLogList.subList(start ,start+5), tradeLogList.subList(next, next+5), order);
                System.out.println("slackMessage = " + slackMessage);
                messageService.sendMessages(slackMessage);
            }

            next = start + tmpList.size();
            System.out.println("order = " + order + " start = " + start + " next = " + next +" stockLisrt = " + tradeLogList.size());
            order++;

        }
        System.out.println("stockList = " + tradeLogList.toArray().length);
        int count = this.saveTradeLogs(tradeLogList);

    }

    public int saveTradeLogs(List<TradeLog> tradeLogList){
        List<TradeLog> savedLogList = tradeLogService.saveAllLog(tradeLogList);
        return savedLogList.size();
    }

    //@Scheduled(fixedRate = 100000)
    @Scheduled(cron="00 10 19 * * MON-FRI")
    public void getRateLog(){

        fileService.deleteFiles(fileService.originPath);
        fileService.deleteFiles(fileService.conditionPath);
        // 종목 시세 period 2: 1day period 3: 1M
        crawlerService.getStockConditionCsv(2);
        try {
            Thread.sleep(10000L);
        } catch(Exception ex){
            log.info("ex = " + ex);
        }
        List<String> fileNames =  fileService.getFileList(fileService.originPath);
        String fileName = fileService.moveFile(fileNames.get(0), fileService.originPath, fileService.conditionPath);
        //<-여가까지 파일 가져와서 무브
        int lines = saveRateLogs(fileName);
        log.info("%d 개의 종목정보가 입력 되었습니다.", lines);

    }

    public int saveRateLogs(String path){
        System.out.println("path = " + path);
        List<RateLog> rateLogs = fileService.readFile(path);
        List<RateLog> savedList = rateLogService.saveAllLog(rateLogs);
        return savedList.size();
    }

    //@Scheduled(fixedRate = 100000)
    @Scheduled(cron="00 00 20 * * MON-FRI")
    public void deleteFilesLogs(){
        int fileCount = 0;
        fileCount = fileService.deleteFiles(fileService.originPath);
        fileCount = fileCount +fileService.deleteFiles(fileService.top5Path);
        fileCount = fileCount +fileService.deleteFiles(fileService.conditionPath);
        log.info(fileCount+"개의 파일이 삭제되었습니다");
    }

}
