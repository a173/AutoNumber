package ru.task.gibdd.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@AllArgsConstructor
@Table("t_number")
public class NumberEntity {
	@Id
	Long id;
	String value;
}
