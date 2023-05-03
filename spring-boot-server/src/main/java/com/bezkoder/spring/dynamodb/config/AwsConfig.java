package com.bezkoder.spring.dynamodb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.aws")
public class AwsConfig {

    private String profile;
    private String region;
    private DynamoDB dynamoDB;
    public static class DynamoDB {
        private String endpoint;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }

    public DynamoDB getDynamoDB() {
        return dynamoDB;
    }

    public void setDynamoDB(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
