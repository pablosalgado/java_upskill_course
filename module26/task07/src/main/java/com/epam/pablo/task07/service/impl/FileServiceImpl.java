package com.epam.pablo.task07.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.epam.pablo.task07.dao.FileDao;
import com.epam.pablo.task07.model.File;
import com.epam.pablo.task07.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    private FileDao fileDao;

    public FileServiceImpl(FileDao fileDao) {
        this.fileDao = fileDao;
    }
    
    @Override
    public List<File> getAll() {
        return fileDao.findAll();
    }

    @Override
    public File getById(int fileId) {
        return fileDao.findById(fileId);
    }

    @Override
    public boolean create(File File) {
        return fileDao.create(File) > 0;
    }

    @Override
    public boolean update(File File) {
        return fileDao.update(File) > 0;
    }

    @Override
    public boolean delete(int FileId) {
        return fileDao.delete(FileId) > 0;
    }
}
