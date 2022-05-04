package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.Channel;
import com.hermes.hermestock.domain.TradeLog;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    Common common = new Common();
    private final ChannelService channelService;
    private String today = common.getToday();
    private String message_intro_kospi = "{" +
            "\"blocks\": [" +
            "{" +
            "\"type\": \"header\"," +
            "\"text\": {" +
            "\"type\": \"plain_text\"," +
            "\"text\": \"" + today + "\"," +
            "\"emoji\": true" +
            "}" +
            "}," +
            "{" +
            "\"type\": \"section\"," +
            "\"text\": {" +
            "\"type\": \"plain_text\"," +
            "\"text\": \"KOSPI\"," +
            "\"emoji\": true" +
            "}" +
            "}," +
            "{" +
            "\"type\": \"divider\"" +
            "}," +
            "{" +
            "\"type\": \"section\",\n" +
            "\"text\": {\n" +
            "\"type\": \"plain_text\",\n" +
            "\"text\": \"기관 순매수 TOP5\",\n" +
            "\"emoji\": true\n" +
            "}\n" +
            "},\n" +
            "{\n" +
            "\"type\": \"section\",\n" +
            "\"text\": {\n" +
            "\"type\": \"mrkdwn\",\n" +
            "\"text\": \" \n";
    private String message_intro_kosdaq = "{\n" +
            "\"blocks\": [\n" +
            "{\n" +
            "\"type\": \"section\",\n" +
            "\"text\": {\n" +
            "\"type\": \"plain_text\",\n" +
            "\"text\": \"KOSDAQ\",\n" +
            "\"emoji\": true\n" +
            "}\n" +
            "},\n" +
            "{\n" +
            "\"type\": \"divider\"\n" +
            "},\n" +
            "{\n" +
            "\"type\": \"section\",\n" +
            "\"text\": {\n" +
            "\"type\": \"plain_text\",\n" +
            "\"text\": \"기관 순매수 TOP5\",\n" +
            "\"emoji\": true\n" +
            "}\n" +
            "},\n" +
            "{\n" +
            "\"type\": \"section\",\n" +
            "\"text\": {\n" +
            "\"type\": \"mrkdwn\",\n" +
            "\"text\": \"";
    private String message_body =
            //"<https://finance.naver.com/item/main.naver?code=096770|카카오뱅크> : 1000000000 \\n<https://finance.naver.com/item/main.naver?code=096770|카카오뱅크> : 1000000000 <https://finance.naver.com/item/main.naver?code=096770|카카오뱅크> : 1000000000"+
            "\"" +
                    "}\n" +
                    "},\n" +
                    "{\n" +
                    "\"type\": \"section\",\n" +
                    "\"text\": {\n" +
                    "\"type\": \"plain_text\",\n" +
                    "\"text\": \"외국인 순매수 TOP5\",\n" +
                    "\"emoji\": true\n" +
                    "}\n" +
                    "},\n" +
                    "{\n" +
                    "\"type\": \"section\",\n" +
                    "\"text\": {\n" +
                    "\"type\": \"mrkdwn\",\n" +
                    "\"text\": \"";
    //"<https://finance.naver.com/item/main.naver?code=096770|카카오뱅크> : 1000000000 \\n<https://finance.naver.com/item/main.naver?code=096770|카카오뱅크> : 1000000000 \\n<https://finance.naver.com/item/main.naver?code=096770|카카오뱅크> : 1000000000 \\n<https://finance.naver.com/item/main.naver?code=096770|카카오뱅크> : 1000000000 \\n<https://finance.naver.com/item/main.naver?code=096770|카카오뱅크> : 1000000000"+"" +
    private String message_foot =
            "\"" +
                    "}\n" +
                    "},\n" +
                    "{\n" +
                    "\"type\": \"divider\"\n" +
                    "},\n" +
                    "{\n" +
                    "\"type\": \"divider\"\n" +
                    "}\n" +
                    "]\n" +
                    "}";

    private String queryUrl = "<https://finance.naver.com/item/main.naver?code=";

    private StringBuilder makeUrl(List<TradeLog> tradeLogs){
        StringBuilder stockContents = new StringBuilder();
        for (TradeLog tradeLog : tradeLogs) {
            stockContents.append(this.queryUrl);
            stockContents.append(tradeLog.getCode());
            stockContents.append("|");
            stockContents.append(tradeLog.getName());
            stockContents.append("> : ");
            DecimalFormat decFormat = new DecimalFormat("###,###");
            stockContents.append(decFormat.format(tradeLog.getPbpayment()));
            stockContents.append("\\n");

        }
        return stockContents;
    }

    public StringBuilder makeContents(List<TradeLog> stocks_k, List<TradeLog> stocks_f, int order){
        StringBuilder message = new StringBuilder();
        if(order <2 ) {
            message.append(this.message_intro_kospi);
        }else{
            message.append(this.message_intro_kosdaq);
        }
        message.append(this.makeUrl(stocks_k)).append(this.message_body).append(this.makeUrl(stocks_f)).append(this.message_foot);
        log.info(String.valueOf(message));
        return message;
    }

    @SneakyThrows
    public void sendMessages(StringBuilder message){
        HttpHeaders headers = new HttpHeaders();;
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String jsonStr=message.toString();//sb는 StringBuilder 객체. API의response를 통해 값을 받은 상태.
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(jsonStr);
        JSONObject jsonObj = (JSONObject) obj;
        HttpEntity<String> entity = new HttpEntity<String>(jsonStr , headers);
        RestTemplate restTemplate = new RestTemplate();
        List<Channel> channelList = channelService.findChannelList();
        int result = 0;
        for(Channel channel : channelList ){
            try {
                if(common.isTest())
                    restTemplate.postForObject(channelList.get(0).getUrl(), entity, String.class); //rex channel test
                else
                    restTemplate.postForObject(channel.getUrl(), entity, String.class);

            }catch (Exception e){
                HttpEntity<String> entity_e = new HttpEntity<String>(e.toString() , headers);
                restTemplate.postForObject(channelList.get(0).getUrl(), entity_e, String.class);
            }
            result++;
        }
        String finalMessage = "{\"blocks\": [{\"type\": \"section\",\"text\":" +
                "{\"type\": \"mrkdwn\"," +
                "\"text\": \"Send to" + Integer.toString(channelList.size())  + "channels\" }}]}";
        HttpEntity<String> entity_r = new HttpEntity<String>(finalMessage , headers);
        if(result==channelList.size()) {
            restTemplate.postForObject(channelList.get(0).getUrl(), entity_r, String.class);
        }
    }

}
