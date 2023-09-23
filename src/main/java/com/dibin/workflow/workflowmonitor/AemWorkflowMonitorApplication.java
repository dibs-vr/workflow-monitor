package com.dibin.workflow.workflowmonitor;

import com.dibin.workflow.workflowmonitor.service.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@SpringBootApplication
@EnableScheduling
@ImportResource("classpath:Spring-Module.xml")
public class AemWorkflowMonitorApplication {

	private Logger logger = LoggerFactory.getLogger(AemWorkflowMonitorApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(AemWorkflowMonitorApplication.class, args);
	}

	@Autowired
	private WorkflowService workflowService;

//	@Scheduled(fixedRate = 24 * 60 * 60 * 1000)
	@Scheduled(fixedRate = 2 * 60 * 1000)
	public void trigger() {
		logger.debug("Start Time is now {}", new Date());

		try {
			workflowService.retrieveAndSaveWorkflow();
			logger.debug("Logger printing doing something");
		} catch(Exception e) {
			e.printStackTrace();
		}
		logger.debug("End Time is now {}" , new Date());
	}
}
