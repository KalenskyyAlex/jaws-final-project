package com.bezkoder.spring.dynamodb.controller;

import com.bezkoder.spring.dynamodb.model.UserDynamoDB;
import com.bezkoder.spring.dynamodb.model.ValidationResponse;
import com.bezkoder.spring.dynamodb.repository.UserDynamoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserDynamoDBRepository userDB;

    @PostMapping("/login")
    public ResponseEntity<ValidationResponse> validateLogin(@Valid @NotNull @RequestBody UserDynamoDB user) {
        UserDynamoDB savedUser = userDB.getByEmailHash(user.getUserEmailHash());

        if (savedUser == null){
            return new ResponseEntity<>(ValidationResponse.NoUserWithSuchEmail, HttpStatus.OK);
        }

        if (savedUser.getUserPasswordHash().equals(user.getUserPasswordHash())){
            return new ResponseEntity<>(ValidationResponse.Success, HttpStatus.OK);
        }

        return new ResponseEntity<>(ValidationResponse.InvalidPassword, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<ValidationResponse> validateSignUp(@Valid @NotNull @RequestBody UserDynamoDB user){
        UserDynamoDB savedUser = userDB.getByEmailHash(user.getUserEmailHash());

        if (savedUser != null){
            return new ResponseEntity<>(ValidationResponse.UserAlreadyExists, HttpStatus.OK);
        }

        userDB.save(user);

        return new ResponseEntity<>(ValidationResponse.Success, HttpStatus.OK);
    }
}
