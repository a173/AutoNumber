package ru.task.gibdd.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NumberEnum {
	DEFAULT_NUMBER("A000AA"),
	REGION("716"),
	RUS(" RUS"),
	MAX("999"),
	ALPHABET("АВЕКМНОРСТУХ"),
	OVER_NUMBER_LIMIT("Over number limit for region " + REGION.getValue());

	private final String value;
}
