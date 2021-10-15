package ru.task.gibdd.services.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import ru.task.gibdd.exceptions.OverNumberLimit;
import ru.task.gibdd.mappers.NumberMapper;
import ru.task.gibdd.models.NumberParse;
import ru.task.gibdd.models.NumberEntity;
import ru.task.gibdd.models.NumberRs;
import ru.task.gibdd.repository.NumberRepository;
import ru.task.gibdd.services.NumberService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NumberServiceImpl implements NumberService {
	private static final int MAX = 999;
	private static final String DEFAULT_NUMBER = "A000AA" + NumberParse.REGION;
	private static final String LAST_NUMBER = "Х999ХХ" + NumberParse.REGION;
	private static final String ALPHABET = "АВЕКМНОРСТУХ";
	private static final String OVER_NUMBER_LIMIT = "Over number limit";

	private final NumberRepository numberRepository;
	private final NumberMapper numberMapper;

	@Override
	public NumberRs next() throws OverNumberLimit {
		NumberEntity newNumber;

		if (numberRepository.findByValue(LAST_NUMBER).isPresent())
			throw new OverNumberLimit(OVER_NUMBER_LIMIT);

		try {
			NumberEntity lastNumber = numberRepository.findLast().orElseThrow(NotFoundException::new);
			NumberParse parseNewNumber = createNumber(numberMapper.entityToNumber(lastNumber));
			newNumber = numberRepository.save(numberMapper.numberToEntity(parseNewNumber));
		} catch (NotFoundException ex) {
			newNumber = numberRepository.save(NumberEntity.builder().value(DEFAULT_NUMBER).build());
		}

		return numberMapper.numberToRs(newNumber);
	}

	private NumberParse createNumber(NumberParse lastNumber) throws OverNumberLimit {
		String newElement = String.format("%03d", changeFigures(lastNumber.getFigures()));
		lastNumber.setFigures(newElement);

		if (newElement.equals("000")) {
			newElement = changeLast(lastNumber.getLastLetter());
			lastNumber.setLastLetter(newElement);
			if (newElement.equals("AA")) {
				Character ch = changeFirst(lastNumber.getFirstLetter());
				if (ch == lastNumber.getFirstLetter())
					throw new OverNumberLimit(OVER_NUMBER_LIMIT);
			}
		}

		return lastNumber;
	}

	private int changeFigures(String oldNum) {
		int number = Integer.parseInt(oldNum);

		return number < MAX ? ++number : 0;
	}

	private String changeLast(String oldLet) {
		StringBuilder newLet = new StringBuilder(oldLet);

		char letter = oldLet.charAt(1);
		if (letter != ALPHABET.charAt(ALPHABET.length() - 1)) {
			newLet.setCharAt(1, ALPHABET.charAt(ALPHABET.indexOf(letter) + 1));
		} else {
			newLet.setCharAt(1, ALPHABET.charAt(0));
			letter = oldLet.charAt(0);
			if (letter != ALPHABET.charAt(ALPHABET.length() - 1)) {
				newLet.setCharAt(0, ALPHABET.charAt(ALPHABET.indexOf(letter) + 1));
			} else {
				newLet.setCharAt(0, ALPHABET.charAt(0));
			}
		}

		return newLet.toString();
	}

	private Character changeFirst(Character oldLet) {
		Character newChar = oldLet;

		if (oldLet != ALPHABET.charAt(ALPHABET.length() - 1))
			newChar = ALPHABET.charAt(ALPHABET.indexOf(oldLet) + 1);

		return newChar;
	}

	@Override
	public NumberRs random() throws OverNumberLimit {
		NumberParse parseNewNumber = createNumber();

		NumberEntity newNumber = numberRepository.save(numberMapper.numberToEntity(parseNewNumber));

		return numberMapper.numberToRs(newNumber);
	}

	private NumberParse createNumber() throws OverNumberLimit {
		if (numberRepository.findByValue(LAST_NUMBER).isPresent())
			throw new OverNumberLimit(OVER_NUMBER_LIMIT);

		NumberParse newNumber = NumberParse.builder()
				.firstLetter(RandomStringUtils.random(1, ALPHABET).charAt(0))
				.figures(RandomStringUtils.randomNumeric(3))
				.lastLetter(RandomStringUtils.random(2, ALPHABET))
				.build();

		if (numberRepository.findByValue(numberMapper.numberToEntity(newNumber).getValue()).isPresent())
			createNumber();

		return newNumber;
	}

	@Override
	public List<NumberRs> all() {
		return numberMapper.toDtoList(Lists.newArrayList(numberRepository.findAll()));
	}
}
