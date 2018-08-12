package com.qualogy.demo.controller;

import com.qualogy.demo.entity.Todo;
import com.qualogy.demo.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller to handle all the requests coming for tasks
 */
@RestController
@RequestMapping("todos") public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);
    @Autowired private TodoRepository todoRepository;

    /**
     * Request Mapping to get all the tasks from database
     * @return List<Todo>
     */
    @GetMapping public List<Todo> getAllTodos() {
        logger.info("Getting All todos");
        Sort sortByCreatedAtDesc = new Sort(Sort.Direction.DESC, "createdDate");
        return (List<Todo>) this.todoRepository.findAll(sortByCreatedAtDesc);
    }

    /**
     * Request Mapping to get one particular task from database
     * @param id of the task to fetch from database
     * @return
     */
    @GetMapping("/{id}") public ResponseEntity<Todo> getTodo(@PathVariable("id") final long id) {
        return todoRepository.findById(id).map(todo -> ResponseEntity.ok().body(todo))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Request mapping to store new task to database.
     * Will also validate the data recied based on the checks implemented on the entity Todo
     * @param todo
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping public void addTodo(@Valid @RequestBody final Todo todo) {
        this.todoRepository.save(todo);
        logger.info("Creating a new task");
    }

    /**
     * Request mapping to update an existing task in the database
     * @param id
     * @param todo task to be updated
     * @return  ResponseEntity<Todo>
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable("id") Long id,@Valid @RequestBody final Todo todo) {
        logger.info("updating exisiting task");
        return todoRepository.findById(id).map(todoData -> {
            todoData.setDescription(todo.getDescription());
            todoData.setCompleted(todo.isCompleted());
            Todo updatedTodo = todoRepository.save(todoData);
            return ResponseEntity.ok().body(updatedTodo);
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Request mapping to delete specify task from the database
     * @param id
     * @return
     */
    @DeleteMapping("/{id}") public ResponseEntity<?> deleteTodo(@PathVariable("id") final Long id) {
        return todoRepository.findById(id).map(todo -> {
            todoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Request mapping to delete all tasks from database
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping public void deleteAllTodos() {
        logger.info("Deleting All tasks");
        todoRepository.deleteAll();
    }
}
