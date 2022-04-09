package com.hermes.hermestock.repository;

import com.hermes.hermestock.domain.TradeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface HermesRepository extends JpaRepository<TradeLog, Integer> {

}
