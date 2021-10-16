package ru.task.gibdd.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.task.gibdd.models.NumberEntity;

import java.util.Optional;
import java.util.Set;

@Repository
public interface NumberRepository extends CrudRepository<NumberEntity, Long> {
	@Query("SELECT * FROM t_number WHERE id = (SELECT MAX(id) FROM t_number WHERE region = :region)")
	Optional <NumberEntity> findLast(@Param("region") String region);
	Set<NumberEntity> findAll();
	Set<NumberEntity> findAllByRegion(String region);
}
