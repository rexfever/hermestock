package com.hermes.hermestock.repository;

import com.hermes.hermestock.domain.TradeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeLogRepository extends JpaRepository<TradeLog, Integer> {
    TradeLog findByName(String name);
}

