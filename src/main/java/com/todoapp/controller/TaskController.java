package com.todoapp.controller;

import com.todoapp.model.Task;
import com.todoapp.model.TaskRepository;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

//@RepositoryRestController
@RestController
public class TaskController {

    private final TaskRepository repository;
    Logger logger = LoggerFactory.getLogger(TaskController.class);

    public TaskController(final TaskRepository repository) {
         this.repository = repository;
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/tasks")
    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> getAllTasks(){
        logger.warn("Default get method giving up all the tasks");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping(value = "/tasks")
    ResponseEntity<List<Task>> getAllTasks(Pageable page){
        logger.warn("Custom get method giving up all the tasks");
        return ResponseEntity.ok(repository.findAll(page).getContent());    //.getContent pozwala pobrac jedynie Liste, bez informacji dodanych do metody
}

    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@RequestBody Task updatedTask, @PathVariable int id){
        if(!repository.existsById(id))
            return ResponseEntity.notFound().build();
        repository.findById(id).ifPresent(task -> {
            task.updateFrom(updatedTask);
            repository.save(task);
        });
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<?> getTask(@PathVariable int id){
        if(!repository.existsById(id))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(repository.findById(id));
    }

    @PostMapping("/tasks")
    ResponseEntity<?> postTask(@RequestBody Task toPost){
        Task result = repository.save(toPost);
        return ResponseEntity.ok(toPost);
    }

    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id){
        if(!repository.existsById(id))
            return ResponseEntity.notFound().build();
        repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }


//    public ResponseEntity<?> toggleTask(@PathVariable int id) {
//        if (!repository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        repository.findById(id)
//                .ifPresent(task -> task.setDone(!task.isDone()));
//        return ResponseEntity.noContent().build();
//    }

}
