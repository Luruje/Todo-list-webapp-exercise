package com.todoapp.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
abstract public class AuditId {

    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")    //ustawiamy ze id bedzie generowane automatycznie, autoincrease
    private int id;
    @NotBlank(message = "Task's description must not be null")      //walidacja uniemozliwiajaca ustawic description jako pusty string itd.
    //walidacja skonfiugorwana w todoappapplication
    private String description;
    private boolean done;

    public void updateFrom(Task source) {
        this.description = source.getDescription();
        this.done = source.isDone();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
