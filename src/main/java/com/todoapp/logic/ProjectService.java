package com.todoapp.logic;

import com.todoapp.TaskConfigurationProperties;
import com.todoapp.model.*;
import com.todoapp.model.projection.GroupReadModel;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectService {
    ProjectRepository repository;
    TaskGroupRepository taskGroupRepository;
    TaskConfigurationProperties config;

    public ProjectService(ProjectRepository repository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    List<Project> getAllProjects(){
       return repository.findAll();
    }

    Project saveProject(Project source){
        return repository.save(source);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline){
        if(!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId))
            throw new IllegalStateException("Only one undone group from project is allowed");

        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    TaskGroup targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getProjectSteps().stream().
                            map(projectStep -> new Task(
                                    projectStep.getDescription(), deadline.plusDays(projectStep.getDaysToDeadline())
                            )).collect(Collectors.toSet())
                    );
                    targetGroup.setProject(project);
                    return taskGroupRepository.save(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));

        return new GroupReadModel(result);
    }
}

