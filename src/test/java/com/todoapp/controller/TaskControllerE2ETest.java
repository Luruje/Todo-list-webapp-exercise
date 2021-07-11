package com.todoapp.controller;

import com.todoapp.model.Task;
import com.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
//integration profile was used for TaskRepository mock injection, has been replaced by h2 db in memory (look TaskConfiguration)
//@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {

    @LocalServerPort
    int port;

    //pozwala odpytywac uslugi bedace na localhoscie
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    TaskRepository repo;

    @Test
    void httpGet_ReturnsAllTasks(){
        //given
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));

        //when, send request for all the objects at /tasks
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        //asert
        assertEquals(result.length, 2);
    }

}