package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.Stock;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileService {

    public List<String> getFileList() {
        String path = System.getProperty("user.home") + "/Downloads";
        path = System.getProperty("user.dir");
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

    public List<Stock> readFile(String fileName, Integer fileOrder) {
        List<Stock> stocks = new ArrayList<>();
        File csv = new File(fileName);
        Common common = new Common();
        String today = common.getToday();
        //today = "20220203";
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
        int ii =0;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(csv), "euc-kr"));
            while ((line = br.readLine()) != null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
                Stock stock = new Stock();
                String[] values = line.split(","); // 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.
                stock.setStock(values[0].replace("\"",""),values[1].replace("\"",""),
                        values[2].replace("\"",""),values[3].replace("\"",""),
                        values[4].replace("\"",""),values[5].replace("\"",""),
                        values[6].replace("\"",""),values[7].replace("\"",""),
                        today, market, buyer);
                stocks.add(stock);
                if(ii >0 && ii < 6){
                    //System.out.println(stocks.get(ii).getName()+" = "+stocks.get(ii).getPbpayment());
                }
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
        return stocks;
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

    public int deleteFiles(){
        List<String> fileNames =  this.getFileList();
        int result = 0;
        for(String filename : fileNames){
            System.out.println("Deleted filename = " + filename);
            result = deleteFile(filename);
        }
        return result;
    }
}


