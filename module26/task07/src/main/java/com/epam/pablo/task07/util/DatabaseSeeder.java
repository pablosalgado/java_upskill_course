package com.epam.pablo.task07.util;

import java.util.Arrays;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.epam.pablo.task07.model.File;
import com.epam.pablo.task07.model.Folder;
import com.epam.pablo.task07.model.User;
import com.epam.pablo.task07.service.FileService;
import com.epam.pablo.task07.service.FolderService;
import com.epam.pablo.task07.service.UserService;

@Component
public class DatabaseSeeder {

    private final Logger logger = Logger.getLogger(DatabaseSeeder.class.getName());
    private final UserService userService;
    private final FolderService folderService;
    private final FileService fileService;
    private final ApplicationContext context;

    public DatabaseSeeder(
        UserService userService, 
        FolderService folderService, 
        FileService fileService, 
        ApplicationContext context
    ) {
        this.userService = userService;
        this.folderService = folderService;
        this.fileService = fileService;
        this.context = context;
    }

    public void seed() {
        try {
            createUsers();
            createFolders();
            createFiles();
            logger.info("Database seeded successfully!");
        } catch (Exception e) {
            logger.severe("Error seeding database: " + e.getMessage());
        }
    }

    public void uploadFile(String fileName, byte[] fileData) {
        var file = createFile(fileName, "application/octet-stream", fileData, 1);
        fileService.create(file);
        logger.info("File uploaded successfully!");
    }

    private void createUsers() {
        var users = Arrays.asList(
            createUser("john_doe", "john.doe@example.com", "hashed_password"),
            createUser("jane_doe", "jane.doe@example.com", "hashed_password")
        );

        users.forEach(userService::create);

        logger.info("Users created successfully!");
    }

    private User createUser(String username, String email, String password) {
        var user = context.getBean(User.class);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    private void createFolders() {
        var folders = Arrays.asList(
            createFolder("My Folder", 1),
            createFolder("Shared Folder", 1),
            createFolder("My Folder", 2),
            createFolder("Shared Folder", 2)
        );

        folders.forEach(folderService::create);

        logger.info("Folders created successfully!");
    }

    private Folder createFolder(String folderName, int ownerId) {
        var folder = context.getBean(Folder.class);
        folder.setFolderName(folderName);
        folder.setOwnerId(ownerId);
        return folder;
    }

    private void  createFiles() {
        var files = Arrays.asList(
            createFile("file1.txt", "text/plain", "lorem ipsum".getBytes(), 1),
            createFile("file2.txt", "text/plain", "dolor sit".getBytes(), 1),
            createFile("file3.txt", "text/plain", "amet consectetur".getBytes(), 2),
            createFile("file4.txt", "text/plain", "adipiscing elit".getBytes(), 2)
        );

        files.forEach(fileService::create);

        logger.info("Files created successfully!");
    }

    private File createFile(String fileName, String fileType, byte[] fileData, int ownerId) {
        var file = context.getBean(File.class);
        file.setFileName(fileName);
        file.setFileType(fileType);
        file.setFileSize(fileData.length);
        file.setFileData(fileData);
        file.setOwnerId(ownerId);
        return file;
    }
}