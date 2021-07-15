package com.todoapp.controller;

import com.todoapp.logic.TaskGroupService;
import com.todoapp.model.Task;
import com.todoapp.model.TaskGroupRepository;
import com.todoapp.model.TaskRepository;
import com.todoapp.model.projection.GroupReadModel;
import com.todoapp.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class TaskGroupController {

    private final TaskGroupService service;
    TaskRepository taskRepository;
    Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

    public TaskGroupController(TaskGroupService service, TaskRepository taskRepository) {
        this.service = service;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> getAllGroups(){
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}/tasks")
    ResponseEntity<List<Task>> getAllTasksByGroupId(@PathVariable int groupId){
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(groupId));
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody GroupWriteModel source){
        var result = service.createGroup(source);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id){
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
