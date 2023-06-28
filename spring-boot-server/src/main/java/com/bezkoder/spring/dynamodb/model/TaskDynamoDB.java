package com.bezkoder.spring.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;

//@DynamoDBTable(tableName = "tasks")
@AllArgsConstructor
@Data
public class TaskDynamoDB {
    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = "nameHash")
    private String userNameHash;

    @DynamoDBRangeKey
    @DynamoDBAutoGeneratedKey
    @DynamoDBAttribute(attributeName = "taskId")
    private String taskId;

    @DynamoDBAttribute(attributeName = "title")
    private String title;

    @DynamoDBAttribute(attributeName = "description")
    private String description;

    @DynamoDBAttribute(attributeName = "published")
    private boolean published;

    @DynamoDBAttribute(attributeName = "done")
    private boolean done;

    public TaskDynamoDB(String userNameHash, String title, String description, boolean published) {
        this.userNameHash = userNameHash;
        this.title = title;
        this.description = description;
        this.published = published;
        done = false;
    }
}
