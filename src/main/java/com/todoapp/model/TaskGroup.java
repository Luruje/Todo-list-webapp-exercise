package com.todoapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup extends AuditId{

    //definiujemy relacje one to many (jedna grupa dla wielu taskow) i tworzymy zatem set (taka lista) taskow
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group") //ustawiamy, ze jesli zostanie usunieta grupa, usuwamy rowniez wszystkie jej nalezace taski i jak sie nazywa obiekt grupy w klasie Task
    private Set<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Embedded
    private Audit audit = new Audit();

//    @Transient    <- pozwala dodac kolumne, ktora nie bedzie zapisana w bazie danych a jedynie wysylana np. jacksonem do pointendu
//    private String isTransient;

    public TaskGroup() {
    }
    //setery getery powinny byc package


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
