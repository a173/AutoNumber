package ru.task.gibdd.services;

import ru.task.gibdd.exceptions.OverNumberLimit;
import ru.task.gibdd.models.NumberRs;

import java.util.Set;

public interface NumberService {
	Set<NumberRs> all();
	Set<NumberRs> allByRegion(String region);
	NumberRs next() throws OverNumberLimit;
	NumberRs random() throws OverNumberLimit;
}
