package net.franzka.kams.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class KamsEurekaserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(KamsEurekaserverApplication.class, args);
	}

}
