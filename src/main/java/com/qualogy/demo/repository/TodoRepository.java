package com.qualogy.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qualogy.demo.entity.Todo;

/**
 * Repository to update database for TodoController
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {
	Todo findOneByDescription(String description);
}
