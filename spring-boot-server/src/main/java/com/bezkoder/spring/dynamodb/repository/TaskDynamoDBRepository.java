package com.bezkoder.spring.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.bezkoder.spring.dynamodb.model.TaskDynamoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Repository
public class TaskDynamoDBRepository {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    private DynamoDB dynamoDB;

    private List<TaskDynamoDB> getUserTaskBy(String nameHash, String attribute, String value){
        PaginatedScanList<TaskDynamoDB> tasks;

        if (value == null || value.isEmpty()) {
            return getAllTasksFromUser(nameHash);
        }
        else {
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                                                            .withFilterExpression(String.format(":nameHash=%s", nameHash));

            scanExpression.addFilterCondition(attribute,
                    new Condition().withComparisonOperator(ComparisonOperator.CONTAINS)
                            .withAttributeValueList(new AttributeValue().withS(value)));
            tasks = dynamoDBMapper.scan(TaskDynamoDB.class, scanExpression);
        }

        tasks.loadAllResults();
        List<TaskDynamoDB> list = new ArrayList<>(tasks.size());

        Iterator<TaskDynamoDB> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            TaskDynamoDB element = iterator.next();
            list.add(element);
        }
        return list;
    }

    public List<TaskDynamoDB> getTasksByTitle(String nameHash, String title) {
        return getUserTaskBy(nameHash, "title", title);
    }

    public List<TaskDynamoDB> getTasksByDescription(String nameHash, String description) {
        return getUserTaskBy(nameHash, "description", description);
    }

    public List<TaskDynamoDB> findByPublished(String nameHash, boolean b) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                                                        .withFilterExpression(String.format(":nameHash=%s", nameHash));;
        scanExpression.addFilterCondition("published", new Condition().withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withBOOL(b)));
        PaginatedScanList<TaskDynamoDB> tasks = dynamoDBMapper.scan(TaskDynamoDB.class, scanExpression);

        tasks.loadAllResults();
        List<TaskDynamoDB> list = new ArrayList<>(tasks.size());

        Iterator<TaskDynamoDB> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            TaskDynamoDB element = iterator.next();
            list.add(element);
        }

        return list;
    }

    public TaskDynamoDBRepository(AmazonDynamoDB amazonDynamoDB){
        dynamoDB = new DynamoDB(amazonDynamoDB);
    }

    public List<TaskDynamoDB> getAllTasksFromUser(String nameHash){
        DynamoDBQueryExpression<TaskDynamoDB> query = new DynamoDBQueryExpression<>();
        TaskDynamoDB hashKeyValues = new TaskDynamoDB();
        hashKeyValues.setUserNameHash(nameHash);
        query.setHashKeyValues(hashKeyValues);

        return dynamoDBMapper.query(TaskDynamoDB.class, query);
    }

    public TaskDynamoDB getTaskFromUserById(String nameHash, String taskId){
        TaskDynamoDB task = null;
        HashMap<String, AttributeValue> compoundKey = new HashMap<>();

        compoundKey.put(":v1", new AttributeValue().withS(nameHash));
        compoundKey.put(":v2", new AttributeValue().withS(taskId));

        DynamoDBQueryExpression<TaskDynamoDB> queryExp = new DynamoDBQueryExpression<TaskDynamoDB>()
                .withKeyConditionExpression("nameHash = :v1 AND  taskId = :v2")
                .withExpressionAttributeValues(compoundKey);

        List<TaskDynamoDB> tasks = dynamoDBMapper.query(TaskDynamoDB.class, queryExp);

        if (tasks.size() > 0) return tasks.get(0);

        return new TaskDynamoDB();
    }

    public TaskDynamoDB saveUserTask(String nameHash, TaskDynamoDB task){
        TaskDynamoDB savedTask = new TaskDynamoDB(nameHash,
                                                  task.getTitle(),
                                                  task.getDescription(),
                                                  task.isPublished());

        dynamoDBMapper.save(savedTask);

        return savedTask;
    }

    public TaskDynamoDB updateUserTask(String nameHash, String taskId, TaskDynamoDB newTask){
        if (getTaskFromUserById(nameHash, taskId) == null) return null;

        TaskDynamoDB updatedTask = new TaskDynamoDB(nameHash,
                                                    taskId,
                                                    newTask.getTitle(),
                                                    newTask.getDescription(),
                                                    newTask.isPublished(),
                                                    newTask.isDone());

        dynamoDBMapper.save(updatedTask);

        return updatedTask;
    }

    public void deleteUserTask(String nameHash, String taskId){
        TaskDynamoDB task = getTaskFromUserById(nameHash, taskId);
        dynamoDBMapper.delete(task);
    }
}
