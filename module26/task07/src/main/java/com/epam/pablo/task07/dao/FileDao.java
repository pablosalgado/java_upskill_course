package com.epam.pablo.task07.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.epam.pablo.task07.model.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FileDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class FileMapper implements RowMapper<File> {
        public File mapRow(ResultSet rs, int rowNum) throws SQLException {
            File file = new File();
            file.setId(rs.getInt("id"));
            file.setFileName(rs.getString("file_name"));
            file.setFileType(rs.getString("file_type"));
            file.setFileSize(rs.getLong("file_size"));
            file.setFileData(rs.getBytes("file_data"));
            file.setOwnerId(rs.getInt("owner_id"));
            file.setCreatedAt(rs.getTimestamp("created_at"));
            return file;
        }
    }

    public List<File> findAll() {
        return jdbcTemplate.query(
            "CALL findAllFiles()", 
            new FileMapper()
        );
    }

    public File findById(int id) {
        return jdbcTemplate.queryForObject(
            "CALL findFileById(?)",
             new FileMapper(), 
             id
        );
    }

    public int create(File file) {
        return jdbcTemplate.update(
            "CALL createFile(?, ?, ?, ?, ?)",
            file.getFileName(), 
            file.getFileType(), 
            file.getFileSize(), 
            file.getFileData(), 
            file.getOwnerId()
        );
    }

    public int update(File file) {
        return jdbcTemplate.update(
            "CALL updateFile(?, ?, ?, ?, ?, ?)",
             file.getId(),
            file.getFileName(), 
            file.getFileType(), 
            file.getFileSize(), 
            file.getFileData(), 
            file.getOwnerId()
        );
    }

    public int delete(int id) {
        return jdbcTemplate.update(
            "CALL deleteFile(?)", 
            id
        );
    }
}