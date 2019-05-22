package com.devhoss.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.devhoss.model.Cliente;
import com.devhoss.model.ClienteDetalle;
import com.devhoss.model.Historico;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ClienteServiceController {

	Logger logger = LoggerFactory.getLogger(ClienteServiceController.class);

	@Autowired
	RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "fallback_getClientes")
	@RequestMapping(value = "/clientes/{id}", method = RequestMethod.GET)
	public ClienteDetalle getCLientes(@PathVariable int id)
	{
		logger.info("REQUEST:" + id);

		Cliente cliente = new Cliente(id,"Hossmell velasco",30);

		ResponseEntity<List<Historico>> response = restTemplate.exchange("http://rcc-service/historico/{id}",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Historico>>() {}, id);
		List<Historico> historico = response.getBody();
		ClienteDetalle clienteDetalle = new ClienteDetalle(cliente,historico);

		logger.info("RESPONSE: " + clienteDetalle);

		return clienteDetalle;
	}

	@SuppressWarnings("unused")
	private ClienteDetalle fallback_getClientes(@PathVariable int id) {
		logger.info("fallo:");
		
		Cliente cliente = new Cliente(id,"Hossmell velasco fallback",0);
		List<Historico> historico = new ArrayList<Historico>();
		historico.add(new Historico("0", "S/T", 0.0));
		ClienteDetalle clienteDetalle = new ClienteDetalle(cliente,historico);

		logger.info("RESPONSE FALL: " + clienteDetalle);

		return clienteDetalle;
		
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	
	
}
