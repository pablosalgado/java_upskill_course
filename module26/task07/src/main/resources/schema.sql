DROP TABLE IF EXISTS activity_log CASCADE;
DROP TABLE IF EXISTS shares CASCADE;
DROP TABLE IF EXISTS permissions CASCADE;
DROP TABLE IF EXISTS file_folder CASCADE;
DROP TABLE IF EXISTS files CASCADE;
DROP TABLE IF EXISTS folders CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE files (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(50),
    file_size BIGINT,
    file_data BYTEA,
    owner_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE folders (
    folder_id SERIAL PRIMARY KEY,
    folder_name VARCHAR(255) NOT NULL,
    owner_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE file_folder (
    file_id INTEGER NOT NULL,
    folder_id INTEGER NOT NULL,
    PRIMARY KEY (file_id, folder_id),
    FOREIGN KEY (file_id) REFERENCES files(id),
    FOREIGN KEY (folder_id) REFERENCES folders(folder_id)
);

CREATE TABLE permissions (
    permission_id SERIAL PRIMARY KEY,
    file_id INTEGER,
    folder_id INTEGER,
    user_id INTEGER NOT NULL,
    permission_type VARCHAR(50) NOT NULL,
    granted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (file_id) REFERENCES files(id),
    FOREIGN KEY (folder_id) REFERENCES folders(folder_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE shares (
    share_id SERIAL PRIMARY KEY,
    sender_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    file_id INTEGER,
    folder_id INTEGER,
    share_status VARCHAR(50) NOT NULL,
    shared_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id),
    FOREIGN KEY (file_id) REFERENCES files(id),
    FOREIGN KEY (folder_id) REFERENCES folders(folder_id)
);

CREATE TABLE activity_Log (
    activity_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    file_id INTEGER,
    folder_id INTEGER,
    activity_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (file_id) REFERENCES files(id),
    FOREIGN KEY (folder_id) REFERENCES folders(folder_id)
);

CREATE OR REPLACE PROCEDURE findAllFiles()
LANGUAGE plpgsql
AS '
BEGIN
    SELECT * FROM Files;
END;
';

CREATE OR REPLACE PROCEDURE findFileById(IN fileId INT)
LANGUAGE plpgsql
AS '
BEGIN
    SELECT * FROM Files WHERE id = fileId;
END;
';

CREATE OR REPLACE PROCEDURE createFile(IN fileName TEXT, IN fileType TEXT, IN fileSize BIGINT, IN fileData BYTEA, IN ownerId INT)
LANGUAGE plpgsql
AS '
BEGIN
    INSERT INTO Files (file_name, file_type, file_size, file_data, owner_id) VALUES (fileName, fileType, fileSize, fileData, ownerId);
END;
';

CREATE OR REPLACE PROCEDURE updateFile(IN fileId INT, IN fileName TEXT, IN fileType TEXT, IN fileSize BIGINT, IN fileData BYTEA, IN ownerId INT)
LANGUAGE plpgsql
AS '
BEGIN
    UPDATE Files SET file_name = fileName, file_type = fileType, file_size = fileSize, file_data = fileData, owner_id = ownerId WHERE id = fileId;
END;
';

CREATE OR REPLACE PROCEDURE deleteFile(IN fileId INT)
LANGUAGE plpgsql
AS '
BEGIN
    DELETE FROM Files WHERE id = fileId;
END;
';