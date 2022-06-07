package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.RateLog;
import com.hermes.hermestock.repository.RateLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateLogService {

    @Autowired
    private final RateLogRepository rateRepository;

    public RateLogService(RateLogRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public List<RateLog> saveAllLog(List<RateLog> rateLogs){
        List<RateLog> savedLog = rateRepository.saveAll(rateLogs);
        return savedLog;
    }


}
