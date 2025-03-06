package com.appsdeveloperbolg.photoapp.api.users;

import com.appsdeveloperbolg.photoapp.api.users.shared.FeignErrorDecoder;
import feign.Logger;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class PhotoAppApiUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppApiUsersApplication.class, args);
	}

	@Autowired
	Environment environment;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public HttpExchangeRepository httpExchangeRepository(){
		return new InMemoryHttpExchangeRepository();
	}

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(RestTemplateBuilder builder){
		return new RestTemplate();
	}

	@Bean
	@Profile("production")
	Logger.Level feignLoggerProducer(){
		return Logger.Level.NONE;
	}

	@Bean
	@Profile("!production")
	Logger.Level feignDefaultLoggerProducer(){
		return Logger.Level.FULL;
	}

	@Bean
	@Profile("production")
	public String createProductionBean(){
		System.out.println("Production bean created. Environment: " + environment.getProperty("myapplication.environment"));
		return "Production bean";
	}

	@Bean
	@Profile("default")
	public String createDevelopmentBean(){
		System.out.println("Development bean created. Environment: " + environment.getProperty("myapplication.environment"));
		return "Development bean";
	}

	@Bean
	@Profile("!production")
	public String createNotProductionBean(){
		System.out.println("Not production bean created. Environment: " + environment.getProperty("myapplication.environment"));
		return "Not production bean";
	}

//	@Bean
//	public FeignErrorDecoder getFeignErrorDecoder(){
//		return new FeignErrorDecoder();
//	}
}
