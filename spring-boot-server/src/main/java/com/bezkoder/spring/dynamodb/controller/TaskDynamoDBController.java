package com.bezkoder.spring.dynamodb.controller;

import java.util.List;

import com.amazonaws.http.HttpResponse;
import com.bezkoder.spring.dynamodb.model.TaskDynamoDB;
import com.bezkoder.spring.dynamodb.repository.TaskDynamoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class TaskDynamoDBController {
	@Autowired
	TaskDynamoDBRepository taskDynamoDBRepository;

	@GetMapping("/tutorials/users/{hashName}")
	public ResponseEntity<List<TaskDynamoDB>> getAllTasks(@PathVariable("hashName") String nameHash,
														  @RequestParam(required = false) String title,
														  @RequestParam(required = false) String description) {
		if (title != null){
			return new ResponseEntity<>(taskDynamoDBRepository.getTasksByTitle(nameHash, title), HttpStatus.OK);
		}

		if (description != null){
			return new ResponseEntity<>(taskDynamoDBRepository.getTasksByDescription(nameHash, title), HttpStatus.OK);
		}

		return new ResponseEntity<>(taskDynamoDBRepository.getAllTasksFromUser(nameHash), HttpStatus.OK);
	}

	@GetMapping("/tutorials")
	public ResponseEntity<HttpResponse> healthCheckDummy(){
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/tutorials/{nameHash}/{taskId}")
	public ResponseEntity<TaskDynamoDB> getUserTaskById(@PathVariable("nameHash") String nameHash,
														@PathVariable("taskId") String taskId){
		TaskDynamoDB task = taskDynamoDBRepository.getTaskFromUserById(nameHash, taskId);

		if (task != null) {
			return new ResponseEntity<>(task, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}


	@PostMapping("/tutorials/{nameHash}")
	public ResponseEntity<TaskDynamoDB> createUserTask(@PathVariable("nameHash") String nameHash,
													   @RequestBody TaskDynamoDB task){
		try{
			TaskDynamoDB savedTask = taskDynamoDBRepository.saveUserTask(nameHash, task);

			return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@PutMapping("/tutorials/{nameHash}/{taskId}")
	public ResponseEntity<TaskDynamoDB> updateUserTask(@PathVariable("nameHash") String nameHash,
													   @PathVariable("taskId") String taskId,
													   @RequestBody TaskDynamoDB newTask){
		TaskDynamoDB updatedTask = taskDynamoDBRepository.updateUserTask(nameHash, taskId, newTask);

		if (updatedTask == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(updatedTask, HttpStatus.OK);
	}


	@DeleteMapping("/tutorials/{nameHash}/{taskId}")
	public ResponseEntity<TaskDynamoDB> deleteUserTask(@PathVariable("nameHash") String nameHash,
													   @PathVariable("taskId") String taskId){
		try {
			taskDynamoDBRepository.deleteUserTask(nameHash, taskId);

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@GetMapping("/tutorials/{nameHash}/published")
	public ResponseEntity<List<TaskDynamoDB>> findUserTaskByPublished(@PathVariable("nameHash") String nameHash) {
		try {
			List<TaskDynamoDB> tasks = taskDynamoDBRepository.findByPublished(nameHash, true);

			if (tasks.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tasks, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
