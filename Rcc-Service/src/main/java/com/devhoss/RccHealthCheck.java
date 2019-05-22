package com.devhoss;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
@Component
public class RccHealthCheck implements HealthIndicator{

	int errorCode  = 0;
	@Override
	public Health health() {
		System.out.println("error code:" + errorCode);
		// TODO Auto-generated method stub
		if(errorCode > 4 && errorCode < 8) {
			errorCode++;
			return Health.down().withDetail("ERROR RCC",errorCode).build();
		}
		errorCode++;
		return Health.up().build();
	}

}
