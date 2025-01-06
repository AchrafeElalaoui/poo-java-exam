package org.example.exam.dao;

import org.example.exam.database.ConnectionDB;
import org.example.exam.models.PlatPrincipal;
import org.example.exam.models.Repas;
import org.example.exam.models.Supplement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepasDAO {
    private Connection connection= ConnectionDB.getConnection();



    public void create(Repas repas) throws SQLException {
        String query = "INSERT INTO Repas (plat_principal_id) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, repas.getPlatPrincipal().getId());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    repas.setId(keys.getInt(1));
                }
            }

            // Ajouter les suppléments dans la table Repas_Supplement
            for (Supplement supplement : repas.getSupplements()) {
                addSupplement(repas.getId(), supplement.getId());
            }
        }
    }

    private void addSupplement(int repasId, int supplementId) throws SQLException {
        String query = "INSERT INTO Repas_Supplement (repas_id, supplement_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, repasId);
            stmt.setInt(2, supplementId);
            stmt.executeUpdate();
        }
    }
    public List<Repas> findAll() {
        List<Repas> repasList = new ArrayList<>();
        String sql = "SELECT r.id AS repas_id, pp.id AS plat_principal_id, pp.nom AS plat_nom, pp.prix_base AS plat_prix_base " +
                "FROM Repas r " +
                "JOIN PlatPrincipal pp ON r.plat_principal_id = pp.id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int repasId = resultSet.getInt("repas_id");

                // Charger le plat principal
                PlatPrincipal platPrincipal = new PlatPrincipal(
                        resultSet.getInt("plat_principal_id"),
                        resultSet.getString("plat_nom"),
                        resultSet.getDouble("plat_prix_base"),
                        new ArrayList<>()
                );

                // Charger les suppléments associés au repas
                List<Supplement> supplements = getSupplementsByRepasId(repasId);

                // Créer l'objet Repas
                Repas repas = new Repas(repasId, platPrincipal, supplements);

                repasList.add(repas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repasList;
    }
    private List<Supplement> getSupplementsByRepasId(int repasId) {
        List<Supplement> supplements = new ArrayList<>();
        String sql = "SELECT s.id, s.nom, s.prix " +
                "FROM Supplement s " +
                "JOIN Repas_Supplement rs ON s.id = rs.supplement_id " +
                "WHERE rs.repas_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, repasId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Supplement supplement = new Supplement(
                            resultSet.getInt("id"),
                            resultSet.getString("nom"),
                            resultSet.getDouble("prix")
                    );
                    supplements.add(supplement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplements;
    }

    public Repas read(int id) throws SQLException {
        String query = "SELECT * FROM Repas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PlatPrincipalDAO platDAO = new PlatPrincipalDAO();
                    PlatPrincipal plat = platDAO.read(rs.getInt("plat_principal_id"));

                    List<Supplement> supplements = getSupplements(id);
                    return new Repas(id, plat, supplements);
                }
            }
        }
        return null;
    }

    private List<Supplement> getSupplements(int repasId) throws SQLException {
        String query = "SELECT s.* FROM Supplement s JOIN Repas_Supplement rs ON s.id = rs.supplement_id WHERE rs.repas_id = ?";
        List<Supplement> supplements = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, repasId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    supplements.add(new Supplement(rs.getInt("id"), rs.getString("nom"), rs.getDouble("prix")));
                }
            }
        }
        return supplements;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Repas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public List<Repas> getRepasByCommandeId(int commandeId) {
        List<Repas> repasList = new ArrayList<>();
        try  {
            String sql = "SELECT * FROM Repas WHERE commande_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, commandeId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int repasId = rs.getInt("id");
                PlatPrincipalDAO platPrincipalDAO = new PlatPrincipalDAO();
                PlatPrincipal platPrincipal = platPrincipalDAO.getPlatPrincipalById(rs.getInt("plat_principal_id"));

                SupplementDAO supplementDAO = new SupplementDAO();
                List<Supplement> supplements = supplementDAO.getSupplementsByRepasId(repasId);

                Repas repas = new Repas(repasId, platPrincipal, supplements);
                repasList.add(repas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repasList;
    }
}

