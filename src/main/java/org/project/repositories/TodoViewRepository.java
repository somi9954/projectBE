package org.project.repositories;

import org.project.entities.TodoView;
import org.project.entities.TodoViewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TodoViewRepository extends JpaRepository<TodoView, TodoViewId> , QuerydslPredicateExecutor<TodoView> {
}
