package ru.task.gibdd.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import ru.task.gibdd.config.NumberEnum;
import ru.task.gibdd.exceptions.OverNumberLimit;
import ru.task.gibdd.mappers.NumberMapper;
import ru.task.gibdd.models.NumberParse;
import ru.task.gibdd.models.NumberEntity;
import ru.task.gibdd.models.NumberRs;
import ru.task.gibdd.repository.NumberRepository;
import ru.task.gibdd.services.NumberService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class NumberServiceImpl implements NumberService {
	private final NumberRepository numberRepository;
	private final NumberMapper numberMapper;

	@Override
	public Set<NumberRs> all() {
		return numberMapper.toDtoList(numberRepository.findAll());
	}

	@Override
	public Set<NumberRs> allByRegion(String region) {
		return numberMapper.toDtoList(numberRepository.findAllByRegion(region));
	}

	@Override
	@Synchronized
	public NumberRs next() throws OverNumberLimit {
		NumberEntity newNumber;

		try {
			newNumber = numberRepository
					.findLast(NumberEnum.REGION.getValue()).orElseThrow(NotFoundException::new);
		} catch (NotFoundException ex) {
			newNumber = NumberEntity.builder()
					.value(NumberEnum.DEFAULT_NUMBER.getValue())
					.region(NumberEnum.REGION.getValue())
					.build();
		}

		return numberMapper.numberToRs(numberRepository.save(createNextNumber(newNumber)));
	}

	private NumberEntity createNextNumber(NumberEntity lastNumber) throws OverNumberLimit {
		NumberEntity newEntity = lastNumber;

		Set<NumberEntity> setNumber = numberRepository.findAllByRegion(NumberEnum.REGION.getValue());

		if (setNumber.size() == maxCombination())
			throw new OverNumberLimit(NumberEnum.OVER_NUMBER_LIMIT.getValue());

		do {
			newEntity = createNextEntity(newEntity);
		} while (setNumber.contains(newEntity));

		return newEntity;
	}

	private NumberEntity createNextEntity(NumberEntity lastNumber) throws OverNumberLimit {
		NumberParse numberParse = numberMapper.entityToNumber(lastNumber);

		String newFigures = String.format("%03d", changeFigures(numberParse.getFigures()));
		numberParse.setFigures(newFigures);

		if (newFigures.equals("001")) {
			String newLastLetters = changeLast(numberParse.getLastLetter());
			numberParse.setLastLetter(newLastLetters);

			if (newLastLetters.equals("АА")) {
				if (numberParse.getFirstLetter().equals(lastLetter()))
					throw new OverNumberLimit(NumberEnum.OVER_NUMBER_LIMIT.getValue());

				Character newFirstLetter = nextLetter(numberParse.getFirstLetter());
				numberParse.setFirstLetter(newFirstLetter);
			}
		}

		return numberMapper.numberToEntity(numberParse, NumberEnum.REGION);
	}

	private int changeFigures(String oldNum) {
		int number = Integer.parseInt(oldNum);

		return number < Integer.parseInt(NumberEnum.MAX.getValue()) ? ++number : 1;
	}

	private String changeLast(String oldLet) {
		StringBuilder newLet = new StringBuilder(oldLet);

		char letter = oldLet.charAt(1);
		if (letter != lastLetter()) {
			newLet.setCharAt(1, nextLetter(letter));
		} else {
			newLet.setCharAt(1, NumberEnum.ALPHABET.getValue().charAt(0));
			letter = oldLet.charAt(0);
			newLet.setCharAt(0, letter != lastLetter() ?
					nextLetter(letter) : NumberEnum.ALPHABET.getValue().charAt(0));
		}

		return newLet.toString();
	}

	private Character lastLetter() {
		return NumberEnum.ALPHABET.getValue().charAt(NumberEnum.ALPHABET.getValue().length() - 1);
	}

	private Character nextLetter(Character letter) {
		return NumberEnum.ALPHABET.getValue().charAt(NumberEnum.ALPHABET.getValue().indexOf(letter) + 1);
	}

	@Override
	@Synchronized
	public NumberRs random() throws OverNumberLimit {
		return numberMapper.numberToRs(numberRepository.save(createRandomNumber()));
	}

	private NumberEntity createRandomNumber() throws OverNumberLimit {
		NumberEntity newNumber;
		Set<NumberEntity> setNumber = numberRepository.findAllByRegion(NumberEnum.REGION.getValue());

		if (setNumber.size() == maxCombination())
			throw new OverNumberLimit(NumberEnum.OVER_NUMBER_LIMIT.getValue());

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

	private Double maxCombination() {
		return Math.pow(12, 3) * Math.pow(10, 3) - Math.pow(12, 3);
	}
}
