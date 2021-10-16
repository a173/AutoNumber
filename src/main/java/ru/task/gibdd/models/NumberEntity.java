package ru.task.gibdd.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Value
@Builder
@AllArgsConstructor
@Table("t_number")
public class NumberEntity {
	@Id
	Long id;
	String value;
	String region;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NumberEntity that = (NumberEntity) o;
		return value.equals(that.value) && region.equals(that.region);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, region);
	}
}
