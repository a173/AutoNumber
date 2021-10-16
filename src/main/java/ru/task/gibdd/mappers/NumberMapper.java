package ru.task.gibdd.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.task.gibdd.config.NumberEnum;
import ru.task.gibdd.models.NumberParse;
import ru.task.gibdd.models.NumberEntity;
import ru.task.gibdd.models.NumberRs;

import java.util.Set;

@Mapper
public interface NumberMapper {
	@Mapping(target = "firstLetter", expression = "java(entity.getValue().charAt(0))")
	@Mapping(target = "figures", expression = "java(entity.getValue().substring(1, 4))")
	@Mapping(target = "lastLetter", expression = "java(entity.getValue().substring(4))")
	NumberParse entityToNumber(NumberEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "value", expression = "java(" +
									"number.getFirstLetter() + " +
									"number.getFigures() + " +
									"number.getLastLetter())")
	@Mapping(target = "region", expression = "java(region.getValue())")
	NumberEntity numberToEntity(NumberParse number, NumberEnum region);

	@Mapping(target = "value", expression = "java(number.getValue() + ' ' + " +
									"NumberEnum.REGION.getValue() + " +
									"NumberEnum.RUS.getValue())")
	NumberRs numberToRs(NumberEntity number);
	Set<NumberRs> toDtoList(Set<NumberEntity> numbers);
}
