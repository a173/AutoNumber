package ru.task.gibdd.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NumberEnum {
	REGION(" 116 RUS"),
	MAX("999"),
	DEFAULT_NUMBER("A000AA"),
	LAST_NUMBER("Х999ХХ"),
	ALPHABET("АВЕКМНОРСТУХ"),
	OVER_NUMBER_LIMIT("Over number limit");

	private final String value;
}
