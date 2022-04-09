package com.hermes.hermestock.repository;

import com.hermes.hermestock.domain.RateLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<RateLog, Integer> {
}
