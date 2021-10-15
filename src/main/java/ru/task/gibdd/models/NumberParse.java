package ru.task.gibdd.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NumberParse {
	public static final String REGION = " 116 RUS";

	Character firstLetter;
	String figures;
	String lastLetter;
}
