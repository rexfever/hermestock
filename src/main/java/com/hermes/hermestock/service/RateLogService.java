package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.RateLog;
import com.hermes.hermestock.repository.RateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateLogService {

    private final RateRepository rateRepository;

    public RateLogService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public List<RateLog> saveAllLog(List<RateLog> rateLogs){
        List<RateLog> savedLog = rateRepository.saveAll(rateLogs);
        return savedLog;
    }


}
