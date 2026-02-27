package com.jonathastassi.api_courses_ti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @OpenAPIDefinition(info = @Info(title = "API Courses", description = "API
// responsável pela gestão de cursos. Atividade proposta na formação Java da
// Rocketseat", version = "1"))
// @SecurityScheme(name = "bearerAuth", scheme = "bearer", bearerFormat = "JWT",
// type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class ApiCoursesTiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCoursesTiApplication.class, args);
	}

}
