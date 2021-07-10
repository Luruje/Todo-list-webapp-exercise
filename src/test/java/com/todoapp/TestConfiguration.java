package com.todoapp;

import com.todoapp.model.Task;
import com.todoapp.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Configuration
public class TestConfiguration {

    @Bean
    @Profile("integration")
    TaskRepository testRepo(){
        return new TaskRepository() {
            Map<Integer, Task> tasks = new HashMap();
            @Override
            public List<Task> findAll() {
                return new ArrayList<Task>(tasks.values());
            }

            @Override
            public Page<Task> findAll(Pageable page) {
                return null;
            }

            @Override
            public List<Task> findByDone(boolean done) {
                return null;
            }

            @Override
            public Optional<Task> findById(int id) {
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public Task save(Task task) {
                return tasks.put(tasks.size() + 1, task);
            }

            @Override
            public boolean existsById(Integer id) {
                return tasks.containsKey(id);
            }

            @Override
            public boolean existsByDoneIsFalseAndGroup_Id(Integer groupId) {
                return false;
            }
        };
    }
}
