package com.todoapp.logic;

import com.todoapp.TaskConfigurationProperties;
import com.todoapp.model.*;
import com.todoapp.model.projection.GroupReadModel;
import com.todoapp.model.projection.GroupTaskWriteModel;
import com.todoapp.model.projection.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectService {
    ProjectRepository repository;
    TaskGroupRepository taskGroupRepository;
    TaskConfigurationProperties config;
    private TaskGroupService taskGroupService;

    public ProjectService(ProjectRepository repository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties config, TaskGroupService taskGroupService) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
        this.taskGroupService = taskGroupService;
    }

    List<Project> getAllProjects(){
       return repository.findAll();
    }

    Project saveProject(Project source){
        return repository.save(source);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline){
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getProjectSteps().stream()
                                    .map(projectStep -> {
                                                var task = new GroupTaskWriteModel();
                                                task.setDescription(projectStep.getDescription());
                                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                                return task;
                                            }
                                    ).collect(Collectors.toSet())
                    );
                    return taskGroupService.createGroup(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}

