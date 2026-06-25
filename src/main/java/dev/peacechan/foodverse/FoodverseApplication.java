package dev.peacechan.foodverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FoodverseApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodverseApplication.class, args);
	}

}
