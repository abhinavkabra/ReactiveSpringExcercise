package com.learnreactivespring.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document   // Equal to @Entity
@AllArgsConstructor
@NoArgsConstructor
public class Item {
	
	@Id
	private String id;
	private String description;
	private Double price;
	
}
