package org.common.model;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;


@Embeddable
public record UserInfo(String name, LocalDate birthday, String favouriteColor, String martialStatus, String favouriteNumber) {

    // no args constructor is required by JPA
    public UserInfo(){
        this(null,null,null,null, null);
    }
}
