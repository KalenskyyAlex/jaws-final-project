package com.bezkoder.spring.dynamodb.controller;

import com.bezkoder.spring.dynamodb.model.UserDynamoDB;
import com.bezkoder.spring.dynamodb.model.UserUnCiphered;
import com.bezkoder.spring.dynamodb.model.ValidationResponse;
import com.bezkoder.spring.dynamodb.repository.UserDynamoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserDynamoDBRepository userDB;

    private String getSHA256Hash(String plaintext){
        try {
            String hash;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] result = md.digest(plaintext.getBytes());
            hash = String.format("%032X", new BigInteger(1, result));

            return hash;
        }
        catch (Exception e) {
            return "";
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Integer> validateLogin(@Valid @NotNull @RequestBody UserUnCiphered user) {
        String nameHash = getSHA256Hash(user.getName());
        String passwordHash = getSHA256Hash(user.getPassword());

        if (nameHash == null || passwordHash == null) {
            return new ResponseEntity<>(ValidationResponse.InvalidData.ordinal(), HttpStatus.OK);
        }

        UserDynamoDB savedUser = userDB.getByNameHash(nameHash);

        if (savedUser == null){
            return new ResponseEntity<>(ValidationResponse.NoSuchUser.ordinal(), HttpStatus.OK);
        }

        if (savedUser.getUserPasswordHash().equals(passwordHash)){
            return new ResponseEntity<>(ValidationResponse.Success.ordinal(), HttpStatus.OK);
        }

        return new ResponseEntity<>(ValidationResponse.InvalidPassword.ordinal(), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<Integer> validateSignUp(@Valid @NotNull @RequestBody UserUnCiphered user){
        String nameHash = getSHA256Hash(user.getName());
        String passwordHash = getSHA256Hash(user.getPassword());
        String emailHash = getSHA256Hash(user.getEmail());

        if (nameHash == null || passwordHash == null || emailHash == null) {
            return new ResponseEntity<>(ValidationResponse.InvalidData.ordinal(), HttpStatus.OK);
        }

        UserDynamoDB savedUser = userDB.getByNameHash(nameHash);

        if (savedUser != null){
            return new ResponseEntity<>(ValidationResponse.UserAlreadyExists.ordinal(), HttpStatus.OK);
        }

        userDB.save(new UserDynamoDB(nameHash, passwordHash, emailHash));

        return new ResponseEntity<>(ValidationResponse.Success.ordinal(), HttpStatus.CREATED);
    }
}
