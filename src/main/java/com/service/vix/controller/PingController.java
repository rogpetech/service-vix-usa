package com.service.vix.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/ping")
public class PingController {

	@GetMapping
	public String ping() {
		return "Service Vix up and ruinning.. ";
	}

}
