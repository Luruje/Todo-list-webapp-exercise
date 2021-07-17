package com.todoapp.model.projection;

import com.todoapp.model.Project;
import com.todoapp.model.ProjectStep;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {

    @NotBlank(message = "Project's description must not be empty!")
    private String description;
    @Valid
    private List<ProjectStep> steps = new ArrayList<>();

    //that's used for creating new Project with at least one pre-created project step
    //since in the form, in thymeleaf, we begin with 1 project step
    public ProjectWriteModel() {
        steps.add(new ProjectStep());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ProjectStep> steps) {
        this.steps = steps;
    }

    //since it's project's projection, which is used for saving new projects, this method creates from this projection a real Project object
    public Project toProject(){

        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));

        return result;
    }
}
