package com.bezkoder.spring.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDynamoDB {

    @DynamoDBHashKey
    private String userEmailHash;

    @DynamoDBAttribute
    private String userPasswordHash;

    @Override
    public String toString() {
        return "User [emailHash=" + userEmailHash + ", pwdHash=" + userPasswordHash + "]";
    }

}
