-- CernAdminPassword
INSERT INTO users
(user_id, creation_date, update_date, username, password, firstname, lastname, role)
VALUES(nextval('users_user_id_seq'), now(), now(), 'cernAdmin', '$2a$10$IWhDhCk2B0X4iWuKD1pSCuI7NRaOF6NtZqAEzJqSDVonqoXkfUawu', 'Cern', 'Admin', 'ROLE_ADMIN');

-- AliceUserPassword
INSERT INTO users
(user_id, creation_date, update_date, username, password, firstname, lastname, role)
VALUES(nextval('users_user_id_seq'), now(), now(), 'aliceUser', '$2a$10$.24auD3aCHzurRAJm0pv0eadbtpp/bCig8SuwlxIYia9FZIDgPIWi', 'Alice', 'Ikari', 'ROLE_USER');

-- BobUserPassword
INSERT INTO users
(user_id, creation_date, update_date, username, password, firstname, lastname, role)
VALUES(nextval('users_user_id_seq'), now(), now(), 'bobUser', '$2a$10$paPBxu5qLXfE5o1Dbq/vv.yEzy79J/E1.5b3j6ljh7CMf.HqZG/NO', 'Bob', 'Ross', 'ROLE_USER');

-- CharlieUserPassword
INSERT INTO users
(user_id, creation_date, update_date, username, password, firstname, lastname, role)
VALUES(nextval('users_user_id_seq'), now(), now(), 'chrisUser', '$2a$10$V4F0YhQCAfaDJXW2Lwe/Ge3gwQBgrUe9EfVCtubgCw4GUlG3r8/N.', 'Chris', 'Fantasy', 'ROLE_USER');


INSERT INTO task_categories
(category_id, creation_date, update_date, category_name, category_description)
VALUES(nextval('task_categories_category_id_seq'), now(), now(), 'Category 1', 'Category 1 description');

INSERT INTO task_categories
(category_id, creation_date, update_date, category_name, category_description)
VALUES(nextval('task_categories_category_id_seq'), now(), now(), 'Category 2', 'Category 2 description');


INSERT INTO tasks(task_id,creation_date, creation_user_id,update_date, update_user_id,task_name, task_description, deadline,category_id,assigned_to_user_id, assigned_to_username)
VALUES(nextval('tasks_task_id_seq'),now(), 1,now(), 1,'Task 1', 'Task 1 description', '2025-01-25 20:41:23.000',1,1, 'cernAdmin');


INSERT INTO tasks(
    task_id,
    creation_date, creation_user_id,
    update_date, update_user_id,
    task_name, task_description, deadline,
    category_id,
    assigned_to_user_id, assigned_to_username
)
VALUES(
    nextval('tasks_task_id_seq'),
    now(), (select user_id from users where username = 'cernAdmin'),
    now(), (select user_id from users where username = 'cernAdmin'),
    'Task 1', 'Task 1 description', '2025-01-25 20:41:23.000',
    (select category_id from task_categories where category_name = 'Category 1'),
    (select user_id from users where username = 'cernAdmin'), 'cernAdmin'
);


INSERT INTO tasks(
    task_id,
    creation_date, creation_user_id,
    update_date, update_user_id,
    task_name, task_description, deadline,
    category_id,
    assigned_to_user_id, assigned_to_username
)
VALUES(
    nextval('tasks_task_id_seq'),
    now(), (select user_id from users where username = 'cernAdmin'),
    now(), (select user_id from users where username = 'cernAdmin'),
    'Task 2', 'Task 2 description', '2025-01-25 20:41:23.000',
     (select category_id from task_categories where category_name = 'Category 1'),
     (select user_id from users where username = 'cernAdmin'), 'cernAdmin'
);

INSERT INTO tasks(
    task_id,
    creation_date, creation_user_id,
    update_date, update_user_id,
    task_name, task_description, deadline,
    category_id,
    assigned_to_user_id, assigned_to_username
)
VALUES(
    nextval('tasks_task_id_seq'),
    now(), (select user_id from users where username = 'cernAdmin'),
    now(), (select user_id from users where username = 'cernAdmin'),
    'Task 3', 'Task 3 description', '2025-01-25 20:41:23.000',
    (select category_id from task_categories where category_name = 'Category 1'),
    (select user_id from users where username = 'cernAdmin'), 'cernAdmin'
);

INSERT INTO tasks(
    task_id,
    creation_date, creation_user_id,
    update_date, update_user_id,
    task_name, task_description, deadline,
    category_id,
    assigned_to_user_id, assigned_to_username
)
VALUES(
    nextval('tasks_task_id_seq'),
    now(), (select user_id from users where username = 'aliceUser'),
    now(), (select user_id from users where username = 'aliceUser'),
    'Task 4', 'Task 4 description', '2025-01-25 20:41:23.000',
    (select category_id from task_categories where category_name = 'Category 1'),
    (select user_id from users where username = 'aliceUser'), 'aliceUser'
);

INSERT INTO tasks(
    task_id,
    creation_date, creation_user_id,
    update_date, update_user_id,
    task_name, task_description, deadline,
    category_id,
    assigned_to_user_id, assigned_to_username
)
VALUES(
    nextval('tasks_task_id_seq'),
    now(), (select user_id from users where username = 'cernAdmin'),
    now(), (select user_id from users where username = 'cernAdmin'),
    'Task 5', 'Task 5 description', '2025-01-25 20:41:23.000',
    (select category_id from task_categories where category_name = 'Category 1'),
    (select user_id from users where username = 'aliceUser'), 'aliceUser'
);

INSERT INTO tasks(
    task_id,
    creation_date, creation_user_id,
    update_date, update_user_id,
    task_name, task_description, deadline,
    category_id,
    assigned_to_user_id, assigned_to_username
)
VALUES(
    nextval('tasks_task_id_seq'),
    now(), (select user_id from users where username = 'aliceUser'),
    now(), (select user_id from users where username = 'cernAdmin'),
    'Task 6', 'Task 6 description', '2025-01-25 20:41:23.000',
    (select category_id from task_categories where category_name = 'Category 2'),
    (select user_id from users where username = 'cernAdmin'), 'cernAdmin'
);

INSERT INTO tasks(
    task_id,
    creation_date, creation_user_id,
    update_date, update_user_id,
    task_name, task_description, deadline,
    category_id,
    assigned_to_user_id, assigned_to_username
)
VALUES(
    nextval('tasks_task_id_seq'),
    now(), (select user_id from users where username = 'bobUser'),
    now(), (select user_id from users where username = 'bobUser'),
    'Task 7', 'Task 7 description', '2025-01-25 20:41:23.000',
    (select category_id from task_categories where category_name = 'Category 2'),
    (select user_id from users where username = 'bobUser'), 'bobUser'
);

INSERT INTO tasks(
    task_id,
    creation_date, creation_user_id,
    update_date, update_user_id,
    task_name, task_description, deadline,
    category_id,
    assigned_to_user_id, assigned_to_username
)
VALUES(
    nextval('tasks_task_id_seq'),
    now(), (select user_id from users where username = 'bobUser'),
    now(), (select user_id from users where username = 'cernAdmin'),
    'Task 8', 'Task 8 description', '2025-01-25 20:41:23.000',
    (select category_id from task_categories where category_name = 'Category 2'),
    (select user_id from users where username = 'bobUser'), 'bobUser'
);

INSERT INTO tasks(
    task_id,
    creation_date, creation_user_id,
    update_date, update_user_id,
    task_name, task_description, deadline,
    category_id,
    assigned_to_user_id, assigned_to_username
)
VALUES(
    nextval('tasks_task_id_seq'),
    now(), (select user_id from users where username = 'bobUser'),
    now(), (select user_id from users where username = 'cernAdmin'),
    'Task 9', 'Task 9 description', '2025-01-25 20:41:23.000',
    (select category_id from task_categories where category_name = 'Category 2'),
    (select user_id from users where username = 'bobUser'), 'bobUser'
);

INSERT INTO tasks(
    task_id,
    creation_date, creation_user_id,
    update_date, update_user_id,
    task_name, task_description, deadline,
    category_id,
    assigned_to_user_id, assigned_to_username
)
VALUES(
    nextval('tasks_task_id_seq'),
    now(), (select user_id from users where username = 'chrisUser'),
    now(), (select user_id from users where username = 'cernAdmin'),
    'Task 10', 'Task 10 description', '2025-01-25 20:41:23.000',
    (select category_id from task_categories where category_name = 'Category 2'),
    (select user_id from users where username = 'cernAdmin'), 'cernAdmin'
);

INSERT INTO tasks(
    task_id,
    creation_date, creation_user_id,
    update_date, update_user_id,
    task_name, task_description, deadline,
    category_id,
    assigned_to_user_id, assigned_to_username
)
VALUES(
    nextval('tasks_task_id_seq'),
    now(), (select user_id from users where username = 'chrisUser'),
    now(), (select user_id from users where username = 'cernAdmin'),
    'Task 11', 'Task 11 description', '2025-01-25 20:41:23.000',
    (select category_id from task_categories where category_name = 'Category 2'),
    (select user_id from users where username = 'cernAdmin'), 'cernAdmin'
);
