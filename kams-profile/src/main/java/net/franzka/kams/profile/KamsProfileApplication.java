package net.franzka.kams.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class KamsProfileApplication {

	public static void main(String[] args) {
		SpringApplication.run(KamsProfileApplication.class, args);
	}

}
