package org.project.repositories;

import org.project.entities.TodoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;



public interface TodoDataRepository extends JpaRepository<TodoData, Long>, QuerydslPredicateExecutor<TodoData> {

}
