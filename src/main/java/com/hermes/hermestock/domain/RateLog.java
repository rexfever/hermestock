package com.hermes.hermestock.domain;

import com.hermes.hermestock.service.Common;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter @Setter
public class RateLog {
    @Id @GeneratedValue
    private Long id;
    private String code;
    private boolean rate; //true : up, false :down
    private Date date;

    public void setRateLog(String code, boolean rate, String date){
        Common common = new Common();
        this.code = code;
        this.rate = rate;
        this.date = common.strToDate(date);

    }
}
