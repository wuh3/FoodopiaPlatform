package com.foodopia.app;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
public class MyFoodopiaApplicationTest {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private Environment env;

	@Test
	void contextLoads() {
		assertNotNull(mongoTemplate, "MongoTemplate should not be null, indicating DB connection is configured.");

		mongoTemplate.getDb().listCollectionNames().forEach(System.out::println);
	}

	@Test
	void printMongoUri() {
		String uri = env.getProperty("spring.data.mongodb.uri");
		System.out.println("Mongo URI: " + uri);
		assertNotNull(uri, "Mongo URI should not be null");
	}
}