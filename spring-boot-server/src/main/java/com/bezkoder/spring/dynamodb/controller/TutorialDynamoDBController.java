package com.bezkoder.spring.dynamodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

	@Autowired
	TutorialDynamoDBRepository tutorialDynamoDBRepository;

	@GetMapping("/tutorials")
	public ResponseEntity<List<TutorialDynamoDB>> getAllTutorials(@RequestParam(required = false) String title) {
		try {

			List<TutorialDynamoDB> tutorials = tutorialDynamoDBRepository.getTutorialByTitel(title);
			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/tutorials/{id}")
	public ResponseEntity<TutorialDynamoDB> getTutorialById(@PathVariable("id") String id) {

		TutorialDynamoDB tutorial = tutorialDynamoDBRepository.getTutorialById(id);

		if (tutorial != null) {
			return new ResponseEntity<>(tutorial, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

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

	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") String id) {

		try {
			tutorialDynamoDBRepository.delete(String.valueOf(id));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

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

}
