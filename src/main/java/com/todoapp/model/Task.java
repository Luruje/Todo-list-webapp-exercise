package com.todoapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.todoapp.model.event.TaskEvent;
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

    public Task() {
    }

    //for GroupTask DTO from projection use, safe task-creation
    public Task(String description, LocalDateTime deadline){
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup group){
        this.setDescription(description);
        this.deadline=deadline;
        if(group!=null)
            this.group=group;
    }

    public TaskEvent toggle(){
        this.setDone(!this.isDone());
        return TaskEvent.changed(this);
    }

    //setery getery powinny byc package
    public LocalDateTime getDeadline() {return deadline; }

    public void setDeadline(LocalDateTime deadline) {this.deadline = deadline; }

    @JsonIgnore
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
