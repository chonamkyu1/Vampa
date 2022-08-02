package com.vam.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class batchTest {
	private static final Logger logger = LoggerFactory.getLogger(batchTest.class);
	
	
//	@Scheduled(cron = "0 * * * * *")
	public void testMethod() throws Exception {
		
		logger.warn("배치 실행 테스트 ..............");
		logger.warn("===================================");
		
	}
}
