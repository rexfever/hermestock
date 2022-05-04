package com.hermes.hermestock.repository;

import com.hermes.hermestock.domain.RateLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateLogRepository extends JpaRepository<RateLog, Integer> {
}
