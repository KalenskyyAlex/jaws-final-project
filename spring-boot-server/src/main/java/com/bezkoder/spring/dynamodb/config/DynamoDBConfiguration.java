package com.bezkoder.spring.dynamodb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Configuration
public class DynamoDBConfiguration {

	@Autowired
	private AwsConfig awsConfig;

	@Bean
	public DynamoDBMapper dynamoDBMapper() {
		return new DynamoDBMapper(buildAmazonDynamoDB());
	}

	@Bean
	public AmazonDynamoDB buildAmazonDynamoDB() {
		String endpoint = awsConfig.getDynamoDB().getEndpoint();
		String region = awsConfig.getRegion();
		String profile = awsConfig.getProfile();
		if (profile != null) {
			System.setProperty("aws.profile", profile);
		}

		AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
		AmazonDynamoDBClientBuilder clientBuilder = AmazonDynamoDBClientBuilder.standard();
		clientBuilder.setEndpointConfiguration(config);
		AmazonDynamoDB client = clientBuilder.build();
		return client;
	}

}
