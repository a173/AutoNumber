package ru.task.gibdd.services.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.task.gibdd.config.NumberEnum;
import ru.task.gibdd.exceptions.OverNumberLimit;
import ru.task.gibdd.mappers.NumberMapper;
import ru.task.gibdd.models.NumberParse;
import ru.task.gibdd.models.NumberEntity;
import ru.task.gibdd.models.NumberRs;
import ru.task.gibdd.repository.NumberRepository;
import ru.task.gibdd.services.NumberService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NumberServiceImpl implements NumberService {
	private final NumberRepository numberRepository;
	private final NumberMapper numberMapper;

	@Override
	public List<NumberRs> all() {
		return numberMapper.toDtoList(Lists.newArrayList(numberRepository.findAll()));
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public NumberRs next() throws OverNumberLimit {
		if (checkLastNumber())
			throw new OverNumberLimit(NumberEnum.OVER_NUMBER_LIMIT.getValue());

		NumberEntity newNumber;
		try {
			NumberEntity lastNumber = numberRepository.findLast().orElseThrow(NotFoundException::new);
			newNumber = numberRepository.save(createNextNumber(lastNumber));
		} catch (NotFoundException ex) {
			newNumber = numberRepository
					.save(NumberEntity.builder().value(NumberEnum.DEFAULT_NUMBER.getValue()).build());
		}

		return numberMapper.numberToRs(newNumber);
	}

	private NumberEntity createNextNumber(NumberEntity lastNumber) throws OverNumberLimit {
		Set<NumberEntity> setNumber = numberRepository.findAll();

		NumberEntity newEntity = lastNumber;
		do {
			newEntity = createNextEntity(newEntity);
		} while (setNumber.contains(newEntity));

		return newEntity;
	}

	private NumberEntity createNextEntity(NumberEntity lastNumber) throws OverNumberLimit {
		NumberParse numberParse = numberMapper.entityToNumber(lastNumber);

		String newElement = String.format("%03d", changeFigures(numberParse.getFigures()));
		numberParse.setFigures(newElement);

		if (newElement.equals("000")) {
			newElement = changeLast(numberParse.getLastLetter());
			numberParse.setLastLetter(newElement);

			if (newElement.equals("AA")) {
				Character ch = changeFirst(numberParse.getFirstLetter());
				if (ch == numberParse.getFirstLetter())
					throw new OverNumberLimit(NumberEnum.OVER_NUMBER_LIMIT.getValue());
				numberParse.setFirstLetter(ch);
			}
		}

		return numberMapper.numberToEntity(numberParse, NumberEnum.REGION);
	}

	private int changeFigures(String oldNum) {
		int number = Integer.parseInt(oldNum);

		return number < Integer.parseInt(NumberEnum.MAX.getValue()) ? ++number : 0;
	}

	private String changeLast(String oldLet) {
		StringBuilder newLet = new StringBuilder(oldLet);

		char letter = oldLet.charAt(1);
		if (letter != lastLetter()) {
			newLet.setCharAt(1, nextLetter(letter));
		} else {
			newLet.setCharAt(1, NumberEnum.ALPHABET.getValue().charAt(0));
			letter = oldLet.charAt(0);
			if (letter != lastLetter())
				newLet.setCharAt(0, nextLetter(letter));
			else
				newLet.setCharAt(0, NumberEnum.ALPHABET.getValue().charAt(0));
		}

		return newLet.toString();
	}

	private Character changeFirst(Character oldLet) {
		Character newChar = oldLet;

		if (oldLet != lastLetter())
			newChar = nextLetter(oldLet);

		return newChar;
	}

	private char lastLetter() {
		return NumberEnum.ALPHABET.getValue().charAt(NumberEnum.ALPHABET.getValue().length() - 1);
	}

	private char nextLetter(Character letter) {
		return NumberEnum.ALPHABET.getValue().charAt(NumberEnum.ALPHABET.getValue().indexOf(letter) + 1);
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public NumberRs random() throws OverNumberLimit {
		if (checkLastNumber())
			throw new OverNumberLimit(NumberEnum.OVER_NUMBER_LIMIT.getValue());

		NumberEntity newNumber = numberRepository.save(createRandomNumber());

		return numberMapper.numberToRs(newNumber);
	}

	private NumberEntity createRandomNumber() {
		Set<NumberEntity> setNumber = numberRepository.findAll();

		NumberEntity newNumber;
		do {
			newNumber = createRandomEntity();
		} while (setNumber.contains(newNumber));

		return newNumber;
	}

	private NumberEntity createRandomEntity(){
		return numberMapper.numberToEntity(NumberParse.builder()
				.firstLetter(RandomStringUtils.random(1, NumberEnum.ALPHABET.getValue()).charAt(0))
				.figures(RandomStringUtils.randomNumeric(3))
				.lastLetter(RandomStringUtils.random(2, NumberEnum.ALPHABET.getValue()))
				.build(), NumberEnum.REGION);
	}

	private boolean checkLastNumber() {
		return numberRepository.findByValue(NumberEnum.LAST_NUMBER.getValue()).isPresent();
	}
}
