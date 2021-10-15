package ru.task.gibdd.services;

import ru.task.gibdd.exceptions.OverNumberLimit;
import ru.task.gibdd.models.NumberRs;

import java.util.List;

public interface NumberService {
	List<NumberRs> all();
	NumberRs next() throws OverNumberLimit;
	NumberRs random() throws OverNumberLimit;
}
