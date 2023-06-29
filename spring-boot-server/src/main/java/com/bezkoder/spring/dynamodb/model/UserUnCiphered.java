package com.bezkoder.spring.dynamodb.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUnCiphered {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String email;
}
