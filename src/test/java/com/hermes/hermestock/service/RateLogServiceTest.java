package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.RateLog;
import com.hermes.hermestock.domain.TradeLog;
import com.hermes.hermestock.repository.RateLogRepository;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RateLogServiceTest {

    @Autowired RateLogRepository rateLogRepository;
    @Autowired RateLogService rateLogService;

    CrawlerService cs = new CrawlerService();
    FileService fs = new FileService();
    Common common = new Common();
    @Test
    public void 로그저장테스트(){
        //given
        cs.getStockConditionCsv(3);
        try {
            Thread.sleep(10000L);
        } catch(Exception ex){
            System.out.println("ex = " + ex);
        }
        //when
        List<String> files = fs.getFileList(fs.originPath);
        List<RateLog> rateLogs = fs.readFile(files.get(0));
        List<RateLog> savedList = rateLogService.saveAllLog(rateLogs);
        //then
        fs.deleteFiles(fs.originPath);
        assertEquals(rateLogs.size() , savedList.size());

    }

    @Test
    public void 테스트여부() {
        boolean isTest = common.isTest();
        int time = 0;
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        time = Integer.parseInt(now.format(formatter));
        if(time < 1540)
            assertEquals(isTest, 1540 > time);
        else if(time >1550)
            assertEquals(isTest, 1550 < time);

    }

}
