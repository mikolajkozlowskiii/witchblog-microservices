package org.common.model;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;


@Embeddable
public record UserInfo(String name, LocalDate dateOfBirth, String favoriteColor, String relationshipStatus, String favoriteNumber) {

    // no args constructor is required by JPA
    public UserInfo(){
        this(null,null,null,null, null);
    }
}
