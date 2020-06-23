package com.learnreactivespring.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document   // Equal to @Entity
@AllArgsConstructor
@NoArgsConstructor
public class ItemCapped {

    @Id
    private String id;
    private String description;
    private Double price;

}