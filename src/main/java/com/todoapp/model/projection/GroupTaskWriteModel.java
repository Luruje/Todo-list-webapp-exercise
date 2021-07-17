package com.todoapp.model.projection;


import com.todoapp.model.Task;
import com.todoapp.model.TaskGroup;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

//klasa uzywana jako forma do zapisywania taskow (DTO)
//uzywana do przesylania danych w bezpieczny sposob
public class GroupTaskWriteModel {
    @NotBlank(message = "Task's description must not be null")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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
