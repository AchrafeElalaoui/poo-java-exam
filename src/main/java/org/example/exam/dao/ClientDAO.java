package org.example.exam.dao;

import org.example.exam.database.ConnectionDB;
import org.example.exam.models.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private Connection connection =ConnectionDB.getConnection();


    public void add(Client client) throws SQLException {
        String query = "INSERT INTO Client (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, client.getName());
            stmt.executeUpdate();
        }
    }

    public Client read(int id) throws SQLException {
        String query = "SELECT * FROM Client WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null;
    }

    public List<Client> readAll() throws SQLException {
        String query = "SELECT * FROM Client";
        List<Client> clients = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                clients.add(new Client(rs.getInt("id"), rs.getString("name")));
            }
        }
        return clients;
    }

    public void update(Client client) throws SQLException {
        String query = "UPDATE Client SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, client.getName());
            stmt.setInt(2, client.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Client WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
