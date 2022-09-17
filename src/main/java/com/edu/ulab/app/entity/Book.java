package com.edu.ulab.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class Book extends Entity {
    @ToString.Exclude
    private User user;
    private String title;
    private String author;
    private long pageCount;
}
