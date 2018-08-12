package com.qualogy.demo.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.qualogy.demo.repository.TodoRepository;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.qualogy.demo.entity.Todo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TodoControllerTests {
    private MockMvc mockMvc;
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private ObjectMapper mapper;
    @MockBean TodoRepository todoRepository;

    @Before public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Test public void _findAllTodos() throws Exception {
        Sort sortByCreatedAtDesc = new Sort(Sort.Direction.DESC, "createdDate");
        List<Todo> allTodos = new ArrayList<Todo>();
        allTodos.add(new Todo());
        allTodos.add(new Todo());
        Mockito.when(todoRepository.findAll(sortByCreatedAtDesc)).thenReturn(allTodos);
        this.mockMvc.perform(get("/todos")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
    }

    @Test public void findOneTodo() throws Exception {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setDescription("First Todo");
        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.ofNullable(todo));
        this.mockMvc.perform(get("/todos/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.description", equalToIgnoringCase("First Todo")));
    }

    @Test public void addTodo() throws Exception {
        final Todo newTodo = new Todo("New todo");
        final String newTodoJson = this.mapper.writeValueAsString(newTodo);
        System.out.println("************ " + newTodoJson);
        this.mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(newTodoJson))
            .andExpect(status().isCreated());
        //.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/todos/4"));
    }

    @Test public void updateTodo() throws Exception {
        Todo oldTodo = new Todo();
        oldTodo.setDescription("Old desc");
        oldTodo.setId(2L);
        final Todo updatedTodo = new Todo("Second todo updated");
        final String updatedTodoJson = this.mapper.writeValueAsString(updatedTodo);
        Mockito.when(todoRepository.findById(2L)).thenReturn(Optional.ofNullable(oldTodo));
        Mockito.when(todoRepository.save(oldTodo)).thenReturn(updatedTodo);
        this.mockMvc.perform(patch("/todos/2").contentType(MediaType.APPLICATION_JSON).content(updatedTodoJson))
            .andExpect(status().isOk()).andExpect(jsonPath("$.description", equalTo("Second todo updated")));
    }

    @Test public void deleteTodo() throws Exception {
        Todo todo = new Todo();
        Mockito.when(todoRepository.findById(3L)).thenReturn(Optional.ofNullable(todo));
        this.mockMvc.perform(delete("/todos/3")).andExpect(status().isNoContent());

    }
}
