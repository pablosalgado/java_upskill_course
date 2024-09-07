package com.epam.pablo.task07.service;

import com.epam.pablo.task07.model.Folder;
import java.util.List;

public interface FolderService {
    List<Folder> getAll();

    Folder getById(int folderId);
    
    boolean create(Folder folder);
    
    boolean update(Folder folder);
    
    boolean delete(int folderId);
}
