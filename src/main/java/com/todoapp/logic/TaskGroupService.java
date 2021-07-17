package com.todoapp.logic;

import com.todoapp.model.Project;
import com.todoapp.model.TaskGroup;
import com.todoapp.model.TaskGroupRepository;
import com.todoapp.model.TaskRepository;
import com.todoapp.model.projection.GroupReadModel;
import com.todoapp.model.projection.GroupWriteModel;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@RequestScope
public class TaskGroupService {
    TaskGroupRepository repository;
    TaskRepository taskRepository;


    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel saveGroup(GroupWriteModel source){
        return saveGroup(source, null);
    }

    public GroupReadModel saveGroup(GroupWriteModel source, Project project){
        TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> getAllGroups(){
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int id){
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(id))
            throw new IllegalStateException("Group has undone tasks. Do all the tasks");

        TaskGroup foundGroup = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));

        foundGroup.setDone(!foundGroup.isDone());
        repository.save(foundGroup);
    }

}
