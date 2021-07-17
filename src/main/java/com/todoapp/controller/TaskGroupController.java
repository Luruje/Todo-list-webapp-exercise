package com.todoapp.controller;

import com.todoapp.logic.TaskGroupService;
import com.todoapp.model.ProjectStep;
import com.todoapp.model.Task;
import com.todoapp.model.TaskRepository;
import com.todoapp.model.projection.GroupReadModel;
import com.todoapp.model.projection.GroupTaskWriteModel;
import com.todoapp.model.projection.GroupWriteModel;
import com.todoapp.model.projection.ProjectWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

//since RestController has @ResponseBody on all of its methods, which indicates that all the methods return values that are bound to the web response body, we change it to Controller
@Controller
@IllegalExceptionProcessing
@RequestMapping("/groups")
public class TaskGroupController {

    private final TaskGroupService service;
    TaskRepository taskRepository;
    Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

    public TaskGroupController(TaskGroupService service, TaskRepository taskRepository) {
        this.service = service;
        this.taskRepository = taskRepository;
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> getAllGroups(){
        return ResponseEntity.ok(service.getAllGroups());
    }

    @ResponseBody
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> getAllTasksByGroupId(@PathVariable(name = "id") int groupId){
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(groupId));
    }

    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<GroupReadModel> createGroup(@RequestBody GroupWriteModel source){
        var result = service.saveGroup(source);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @ResponseBody
    @Transactional
    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id){
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

//    thymeleaf:

    @GetMapping
    String showGroups(Model model){
        //model allows communication between groups.html and a controller
        //init "project" object, which is ProjectWriteModel instance
        model.addAttribute("group", new GroupWriteModel());
        return "groups";  //refers to projects.html
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(@ModelAttribute("group")  @Valid GroupWriteModel current,
                    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")LocalDateTime deadline,
                    BindingResult bindingResult,
                    Model model){
        //validation by using object of BindingResult, if has any errors dont send new group to db
        if(bindingResult.hasErrors()) {
            return "groups";
        }
        service.saveGroup(current);
        model.addAttribute("group", new GroupWriteModel()); // this is to set-up new group after submiting previous one
        model.addAttribute("groups", getGroups());          // this makes the page load group that was added
        model.addAttribute("message", "Group created!");    //a message to pop up
        return "groups";
    }

    //    <button type="submit" name="addTask">+</button> from groups.html
    @PostMapping(params = "addTask")
    String addGroupTask(@ModelAttribute("group") GroupWriteModel current){
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }

    @ModelAttribute("groups")
    List<GroupReadModel> getGroups(){
        return service.getAllGroups();
    }
}
//    jak parametryzowac adresy url