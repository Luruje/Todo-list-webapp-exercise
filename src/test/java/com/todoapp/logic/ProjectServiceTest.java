package com.todoapp.logic;

import com.todoapp.TaskConfigurationProperties;
import com.todoapp.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    void test_createGroup_AllowMultipleTasksIsFalse_and_undoneGroupsExist_throws_illegalStateException() {
        //given
        TaskGroupRepository mockTaskGroupRepo = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepo.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
        //and
        TaskConfigurationProperties.Template mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(false);
        //and
        TaskConfigurationProperties mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        //system under test
        ProjectService toTest = new ProjectService(null, mockTaskGroupRepo, mockConfig, null);
        String result = assertThrows(IllegalStateException.class, () -> toTest.createGroup(0, null)).getMessage();
        //assert
        assertEquals("Only one undone group from project is allowed", result);

    }

    @Test
    void test_createGroup_AllowMultipleTasksIsTrue_and_undoneGroupsDoesNotExist_throws_illegalArgumentException() {
        //given
        TaskGroupRepository mockTaskGroupRepo = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepo.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(false);
        //and
        TaskConfigurationProperties.Template mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(true);
        //and
        TaskConfigurationProperties mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        //and
        ProjectRepository mockProjectRepo = mock(ProjectRepository.class);
        when(mockProjectRepo.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        ProjectService toTest = new ProjectService(mockProjectRepo, mockTaskGroupRepo, mockConfig, null);
        String result = assertThrows(IllegalArgumentException.class, () -> toTest.createGroup(10,null)).getMessage();
        //assert
        assertEquals("Project with given id not found", result);

    }

    Project projectWith(String description, Set<Integer> daystoDeadLine){
        Project mockProject = mock(Project.class);
        when(mockProject.getDescription()).thenReturn(description);
        when(mockProject.getSteps()).thenReturn(
                daystoDeadLine.stream()
                .map(days ->{
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet())
        );
        return mockProject;
    }
}