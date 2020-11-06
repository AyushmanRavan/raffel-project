package com.rafael.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
//@ConditionalOnProperty(name = "spring.enable.scheduling")
public class RafaelReportGeneratorApplication { // extends SpringBootServletInitializer
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(RafaelReportGeneratorApplication.class);
//	}

	public static void main(String[] args) {
		SpringApplication.run(RafaelReportGeneratorApplication.class, args);
	}

}
