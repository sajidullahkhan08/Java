package database;

import model.Disease;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiseaseDAO {
    public void save(Disease d) throws SQLException {
        String sql = "INSERT INTO Disease (Disease_Name, Disease_Description) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getDescription());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) d.setId(rs.getInt(1));
            }
        }
    }

    public List<Disease> getAll() throws SQLException {
        List<Disease> list = new ArrayList<>();
        String sql = "SELECT * FROM Disease ORDER BY Disease_Name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Disease d = new Disease();
                d.setId(rs.getInt("Disease_ID"));
                d.setName(rs.getString("Disease_Name"));
                d.setDescription(rs.getString("Disease_Description"));
                list.add(d);
            }
        }
        return list;
    }

    public Disease findById(int id) throws SQLException {
        String sql = "SELECT * FROM Disease WHERE Disease_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Disease d = new Disease();
                    d.setId(rs.getInt("Disease_ID"));
                    d.setName(rs.getString("Disease_Name"));
                    d.setDescription(rs.getString("Disease_Description"));
                    return d;
                }
            }
        }
        return null;
    }
}