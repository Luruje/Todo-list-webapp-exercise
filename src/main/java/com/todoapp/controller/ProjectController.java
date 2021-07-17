package com.todoapp.controller;

import com.todoapp.logic.ProjectService;
import com.todoapp.model.Project;
import com.todoapp.model.ProjectStep;
import com.todoapp.model.projection.ProjectWriteModel;
import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    //maps on thymeleaf projects.html automatically
    @GetMapping
    String showProjects(Model model){
        //model allows communication between projects.html and a controller
        //init "project" object, which is ProjectWriteModel instance
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";  //refers to projects.html
    }

    @PostMapping
    String addProject(@ModelAttribute("project")  @Valid ProjectWriteModel current, BindingResult bindingResult, Model model){
        //validation by using object of BindingResult, if has any errors dont send new project to db
        if(bindingResult.hasErrors()) {
            return "projects";
        }
        service.saveProject(current);
        model.addAttribute("project", new ProjectWriteModel()); // this is to set-up new project after sumbiting previous one
        model.addAttribute("projects", getProjects());          // this makes the page load project that was added
        model.addAttribute("message", "Project created!");    //a message to pop up
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @PostMapping("/{id}")
    String createGroup(@ModelAttribute("project") ProjectWriteModel current,
                       Model model,
                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")LocalDateTime deadline,
                       @PathVariable int id ){
        //since createGroup can throw exceptions, its wrapped with try-catch
        try{
            service.createGroup(id, deadline);
            model.addAttribute("message", "Group created!");
        }catch(IllegalStateException | IllegalArgumentException e){
            model.addAttribute("message", "There was an error during creation!");
        }
        return "projects";
    }

//    <dl th:each="project : ${projects}">
    @ModelAttribute("projects")
    List<Project> getProjects(){
        return service.getAllProjects();
    }
}
