package com.dibin.workflow.workflowmonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class AemWorkflowMonitorApplication {

	private Logger logger = LoggerFactory.getLogger(AemWorkflowMonitorApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(AemWorkflowMonitorApplication.class, args);
	}

	@Scheduled(fixedRate = 5000)
	public void trigger() {
		logger.debug("Start Time is now {}", new Date());

		try {
			System.out.println("doing something");
			logger.debug("Logger printing doing something");
		} catch(Exception e) {
			e.printStackTrace();
		}
		logger.debug("End Time is now {}" , new Date());
	}
}
