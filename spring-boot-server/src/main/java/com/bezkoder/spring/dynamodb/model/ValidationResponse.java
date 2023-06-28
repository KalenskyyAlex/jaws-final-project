package com.bezkoder.spring.dynamodb.model;

public enum ValidationResponse {
    InvalidData,
    UserAlreadyExists,
    NoSuchUser,
    InvalidPassword,
    Success
}
