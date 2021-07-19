package com.todoapp.model.event;

import com.todoapp.model.Task;

import java.time.Clock;
import java.time.Instant;

//just own event learning class
//changes in: Tasks (toggle method), TaskController eventpublisher and toggling
public class TaskEvent {

    public static TaskEvent changed(Task source){
        return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }

    private int taskId;
    private Instant occurence;

    TaskEvent(int taskId, Clock clock) {
        this.taskId = taskId;
        this.occurence = Instant.now(clock);
    }

    public int getTaskId() {
        return taskId;
    }

    public Instant getOccurence() {
        return occurence;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "taskId=" + taskId +
                ", occurence=" + occurence +
                '}';
    }
}
