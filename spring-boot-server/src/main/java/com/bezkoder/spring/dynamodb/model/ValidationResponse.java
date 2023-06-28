package com.bezkoder.spring.dynamodb.model;

public enum ValidationResponse {
    UserAlreadyExists,
    NoUserWithSuchEmail,
    InvalidPassword,
    Success
}
