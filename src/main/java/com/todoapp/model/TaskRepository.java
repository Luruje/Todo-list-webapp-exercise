package com.todoapp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//tworzymy klase ktora bedzie odpowiadac za enkapsulacje i ograniczenie udostepnionych na zewnatrz metod restowych
//nalezy dodac do prawowitego repo jako extends
public interface TaskRepository {

    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    List<Task> findByDone(@Param("state")boolean done);

    Optional<Task> findById(int id);

    Task save(Task task);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

}
