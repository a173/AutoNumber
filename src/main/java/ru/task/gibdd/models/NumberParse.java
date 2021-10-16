package ru.task.gibdd.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NumberParse {
	Character firstLetter;
	String figures;
	String lastLetter;
	String region;
}
