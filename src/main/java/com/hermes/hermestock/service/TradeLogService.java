package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.TradeLog;
import com.hermes.hermestock.repository.TradeLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeLogService {

    private final TradeLogRepository tradeLogRepository;

    public TradeLogService(TradeLogRepository tradeLogRepository) {
        this.tradeLogRepository = tradeLogRepository;
    }

    public List<TradeLog> saveAllLog(List<TradeLog> tradeLogs){
        List<TradeLog> tradeLogs1 = tradeLogRepository.saveAll(tradeLogs);
        return tradeLogs1;
    }
}
