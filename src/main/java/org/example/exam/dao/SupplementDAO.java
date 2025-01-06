package org.example.exam.dao;

import org.example.exam.database.ConnectionDB;
import org.example.exam.models.Supplement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplementDAO {
    private Connection connection = ConnectionDB.getConnection();


    public void create(Supplement supplement) throws SQLException {
        String query = "INSERT INTO Supplement (nom, prix) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, supplement.getNom());
            stmt.setDouble(2, supplement.getPrix());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    supplement.setId(keys.getInt(1));
                }
            }
        }
    }

    public Supplement read(int id) throws SQLException {
        String query = "SELECT * FROM Supplement WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Supplement(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getDouble("prix")
                    );
                }
            }
        }
        return null;
    }

    public List<Supplement> readAll() throws SQLException {
        String query = "SELECT * FROM Supplement";
        List<Supplement> supplements = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                supplements.add(new Supplement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("prix")
                ));
            }
        }
        return supplements;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Supplement WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public List<Supplement> getSupplementsByRepasId(int repasId) {
        List<Supplement> supplements = new ArrayList<>();
        try {
            String sql = "SELECT s.* FROM Supplement s " +
                    "JOIN Repas_Supplement rs ON s.id = rs.supplement_id " +
                    "WHERE rs.repas_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, repasId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                supplements.add(new Supplement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("prix")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplements;
    }
}

