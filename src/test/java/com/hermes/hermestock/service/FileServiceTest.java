package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.RateLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceTest {

    CrawlerService cs = new CrawlerService();
    FileService fs = new FileService();
    @Test
    public void Top5파일다운받고_목표디렉토리로_이동() throws IOException {
    //크롤링 해서 다운 받은 파일을 목표 디렉토리로 이동하고 다운 받은 파일 수 와 이동한 파일수를 비교한다.
    //given
        cs.getTop5Csv("20220228");
        try {
            Thread.sleep(10000L);
        } catch(Exception ex){
            System.out.println("ex = " + ex);
        }
        List<String> fileNames =  fs.getFileList(fs.originPath);
    //when
        for(String name : fileNames){
            fs.moveFile(name, fs.originPath, fs.top5Path);
        }

    //then
        List<String> movedFileNames =  fs.getFileList(fs.top5Path);
        fs.deleteFiles(fs.originPath);
        fs.deleteFiles(fs.top5Path);
        assertEquals(fileNames.size(), movedFileNames.size());
    }

    @Test
    public void Condition파일다운받고_목표디렉토리로_이동() throws IOException {
        //크롤링 해서 다운 받은 파일을 목표 디렉토리로 이동하고 다운 받은 파일 수 와 이동한 파일수를 비교한다.
        //given
        fs.deleteFiles(fs.originPath);
        cs.getStockConditionCsv(3);
        try {
            Thread.sleep(10000L);
        } catch(Exception ex){
            System.out.println("ex = " + ex);
        }
        List<String> fileNames =  fs.getFileList(fs.originPath);
        //when
        for(String name : fileNames){
            fs.moveFile(name, fs.originPath, fs.conditionPath);
        }

        //then
        List<String> movedFileNames =  fs.getFileList(fs.conditionPath);
        assertEquals(fileNames.size(), movedFileNames.size());
        fs.deleteFiles(fs.conditionPath);

    }

}
