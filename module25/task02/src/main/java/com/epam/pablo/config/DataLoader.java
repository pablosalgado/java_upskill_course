package com.epam.pablo.config;

import com.epam.pablo.entity.User;
import com.epam.pablo.entity.impl.UserImpl;
import com.epam.pablo.storage.impl.StorageImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class DataLoader implements InitializingBean {
    Logger logger = Logger.getLogger(DataLoader.class.getName());

    @Value("${data.file.path}")
    private Resource dataFilePath;

    @Autowired
    private StorageImpl storage;

    @Override
    public void afterPropertiesSet() throws Exception {
        var data = readDataFromFile(dataFilePath.getFile());

        logger.info("Initializing storage from JSON content");
        data.stream().forEach(user -> storage.save(user,  User.class));
    }

    private List<User> readDataFromFile(File file) throws IOException {
        var mapper = new ObjectMapper();
        return mapper.readValue(
                file,
                mapper.getTypeFactory().constructCollectionType(List.class, UserImpl.class)
        );
    }
}