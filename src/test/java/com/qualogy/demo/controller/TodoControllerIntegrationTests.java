package com.qualogy.demo.controller;

import com.qualogy.demo.TodoAppApplication;
import com.qualogy.demo.entity.Todo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.apache.http.client.HttpClient;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoAppApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase @FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestPropertySource(locations = "classpath:test.properties") public class TodoControllerIntegrationTests {
    @Autowired private TestRestTemplate restTemplate;
    private RestTemplate patchRestTemplate;
    @Autowired private ObjectMapper mapper;

    @LocalServerPort private int port;

    private String URL;

    @Before public void before() {
        this.URL = "http://localhost:" + port;

    }

    @Test public void _findAllTodos() throws JsonParseException, JsonMappingException, IOException {
        final Todo newTodo = new Todo("This is a new todo");

        final ResponseEntity<Todo> todo = this.restTemplate.postForEntity(this.URL + "/todos", newTodo, Todo.class);
        final Todo newTodo2 = new Todo("This is a new todo");

        final ResponseEntity<Todo> todo2 = this.restTemplate.postForEntity(this.URL + "/todos", newTodo2, Todo.class);
        final String response = this.restTemplate.getForObject(this.URL + "/todos", String.class);
        final List<Todo> todos = Arrays.asList(this.mapper.readValue(response, Todo[].class));

        assertThat(todos.size()).isEqualTo(2);
    }

    @Test public void findOne() throws JsonParseException, JsonMappingException, IOException {
        final Todo newTodo = new Todo("This is a new todo");
        this.restTemplate.postForEntity(this.URL + "/todos", newTodo, Todo.class);
        final String response = this.restTemplate.getForObject(this.URL + "/todos/1", String.class);
        final Todo todo = this.mapper.readValue(response, Todo.class);

        assertThat(todo.getId()).isEqualTo(1);
        assertThat(todo.getDescription()).isEqualToIgnoringCase("This is a new todo");
    }


    @Test public void updateTodo() {
        this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        final Todo updatedTodo = new Todo("This is an updated todo");
        final HttpEntity<Todo> entity = new HttpEntity<Todo>(updatedTodo);
        final Map<String, String> urlVariables = new HashMap<String, String>();
        urlVariables.put("id", "1");
        final ResponseEntity<Todo> responseEntity =
            this.patchRestTemplate.exchange(this.URL + "/todos/{id}", HttpMethod.PATCH, entity, Todo.class, urlVariables);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getId()).isEqualTo(1L);
        assertThat(responseEntity.getBody().getDescription()).isEqualTo("This is an updated todo");
    }

    @Test public void deleteTodo() {
        this.restTemplate.delete(this.URL + "/todos/2");
        final String response = this.restTemplate.getForObject(this.URL + "/todos/2", String.class);

        assertThat(response).isNull();
    }
}
