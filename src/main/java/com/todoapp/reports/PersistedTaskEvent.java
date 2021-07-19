package com.todoapp.reports;

import com.todoapp.model.event.TaskEvent;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "task_events")
public class PersistedTaskEvent {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    public int id;
    public int taskId;
    public String name;
    public LocalDateTime occurence;

    public PersistedTaskEvent() {
    }

    public PersistedTaskEvent(TaskEvent source) {
        taskId = source.getTaskId();
        name = source.getClass().getSimpleName();
        occurence = LocalDateTime.ofInstant(source.getOccurence(), ZoneId.systemDefault());
    }

    public int getId() {
        return id;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getOccurence() {
        return occurence;
    }
}
