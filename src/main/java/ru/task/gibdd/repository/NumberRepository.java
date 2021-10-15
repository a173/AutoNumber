package ru.task.gibdd.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.task.gibdd.models.NumberEntity;

import java.util.Optional;
import java.util.Set;

@Repository
public interface NumberRepository extends CrudRepository<NumberEntity, Long> {
	@Query("SELECT * FROM t_number WHERE id = (SELECT MAX(id) FROM t_number)")
	Optional <NumberEntity> findLast();
	Optional <NumberEntity> findByValue(String value);
	Set<NumberEntity> findAll();
}
