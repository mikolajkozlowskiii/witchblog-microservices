package org.common.model;

import jakarta.persistence.Embeddable;


@Embeddable
public record UserInfo(String name, String birthday, String favouriteColor, String martialStatus, String favouriteNumber) {

    // no args constructor is required by JPA
    public UserInfo(){
        this(null,null,null,null, null);
    }
}
