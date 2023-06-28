package com.bezkoder.spring.dynamodb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class UserUnCiphered {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String password;
}
