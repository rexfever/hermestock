package com.hermes.hermestock.domain;

import com.hermes.hermestock.service.Common;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Getter @Setter
public class TradeLog {
    @Id @GeneratedValue
    private Long id;
    private String code;
    private String name;
    private Long svolume;
    private Long bvolume;
    private Long pbvolume;
    private Long spayment;
    private Long bpayment;
    private Long pbpayment;
    private Date date;
    private String market;
    private String buyer;


    public void setStock(String code, String name, String svoluem, String bvolume, String pbvolume, String spayment,
                         String bpayment, String pbpayment, String date, String market, String buyer) {

        Common common = new Common();


        this.code = code;
        this.name = name;
        this.svolume = Long.parseLong(svoluem.replace(",", ""));
        this.bvolume = Long.parseLong(bvolume.replace(",", ""));
        this.pbvolume = Long.parseLong(pbvolume.replace(",", ""));
        this.spayment = Long.parseLong(spayment.replace(",", ""));
        this.bpayment = Long.parseLong(bpayment.replace(",", ""));
        this.pbpayment = Long.parseLong(pbpayment.replace(",", ""));
        this.date = common.strToDate(date);
        this.market = market;
        this.buyer = buyer;

    }

}
