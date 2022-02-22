package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final MessageService messageService;

    @Scheduled(fixedRate = 300000)
    public void printTimre(){
        LocalTime now = LocalTime.now();
        System.out.println(now); //
    }
    /***
     * 파일 읽어서 메세지 만드는 것 까지 완료
     * 스케쥴링 크론 잡 만들어야 함 -done
     * 메세지 보내는 함수 만들어야 함 -done
     * 메세지 보내고 파일 카운트 해서 4이면 성공 기록하고(작업 완료 여부 변수 만들고 저장)-> while loop로 해결 done
     * 작업 성공하면 파일 삭제 - done
     * 작업 성공하지 못하면 파일 삭제 후 재시도 - while loop로 해결
     */
    //@Scheduled(fixedRate = 10000000)
    @Scheduled(cron="30 45 15 * * MON-FRI")
    public int getFiles(){

        Crawler crawler = new Crawler();
        FileService fileService = new FileService();
        int fileCount = 0;
        while(fileCount < 4) {

            fileService.deleteFiles();
            crawler.getCSVfiles();
            try {
                Thread.sleep(10000L);
            } catch(Exception ex){
                System.out.println("ex = " + ex);
            }
            List<String> fileNames =  fileService.getFileList();
            fileCount = fileNames.size();
            System.out.println("in while fileCount = " + fileCount);
        }
        System.out.println("fileCount = " + fileCount);
        this.sendStockInfo();
        return 0;
    }
    public void sendStockInfo(){
        FileService fileService = new FileService();
        List<Stock> stockList = new ArrayList<>();
        List<String> fileNames =  fileService.getFileList();
        StringBuilder slackMessage = new StringBuilder();
        int order = 0, start = 0, next = 0;
        for(String name : fileNames){
            List<Stock> tmpList = fileService.readFile(name,order);
            System.out.println("tmpList.size() = " + tmpList.size());
            if(order == 2) {
                start = stockList.size();
            }
            stockList.addAll(tmpList);
            if(order==1 || order==3){
                slackMessage = messageService.makeContents(stockList.subList(start+1 ,start+6), stockList.subList(next+1, next+6), order);
                System.out.println("slackMessage = " + slackMessage);
                messageService.sendMessages(slackMessage);
            }

            next = start + tmpList.size();
            System.out.println("order = " + order + " start = " + start + " next = " + next +" stockLisrt = " + stockList.size());
            order++;

        }
        System.out.println("stockList = " + stockList.toArray().length);

    }

}
