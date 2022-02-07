package com.ceti.wholesale;

import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.repository.FactoryRepository;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableEurekaClient
public class WholeSaleApplication {
	@Autowired
	private FactoryRepository factoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(WholeSaleApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
 
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		restTemplate.setRequestFactory(requestFactory);
		return restTemplate;
	}
	@Bean
	public Map<String,String> getFactoryMap(){
		Map<String,String> factoryMap=new HashedMap<String, String>();
		List<Factory> factories=factoryRepository.findAll();
		for (Factory factory : factories) {
			factoryMap.put(factory.getId(), factory.getCode());
		}
		System.out.println("data");
		for (String key: factoryMap.keySet()){
			System.out.println(key+"-"+factoryMap.get(key));
		}
		return factoryMap;
	}
}
