package com.hermes.hermestock;

import com.hermes.hermestock.domain.Channel;
import com.hermes.hermestock.repository.ChannelRepository;
import com.hermes.hermestock.service.Common;
import com.hermes.hermestock.service.MessageService;
import com.hermes.hermestock.service.ScheduledTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class HermestockApplication {
	@Autowired
	public static void main(String[] args) {
		SpringApplication.run(HermestockApplication.class, args);

	}
}
