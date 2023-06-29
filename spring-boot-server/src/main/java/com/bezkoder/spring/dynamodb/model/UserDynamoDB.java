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

    @DynamoDBHashKey(attributeName = "nameHash")
    private String userNameHash;

    @DynamoDBAttribute(attributeName = "passwordHash")
    private String userPasswordHash;

    @DynamoDBAttribute(attributeName = "emailHash")
    private String userEmailHash;

    @Override
    public String toString() {
        return "User [nameHash=" + userNameHash + ", pwdHash=" + userPasswordHash + ", emailHash=" + userEmailHash + "]";
    }
}
