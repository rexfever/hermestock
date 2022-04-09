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

    @Test
    public void 파일읽기() throws IOException {
        String filePath = System.getProperty("user.dir") + "/CONDITION/test.";
        File file = new File(filePath).getAbsoluteFile(); // File객체 생성
        List<RateLog> rateLogs = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        int lineCount = -1;
        while (reader.readLine() != null) {
            lineCount++;
        }


        FileService fileService = new FileService();
        rateLogs = fileService.readFile(filePath);

        assertEquals(rateLogs.size(), lineCount);

        fileService.deleteFile(filePath);
    }
}