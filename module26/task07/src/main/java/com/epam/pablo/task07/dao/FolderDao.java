package com.epam.pablo.task07.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.epam.pablo.task07.model.Folder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FolderDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class FolderMapper implements RowMapper<Folder> {
        public Folder mapRow(ResultSet rs, int rowNum) throws SQLException {
            Folder folder = new Folder();
            folder.setFolderId(rs.getInt("folder_id"));
            folder.setFolderName(rs.getString("folder_name"));
            folder.setOwnerId(rs.getInt("owner_id"));
            folder.setCreatedAt(rs.getTimestamp("created_at"));
            return folder;
        }
    }

    public List<Folder> findAll() {
        String sql = "SELECT * FROM folders";
        return jdbcTemplate.query(sql, new FolderMapper());
    }

    public Folder findById(int folderId) {
        String sql = "SELECT * FROM folders WHERE folder_id = ?";
        return jdbcTemplate.queryForObject(sql, new FolderMapper(), folderId);
    }

    public int create(Folder folder) {
        String sql = "INSERT INTO folders (folder_name, owner_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, folder.getFolderName(), folder.getOwnerId());
    }

    public int update(Folder folder) {
        String sql = "UPDATE folders SET folder_name = ?, owner_id = ? WHERE folder_id = ?";
        return jdbcTemplate.update(sql, folder.getFolderName(), folder.getOwnerId(), folder.getFolderId());
    }

    public int delete(int folderId) {
        String sql = "DELETE FROM folders WHERE folder_id = ?";
        return jdbcTemplate.update(sql, folderId);
    }
}