alter table task_groups
    add column project_id int null;

alter table task_groups
    add foreign key (project_id) references projects(id);

--alter table tasks add column task_group_id int null;
--alter table tasks
--    add foreign key (task_group_id) references task_groups(id);