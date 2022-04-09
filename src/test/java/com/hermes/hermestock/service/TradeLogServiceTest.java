package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.TradeLog;
import com.hermes.hermestock.repository.TradeLogRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TradeLogServiceTest {

    @Autowired TradeLogService tradeLogService;
    @Autowired TradeLogRepository tradeLogRepository;

    @Test
    public void 로그등록() throws Exception{
        //given
        List<TradeLog> tradeLogs = new ArrayList<>();
        TradeLog tradeLog = new TradeLog();
        TradeLog tradeLog1 = new TradeLog();
        TradeLog tradeLog2 = new TradeLog();
        TradeLog tradeLog3 = new TradeLog();
        tradeLog.setStock("11111", "stock1", "100000", "100000", "1111111", "111111", "222222", "33333", "20120202", "KOSPI", "FOREIGN" );
        tradeLog1.setStock("11111", "stock1", "100000", "100000", "1111111", "111111", "222222", "33333", "20120202", "KOSPI", "FOREIGN" );
        tradeLog2.setStock("11111", "stock1", "100000", "100000", "1111111", "111111", "222222", "33333", "20120202", "KOSPI", "FOREIGN" );
        tradeLog3.setStock("11111", "stock1", "100000", "100000", "1111111", "111111", "222222", "33333", "20120202", "KOSPI", "FOREIGN" );
        tradeLogs.add(tradeLog);
        tradeLogs.add(tradeLog1);
        tradeLogs.add(tradeLog2);
        tradeLogs.add(tradeLog3);

        //when
        List<TradeLog> savedList = tradeLogService.saveAllLog(tradeLogs);
        //then

        assertEquals(tradeLogs.size() , savedList.size());
    }


}