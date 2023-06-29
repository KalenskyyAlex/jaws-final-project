package com.bezkoder.spring.dynamodb.controller;

import java.util.List;

import com.bezkoder.spring.dynamodb.model.TaskDynamoDB;
import com.bezkoder.spring.dynamodb.repository.TaskDynamoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.dynamodb.model.TutorialDynamoDB;
import com.bezkoder.spring.dynamodb.repository.TutorialDynamoDBRepository;

@RestController
@RequestMapping("/api")
public class TutorialDynamoDBController {

	// legacy
	@Autowired
	TutorialDynamoDBRepository tutorialDynamoDBRepository;

	@Autowired
	TaskDynamoDBRepository taskDynamoDBRepository;

	//legacy
	@GetMapping("/tutorials")
	public ResponseEntity<List<TutorialDynamoDB>> getAllTutorials(@RequestParam(required = false) String title) {
		try {

			List<TutorialDynamoDB> tutorials = tutorialDynamoDBRepository.getTutorialByTitle(title);
			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/tutorials/users/{hashName}")
	public ResponseEntity<List<TaskDynamoDB>> getAllTasks(@PathVariable("hashName") String nameHash) {
//		if (!title.isEmpty()){
//			return new ResponseEntity<>(taskDynamoDBRepository.getTasksByTitle(nameHash, title), HttpStatus.OK);
//		}
//		else if (!description.isEmpty()){
//			return new ResponseEntity<>(taskDynamoDBRepository.getTasksByDescription(nameHash, title), HttpStatus.OK);
//		}

		return new ResponseEntity<>(taskDynamoDBRepository.getAllTasksFromUser(nameHash), HttpStatus.OK);
	}

	//legacy
	@GetMapping("/tutorials/{id}")
	public ResponseEntity<TutorialDynamoDB> getTutorialById(@PathVariable("id") String id) {

		TutorialDynamoDB tutorial = tutorialDynamoDBRepository.getTutorialById(id);

		if (tutorial != null) {
			return new ResponseEntity<>(tutorial, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
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

	//legacy
	@PostMapping("/tutorials")
	public ResponseEntity<TutorialDynamoDB> createTutorial(@RequestBody TutorialDynamoDB tutorial) {

		try {
			TutorialDynamoDB _tutorial = tutorialDynamoDBRepository
					.save(new TutorialDynamoDB(tutorial.getTitle(), tutorial.getDescription(), false));
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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

	//legacy
	@PutMapping("/tutorials/{id}")
	public ResponseEntity<TutorialDynamoDB> updateTutorial(@PathVariable("id") String id,
			@RequestBody TutorialDynamoDB tutorial) {

		tutorial.setId(id);
		TutorialDynamoDB updatedTutorial = tutorialDynamoDBRepository.update(id, tutorial);

		if (updatedTutorial != null) {
			return new ResponseEntity<>(updatedTutorial, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

	//legacy
	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") String id) {

		try {
			tutorialDynamoDBRepository.delete(String.valueOf(id));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

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

	//legacy
	@GetMapping("/tutorials/published")
	public ResponseEntity<List<TutorialDynamoDB>> findByPublished() {
		try {
			List<TutorialDynamoDB> tutorials = tutorialDynamoDBRepository.findByPublished(true);

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
