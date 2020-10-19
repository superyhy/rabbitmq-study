package com.yhy.consumer.entity;

import java.io.Serializable;

public class Message implements Serializable {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
