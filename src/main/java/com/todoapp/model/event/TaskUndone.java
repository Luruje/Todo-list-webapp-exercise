package com.todoapp.model.event;

import com.todoapp.model.Task;

import java.time.Clock;

public class TaskUndone extends TaskEvent {
    public TaskUndone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
