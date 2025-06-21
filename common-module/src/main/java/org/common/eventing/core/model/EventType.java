package org.common.eventing.core.model;

import lombok.Getter;

@Getter
public abstract class EventType {
    private final String type;

    protected EventType(String type) {
        this.type = type;
    }
}
