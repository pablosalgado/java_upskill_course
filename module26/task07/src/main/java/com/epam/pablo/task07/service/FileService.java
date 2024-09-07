package com.epam.pablo.task07.service;

import java.util.List;

import com.epam.pablo.task07.model.File;

public interface FileService {
    List<File> getAll();
    
    File getById(int FileId);

    boolean create(File File);

    boolean update(File File);

    boolean delete(int FileId);
}
