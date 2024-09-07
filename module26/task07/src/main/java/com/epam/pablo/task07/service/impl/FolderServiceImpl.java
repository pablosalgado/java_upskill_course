package com.epam.pablo.task07.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.epam.pablo.task07.model.Folder;
import com.epam.pablo.task07.service.FolderService;
import com.epam.pablo.task07.dao.FolderDao;
import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    @Autowired
    private FolderDao folderDao;

    @Override
    public List<Folder> getAll() {
        return folderDao.findAll();
    }

    @Override
    public Folder getById(int folderId) {
        return folderDao.findById(folderId);
    }

    @Override
    public boolean create(Folder folder) {
        return folderDao.create(folder) > 0;
    }

    @Override
    public boolean update(Folder folder) {
        return folderDao.update(folder) > 0;
    }

    @Override
    public boolean delete(int folderId) {
        return folderDao.delete(folderId) > 0;
    }
}