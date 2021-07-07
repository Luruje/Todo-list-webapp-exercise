package com.todoapp.adapter;


import com.todoapp.model.Task;
import com.todoapp.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.List;

//@RepositoryRestResource     //(path = "todos", collectionResourceRel = "tododos") //pozwala na uzywanie wbudowanych metod CRUD
@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);    //metoda pozwalajaca stwierdzic czy mozna zamknac grupe 'taskow' sprawdzajac czy sa wszystkie wykonane

//    @Override
//    @Query(nativeQuery = true, value="select count(*) > 0 from tasks where id=?1")
//    boolean existsById(Integer id);

    //    @RestResource(path = "done", rel = "done")      //osiagalne poprzez /tasks/search/done?state=true
//    List<Task> findByDone(@Param("state")boolean done);

//    @RestResource(path = "done", rel = "done")      //osiagalne poprzez tasks/search/done
//    List<Task> findByDoneIsTrue();

    //wylaczanie metody delete (trzeba nadpisac dwie)
//    @Override
//    @RestResource(exported = false)
//    void deleteById(Integer integer);
//
//    @Override
//    @RestResource(exported = false)
//    void delete(Task task);
}
