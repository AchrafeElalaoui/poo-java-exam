package org.example.exam.dao;
import org.example.exam.database.ConnectionDB;
import org.example.exam.models.Ingredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO {
    private Connection connection = ConnectionDB.getConnection();


    public void create(Ingredient ingredient) throws SQLException {
        String query = "INSERT INTO Ingredient (nom, quantite, unite) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ingredient.getNom());
            stmt.setDouble(2, ingredient.getQuantite());
            stmt.setString(3, ingredient.getUnite());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    ingredient.setId(keys.getInt(1));
                }
            }
        }
    }

    public Ingredient read(int id) throws SQLException {
        String query = "SELECT * FROM Ingredient WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Ingredient(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getDouble("quantite"),
                            rs.getString("unite")
                    );
                }
            }
        }
        return null;
    }

    public List<Ingredient> readAll() throws SQLException {
        String query = "SELECT * FROM Ingredient";
        List<Ingredient> ingredients = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("quantite"),
                        rs.getString("unite")
                ));
            }
        }
        return ingredients;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM Ingredient WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

    }

    public List<Ingredient> getIngredientsByPlatPrincipalId(int platPrincipalId) {
        List<Ingredient> ingredients = new ArrayList<>();
        try  {
            String sql = "SELECT i.* FROM Ingredient i " +
                    "JOIN PlatPrincipal_Ingredient pi ON i.id = pi.ingredient_id " +
                    "WHERE pi.plat_principal_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, platPrincipalId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ingredient ingredient = new Ingredient(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("quantite"),
                        rs.getString("unite")
                );
                ingredients.add(ingredient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ingredients;
    }
}

