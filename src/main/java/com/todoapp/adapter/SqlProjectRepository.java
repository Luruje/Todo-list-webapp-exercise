package com.todoapp.adapter;

import com.todoapp.model.Project;
import com.todoapp.model.ProjectRepository;
import com.todoapp.model.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("select distinct p from Project p join fetch p.projectSteps")
    List<Project> findAll();

}
