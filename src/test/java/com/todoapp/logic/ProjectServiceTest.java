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
//
//    void test_findAllPageable_anyPageRequest_returnsPageOfSWCharacters() {
//        List<SWCharacter> givenSWCharacterList = Arrays.asList(createSWCharacter(),
//                createSWCharacter());
//        Page<SWCharacter> givenSWCharacterPage = new PageImpl(givenSWCharacterList);
//
//        SWCharacterRepositoryForm mockedSWCharacterRepository = mock(SWCharacterRepositoryForm.class);
//        when(mockedSWCharacterRepository.findAll(any(PageRequest.class)))
//                .thenReturn(givenSWCharacterPage);
//
//        SWCharacterServiceImpl toTest = new SWCharacterServiceImpl(mockedSWCharacterRepository);
//        Page<SWCharacter> result = toTest.getAllSWCharacters(PageRequest.of(0, 2));
//
//        assertEquals(givenSWCharacterPage, result);
//    }

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
        ProjectService toTest = new ProjectService(null, mockTaskGroupRepo, mockConfig);
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
        ProjectService toTest = new ProjectService(mockProjectRepo, mockTaskGroupRepo, mockConfig);
        String result = assertThrows(IllegalArgumentException.class, () -> toTest.createGroup(10,null)).getMessage();
        //assert
        assertEquals("Project with given id not found", result);

    }
@Test
 void test_createGroup_AllowMultipleTasksIsTrue_and_undoneGroupsDoesNotExist_createsAndSaveTaskGroup() {
        //given
        LocalDateTime today = LocalDate.now().atStartOfDay();
        //and
        TaskGroupRepository memoryTaskGroupRepo = inMemoryTaskGroupRepository();
        when(memoryTaskGroupRepo.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(false);
        //and
        TaskConfigurationProperties.Template mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(true);
        //and
        TaskConfigurationProperties mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        //and
        ProjectRepository mockProjectRepo = mock(ProjectRepository.class);
        when(mockProjectRepo.findById(anyInt())).thenReturn(Optional.of(projectWith("bar", Set.of(-1, -2))));
        //system under test
        ProjectService toTest = new ProjectService(mockProjectRepo, memoryTaskGroupRepo, mockConfig);
        var result = toTest.createGroup(1, today);
        //assert

        assertEquals(result.getDeadline(), "bar");

    }

    private TaskGroupRepository inMemoryTaskGroupRepository(){
        return new TaskGroupRepository() {

            private int index=0;
            private Map<Integer, TaskGroup> map = new HashMap<>();

            @Override
            public List<TaskGroup> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public TaskGroup save(TaskGroup entity) {
                if(entity.getId()==0){
                    try{
                        TaskGroup.class.getDeclaredField("id").set(entity, ++index);
                    }catch(NoSuchFieldException | IllegalStateException | IllegalAccessException e){
                        throw new RuntimeException();
                    }
                }
                map.put(entity.getId(), entity);
                return entity;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return map.values().stream()
                        .filter(e -> !e.isDone())
                        .filter(e -> e.getProject()!=null && e.getProject().getId()==projectId)
                        .findAny().isPresent();
            }
        };
    }

    Project projectWith(String description, Set<Integer> daystoDeadLine){
        Project mockProject = mock(Project.class);
        when(mockProject.getDescription()).thenReturn(description);
        when(mockProject.getProjectSteps()).thenReturn(
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