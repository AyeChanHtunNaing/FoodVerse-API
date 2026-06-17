package dev.peacechan.foodverse.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI foodverseOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("FoodVerse API")
						.description("API documentation for the FoodVerse backend.")
						.version("v1")
						.contact(new Contact()
								.name("Peace Chan")
								.email("contact@peacechan.dev"))
						.license(new License()
								.name("Proprietary")));
	}

}
