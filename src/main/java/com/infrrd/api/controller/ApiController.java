package com.infrrd.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infrrd.api.service.DataPrcoseeService;
import com.infrrd.api.service.DataPrcoseeServiceImpl;

@RestController
@RequestMapping("/api")
public class ApiController {
	
	Logger logger = LoggerFactory.getLogger(ApiController.class);

	@Autowired
	private DataPrcoseeService dataProcessService;


	@PostMapping("/startprocess")
	public Map<String, Integer> startProcessing() {
		logger.debug("Start process data request is called..");
		// Create one thread to handle each request
		ExecutorService exe = Executors.newSingleThreadExecutor();
		DataPrcoseeServiceImpl dsi = new DataPrcoseeServiceImpl();
		Future<Map<String, Integer>> submit = exe.submit(dsi);
		try {
			return submit.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			logger.error("Previous request has been interuupted by current request");
			Map<String, Integer> errormap = new HashMap<>();
			errormap.put("This request got interrupted due to recent request for same endpoint", -1);
			return errormap;
		}
	}

	@PostMapping("/endprocess")
	public String endProcessing() {
		logger.debug("End data process request is called..");
		return dataProcessService.endProcess();
	}

}
