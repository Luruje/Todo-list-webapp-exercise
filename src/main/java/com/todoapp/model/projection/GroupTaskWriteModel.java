package com.todoapp.model.projection;


import com.todoapp.model.Task;
import com.todoapp.model.TaskGroup;

import java.time.LocalDateTime;

//klasa uzywana jako forma do zapisywania taskow (DTO)
//uzywana do przesylania danych w bezpieczny sposob
public class GroupTaskWriteModel {
    private String description;
    private LocalDateTime deadline;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Task toTask(TaskGroup group){
        return new Task(description, deadline, group);
    }
}
