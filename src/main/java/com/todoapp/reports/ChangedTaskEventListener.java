package com.todoapp.reports;

import com.todoapp.model.Task;
import com.todoapp.model.event.TaskDone;
import com.todoapp.model.event.TaskEvent;
import com.todoapp.model.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ChangedTaskEventListener {

    Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);
    PersistedTaskEventRepository repository;

    public ChangedTaskEventListener(PersistedTaskEventRepository repository) {
        this.repository = repository;
    }

//  async makes another thread for just logging
//  async enabled in TodoappApplication
    @Async
    @EventListener
    public void on(TaskDone event){
        onChanged(event);
    }
    @Async
    @EventListener
    public void on(TaskUndone event){
        onChanged(event);
    }
    @Async
    @EventListener
    public void on(Task event){

        logger.info(event.getDescription() + ": new task created");
    }

    void onChanged(TaskEvent event){
        logger.info("Got " + event);
        repository.save(new PersistedTaskEvent(event));
    }
}
