package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.TradeLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ScheduledTasksTest {

    CrawlerService cs = new CrawlerService();

    @Test
    public void saveRateLog() {

    }

    @Test
    public void saveTradeLogs() {
    }

    @Test
    public void deleteFilesLogs() {
    }

    @Test
    public void dataMigrationTest(){

    }
}
