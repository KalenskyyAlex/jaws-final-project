package com.bezkoder.spring.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.bezkoder.spring.dynamodb.model.UserDynamoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDynamoDBRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    private DynamoDB dynamoDB;

    public UserDynamoDBRepository(AmazonDynamoDB amazonDynamoDB){
        dynamoDB = new DynamoDB(amazonDynamoDB);
    }

    public UserDynamoDB save(UserDynamoDB user){
        dynamoDBMapper.save(user);
        return user;
    }

    public UserDynamoDB getByNameHash(String nameHash){
        return dynamoDBMapper.load(UserDynamoDB.class, nameHash);
    }
}
