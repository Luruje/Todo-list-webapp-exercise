package com.todoapp.adapter;

import com.todoapp.model.TaskGroup;
import com.todoapp.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    //domyslnie nativequerry = false, i uzywanym jezykiem jest JQL (java sql)
    @Override
    @Query("select distinct tg from TaskGroup tg join fetch tg.tasks")
    List<TaskGroup> findAll();      //zastosowano distinct by dostac unikalne, niepwotarzalne grupy, w innym wypadku mogloby zwrocic tyle razy te sama grupe
    //ile ma w sumie taskow

//    Zmodyfikować TaskGroupRepository - dodać pobieranie wszystkich niezrobionych grup w obrębie danego projektu
    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);

//    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId)
}
