package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.RateLog;
import com.hermes.hermestock.domain.TradeLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
@Service
@Slf4j
public class FileService {
    public String originPath = System.getProperty("user.dir");
    public String top5Path = originPath+"/TOP5";
    public String conditionPath = originPath+"/CONDITION";

    public List<String> getFileList(String path) {
        List<String> fileNames = new ArrayList<>();
        File[] files = new File(path).listFiles();
        String EXTENSIONS = ".csv";
        for(File file : files){
            if(file.isFile()){
                if(file.getName().indexOf("data_") < 0){
                    continue;
                }
                if(EXTENSIONS.contains(file.getName().substring(file.getName().lastIndexOf(".")))){
                    System.out.println("file = " + file);
                    fileNames.add(file.toString());
                }
            }
        }
        Collections.sort(fileNames);
        return fileNames;
    }

    public int deleteFile(String filename){
        File deleteFile = new File(filename);

        if(deleteFile.exists()) {
            deleteFile.delete();
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }

        return 0;
    }

    public int deleteFiles(String path){
        List<String> fileNames =  this.getFileList(path);
        int result = fileNames.size();
        for(String filename : fileNames){
            System.out.println("Deleted filename = " + filename);
            result = deleteFile(filename);
        }
        log.info(result + "file(s) deleted.");
        return result;
    }

    public List<TradeLog> readFile(String fileName, Integer fileOrder) {
        List<TradeLog> tradeLogs = new ArrayList<>();
        File csv = new File(fileName);
        Common common = new Common();
        String today = common.getDate();
        BufferedReader br = null;
        String line = "";
        String market = "KOSPI";
        String buyer = "기관";
        if( fileOrder > 1) {
            market = "KOSDAQ";
        }
        if(fileOrder%2 == 1){
            buyer = "외국인";
        }

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(csv), "iso-8859-1"));
            int ii =0;
            while ((line = br.readLine()) != null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
                TradeLog tradeLog = new TradeLog();
                String readLine = new String(line.getBytes("ISO-8859-1"),"EUC-KR");
                System.out.println("readLine = " + readLine);
                
                String[] values = readLine.split(","); // 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.
                if(values[0].equals("종목코드")) continue; // 타이틀 라인인 경우 pass , 메세지의 start, next 에서 1 빼야 함

                tradeLog.setStock(values[0].replace("\"",""),values[1].replace("\"",""),
                        values[2].replace("\"",""),values[3].replace("\"",""),
                        values[4].replace("\"",""),values[5].replace("\"",""),
                        values[6].replace("\"",""),values[7].replace("\"",""),
                        today, market, buyer);
                tradeLogs.add(tradeLog);
                ii++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close(); // 사용 후 BufferedReader를 닫아준다.
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return tradeLogs;
    }

    /***
     * 등락률 csv 를 읽어서 배열로 리턴한다.
     * @param fileName
     * @return
     */
    public List<RateLog> readFile(String fileName){
        List<RateLog> rateLogs = new ArrayList<>();
        File csv = new File(fileName);
        Common common = new Common();
        String today = common.getDate();
        BufferedReader br = null;
        String line = "";
        BasicFileAttributes attrs;
        try {
            attrs = Files.readAttributes(csv.toPath(), BasicFileAttributes.class);
            FileTime time = attrs.creationTime();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(csv), "iso-8859-1"));

            while ((line = br.readLine()) != null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
                RateLog rateLog = new RateLog();
                String readLine = new String(line.getBytes("ISO-8859-1"),"EUC-KR");

                String[] values = readLine.split(","); // 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.
                if(values[0].equals("종목코드")) continue; // 타이틀 라인인 경우 pass , 메세지의 start, next 에서 1 빼야 함
                rateLog.setRateLog(values[0].replace("\"", ""), common.isPositive(values[4].replace("\"", "")), today);
                rateLogs.add(rateLog);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close(); // 사용 후 BufferedReader를 닫아준다.
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("rateLogs = " + rateLogs.size());
        return rateLogs;
    }


    public String moveFile(String originFile, String originPath, String targetPath) {

        try{

            File file = new File(originFile);
            String targetFile = originFile.replace(originPath, targetPath);
            if(file.renameTo(new File(targetFile))){ //파일 이동
                System.out.println("Success");
            return targetFile; //성공시 성공 파일 경로 return
            }else{
                System.out.println("Fail");
                return "Fail";
            }

        }catch(Exception e){
            e.printStackTrace();
            return "Fail";
        }

    }

}


