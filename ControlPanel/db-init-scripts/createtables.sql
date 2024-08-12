CREATE TABLE IF NOT EXISTS Users (
    user_id UUID PRIMARY KEY,
    email VARCHAR(32) UNIQUE NOT NULL,
    full_name VARCHAR(128) NOT NULL,
    password VARCHAR NOT NULL
    );

CREATE TABLE IF NOT EXISTS Tasks (
     task_id UUID PRIMARY KEY,
     owner UUID NOT NULL,
     performer UUID,
     task_priority VARCHAR NOT NULL,
     task_status VARCHAR DEFAULT 'TODO' NOT NULL,
     task_name VARCHAR(64) NOT NULL,
    task_theme VARCHAR(64) NOT NULL,
    task_desc VARCHAR(256) NOT NULL,
    FOREIGN KEY (owner) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (performer) REFERENCES Users(user_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Comments (
    сomment_id UUID PRIMARY KEY,
    owner UUID NOT NULL,
    comment_to_task_id UUID NOT NULL,
    comment_text VARCHAR(256) NOT NULL,
    FOREIGN KEY (owner) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (comment_to_task_id) REFERENCES Tasks(task_id) ON DELETE CASCADE
    );