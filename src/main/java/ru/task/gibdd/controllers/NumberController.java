package ru.task.gibdd.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.task.gibdd.exceptions.OverNumberLimit;
import ru.task.gibdd.models.NumberRs;
import ru.task.gibdd.services.NumberService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class NumberController {

	private final NumberService numberService;

	@GetMapping("/next")
	public NumberRs next() throws OverNumberLimit {
		return numberService.next();
	}

	@GetMapping("/random")
	public NumberRs random() throws OverNumberLimit {
		return numberService.random();
	}

	@GetMapping("/all")
	public Set<NumberRs> all() {
		return numberService.all();
	}
	@GetMapping("/all/{region}")
	public Set<NumberRs> all(@PathVariable String region) {
		return numberService.allByRegion(region);
	}
}
