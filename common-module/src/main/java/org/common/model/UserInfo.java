package org.common.model;

import jakarta.persistence.Embeddable;


@Embeddable
public record UserInfo(String name, String dateOfBirth, String favoriteColor, String relationshipStatus, String favoriteNumber) {
    // no args constructor is required by JPA
    public UserInfo(){
        this(null,null,null,null, null);
    }
}
