package org.example.exam.dao;

import org.example.exam.database.ConnectionDB;
import org.example.exam.models.Client;
import org.example.exam.models.Commande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {
    private Connection connection = ConnectionDB.getConnection();

    public void create(Commande commande) throws SQLException {
        String query = "INSERT INTO Commande (client_id) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, commande.getClient().getId());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    commande.setId(keys.getInt(1));
                }
            }
        }
    }

    public Commande read(int id) throws SQLException {
        String query = "SELECT * FROM Commande WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int clientId = rs.getInt("client_id");
                    ClientDAO clientDAO = new ClientDAO();
                    Client client = clientDAO.read(clientId);
                    return new Commande(rs.getInt("id"), client);
                }
            }
        }
        return null;
    }

    public List<Commande> readAll() throws SQLException {
        String query = "SELECT * FROM Commande";
        List<Commande> commandes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int clientId = rs.getInt("client_id");
                ClientDAO clientDAO = new ClientDAO();
                Client client = clientDAO.read(clientId);
                commandes.add(new Commande(rs.getInt("id"), client));
            }
        }
        return commandes;
    }

    public void update(Commande commande) throws SQLException {
        String query = "UPDATE Commande SET client_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, commande.getClient().getId());
            stmt.setInt(2, commande.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Commande WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public List<Commande> getCommandesByClientId(int clientId) {
        List<Commande> commandes = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Commande WHERE client_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, clientId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("id"),
                        null // Le client sera ajouté séparément
                );

                RepasDAO repasDAO = new RepasDAO();
                commande.getRepas().addAll(repasDAO.getRepasByCommandeId(commande.getId()));

                commandes.add(commande);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commandes;
    }
}

