package com.ceti.wholesale.service.impl.v2;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ceti.wholesale.service.v2.WarehouseCommunicationService;

@Service
public class WarehouseCommunicationServiceImpl implements WarehouseCommunicationService {

	@Autowired
	RestTemplate restTemplate;
	
	@Async
	@Override
	public void updateTruckCommand(Map<String, Object> data) {
		// Header
		HttpHeaders headers = new HttpHeaders();
		// Body
		String commandReferenceId = (String) data.get("id");
		String weight1 = (String) data.get("weight1");
		String weight2 = (String) data.get("weight2");

		Map<String, Object> body = new HashMap<String, Object>();
		body.put("weight1", weight1);
		body.put("weight2", weight2);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(body, headers);

		restTemplate.exchange("http://warehouse-service/v2/truck-import-export-histories/" + commandReferenceId, HttpMethod.PATCH, 
				entity, Object.class);
	}

}
