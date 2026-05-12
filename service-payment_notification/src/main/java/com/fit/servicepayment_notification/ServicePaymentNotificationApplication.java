package com.fit.servicepayment_notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ServicePaymentNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicePaymentNotificationApplication.class, args);
		log.info("Payment & Notification Service started successfully on port 8084");
	}

}
