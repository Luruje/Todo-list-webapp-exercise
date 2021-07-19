package com.todoapp.reports;

import com.todoapp.model.Task;
import com.todoapp.model.TaskRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private TaskRepository taskRepository;
    private PersistedTaskEventRepository eventRepository;

    public ReportController(TaskRepository taskRepository, PersistedTaskEventRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/count/{id}")
    public ResponseEntity<TaskChangesCount> getCountOfChanges(@PathVariable int id){

        return taskRepository.findById(id)
                .map(task -> new TaskChangesCount(task, eventRepository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static class TaskChangesCount {
        public String description;
        public boolean done;
        public int changedCount;
        public TaskChangesCount(Task task, List<PersistedTaskEvent> events) {
            description = task.getDescription();
            done = task.isDone();
            changedCount = events.size();
        }
    }
}
