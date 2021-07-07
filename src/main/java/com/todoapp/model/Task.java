package com.todoapp.model;

import org.hibernate.annotations.GenericGenerator;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task extends AuditId{

    private LocalDateTime deadline;

    //definiujemy, ze wystepuje relacja many to one wzgledem task group (wiele taskow do jednej grupy)
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    @Embedded
    private Audit audit = new Audit();

//    @Transient    <- pozwala dodac kolumne, ktora nie bedzie zapisana w bazie danych a jedynie wysylana np. jacksonem do pointendu
//    private String isTransient;

    public Task() {
    }

    //for GroupTask DTO from projection use, safe task-creation
    public Task(String description, LocalDateTime deadline){
        this.setDescription(description);
        this.deadline=deadline;
    }
    //setery getery powinny byc package
    public LocalDateTime getDeadline() {return deadline; }

    public void setDeadline(LocalDateTime deadline) {this.deadline = deadline; }


    public TaskGroup getGroup() {
        return group;
    }

    public void setGroup(TaskGroup group) {
        this.group = group;
    }

    public void updateFrom(Task source){
        super.updateFrom(source);
        this.deadline = source.deadline;
        this.group = source.group;
    }



}
