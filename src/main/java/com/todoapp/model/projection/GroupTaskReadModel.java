package com.todoapp.model.projection;

import com.todoapp.model.Task;

public class GroupTaskReadModel {
    private boolean done;
    private String description;

    public GroupTaskReadModel(Task source) {
        this.description= source.getDescription();
        this.done=source.isDone();
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
