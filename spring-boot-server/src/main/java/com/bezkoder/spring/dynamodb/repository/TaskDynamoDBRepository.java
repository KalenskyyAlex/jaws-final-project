package com.bezkoder.spring.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.bezkoder.spring.dynamodb.model.TaskDynamoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskDynamoDBRepository {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    private DynamoDB dynamoDB;

    public TaskDynamoDBRepository(AmazonDynamoDB amazonDynamoDB){
        dynamoDB = new DynamoDB(amazonDynamoDB);
    }

    public List<TaskDynamoDB> getAllTasksFromUser(String nameHash){
        DynamoDBQueryExpression<TaskDynamoDB> query = new DynamoDBQueryExpression<>();
        TaskDynamoDB hashKeyValues = new TaskDynamoDB();
        hashKeyValues.setUserNameHash(nameHash);
        query.setHashKeyValues(hashKeyValues);

        List<TaskDynamoDB> tasks = dynamoDBMapper.query(TaskDynamoDB.class, query);
        return tasks;
    }

    public TaskDynamoDB getTaskFromUserById(String nameHash, String taskId){
        String condition = String.format(":nameHash=%s and :taskId=%s", nameHash, taskId);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(condition);

        List<TaskDynamoDB> tasks = dynamoDBMapper.scan(TaskDynamoDB.class, scanExpression);

        if (tasks.size() != 1) return null;

        return tasks.get(0);
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
