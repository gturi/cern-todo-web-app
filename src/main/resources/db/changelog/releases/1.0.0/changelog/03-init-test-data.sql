INSERT INTO task_categories
(category_id, creation_date, update_date, category_name, category_description)
VALUES(nextval('task_categories_category_id_seq'), now(), now(), 'Category 1', 'Category 1 description');

INSERT INTO task_categories
(category_id, creation_date, update_date, category_name, category_description)
VALUES(nextval('task_categories_category_id_seq'), now(), now(), 'Category 2', 'Category 2 description');


INSERT INTO tasks
(task_id, creation_date, update_date, task_name, task_description, deadline, category_id)
VALUES(nextval('tasks_task_id_seq'), now(), now(), 'Task 1', 'Task 1 description', '2025-01-25 20:41:23.000', (select category_id from task_categories where category_name = 'Category 1'));

INSERT INTO tasks
(task_id, creation_date, update_date, task_name, task_description, deadline, category_id)
VALUES(nextval('tasks_task_id_seq'), now(), now(), 'Task 2', 'Task 2 description', '2025-01-25 20:41:23.000', (select category_id from task_categories where category_name = 'Category 1'));

INSERT INTO tasks
(task_id, creation_date, update_date, task_name, task_description, deadline, category_id)
VALUES(nextval('tasks_task_id_seq'), now(), now(), 'Task 3', 'Task 3 description', '2025-01-25 20:41:23.000', (select category_id from task_categories where category_name = 'Category 1'));

INSERT INTO tasks
(task_id, creation_date, update_date, task_name, task_description, deadline, category_id)
VALUES(nextval('tasks_task_id_seq'), now(), now(), 'Task 4', 'Task 4 description', '2025-01-25 20:41:23.000', (select category_id from task_categories where category_name = 'Category 1'));

INSERT INTO tasks
(task_id, creation_date, update_date, task_name, task_description, deadline, category_id)
VALUES(nextval('tasks_task_id_seq'), now(), now(), 'Task 5', 'Task 5 description', '2025-01-25 20:41:23.000', (select category_id from task_categories where category_name = 'Category 1'));

INSERT INTO tasks
(task_id, creation_date, update_date, task_name, task_description, deadline, category_id)
VALUES(nextval('tasks_task_id_seq'), now(), now(), 'Task 6', 'Task 6 description', '2025-01-25 20:41:23.000', (select category_id from task_categories where category_name = 'Category 2'));

INSERT INTO tasks
(task_id, creation_date, update_date, task_name, task_description, deadline, category_id)
VALUES(nextval('tasks_task_id_seq'), now(), now(), 'Task 7', 'Task 7 description', '2025-01-25 20:41:23.000', (select category_id from task_categories where category_name = 'Category 2'));

INSERT INTO tasks
(task_id, creation_date, update_date, task_name, task_description, deadline, category_id)
VALUES(nextval('tasks_task_id_seq'), now(), now(), 'Task 8', 'Task 8 description', '2025-01-25 20:41:23.000', (select category_id from task_categories where category_name = 'Category 2'));

INSERT INTO tasks
(task_id, creation_date, update_date, task_name, task_description, deadline, category_id)
VALUES(nextval('tasks_task_id_seq'), now(), now(), 'Task 9', 'Task 9 description', '2025-01-25 20:41:23.000', (select category_id from task_categories where category_name = 'Category 2'));

INSERT INTO tasks
(task_id, creation_date, update_date, task_name, task_description, deadline, category_id)
VALUES(nextval('tasks_task_id_seq'), now(), now(), 'Task 20', 'Task 10 description', '2025-01-25 20:41:23.000', (select category_id from task_categories where category_name = 'Category 2'));
