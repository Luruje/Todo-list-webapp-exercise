package com.todoapp.logic;

import com.todoapp.TaskConfigurationProperties;
import com.todoapp.model.ProjectRepository;
import com.todoapp.model.TaskGroupRepository;
import com.todoapp.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfig {

    @Bean
    ProjectService projectService(ProjectRepository repository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties config){
        return new ProjectService(repository, taskGroupRepository, config);
    }

    @Bean
    TaskGroupService taskGroupService(TaskGroupRepository repository, TaskRepository taskRepository){
        return new TaskGroupService(repository, taskRepository);
    }
}
