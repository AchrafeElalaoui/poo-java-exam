package org.example.exam.dao;

import org.example.exam.database.ConnectionDB;
import org.example.exam.models.Ingredient;
import org.example.exam.models.PlatPrincipal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatPrincipalDAO {
    private Connection connection = ConnectionDB.getConnection();


    public void create(PlatPrincipal platPrincipal) throws SQLException {
        String query = "INSERT INTO PlatPrincipal (nom, prix_base) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, platPrincipal.getNom());
            stmt.setDouble(2, platPrincipal.getPrixBase());
            stmt.executeUpdate();
            // Ajouter les ingrédients dans la table PlatPrincipal_Ingredient
            for (Ingredient ingredient : platPrincipal.getIngredients()) {
                addIngredient(platPrincipal.getId(), ingredient.getId());
            }
        }
    }

    private void addIngredient(int platId, int ingredientId) throws SQLException {
        String query = "INSERT INTO PlatPrincipal_Ingredient (plat_principal_id, ingredient_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, platId);
            stmt.setInt(2, ingredientId);
            stmt.executeUpdate();
        }
    }

    public PlatPrincipal read(int id) throws SQLException {
        String query = "SELECT * FROM PlatPrincipal WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<Ingredient> ingredients = getIngredients(id);
                    return new PlatPrincipal(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getDouble("prix_base"),
                            ingredients
                    );
                }
            }
        }
        return null;
    }

    private List<Ingredient> getIngredients(int platId) throws SQLException {
        String query = "SELECT i.* FROM Ingredient i JOIN PlatPrincipal_Ingredient pi ON i.id = pi.ingredient_id WHERE pi.plat_principal_id = ?";
        List<Ingredient> ingredients = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, platId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(new Ingredient(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getDouble("quantite"),
                            rs.getString("unite")
                    ));
                }
            }
        }
        return ingredients;
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM PlatPrincipal WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public PlatPrincipal getPlatPrincipalById(int platPrincipalId) {
        PlatPrincipal platPrincipal = null;
        try  {
            String sql = "SELECT * FROM PlatPrincipal WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, platPrincipalId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                IngredientDAO ingredientDAO = new IngredientDAO();
                List<Ingredient> ingredients = ingredientDAO.getIngredientsByPlatPrincipalId(platPrincipalId);

                platPrincipal = new PlatPrincipal(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("prix_base"),
                        ingredients
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return platPrincipal;
    }


    public List<PlatPrincipal> findAll() {
        List<PlatPrincipal> plats = new ArrayList<>();
        String sql = "SELECT id, nom, prix_base FROM PlatPrincipal";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                double prixBase = resultSet.getDouble("prix_base");

                // Charger les ingrédients associés au plat principal
                List<Ingredient> ingredients = getIngredientsByPlatPrincipalId(id);

                // Créer un objet PlatPrincipal
                PlatPrincipal plat = new PlatPrincipal(id, nom, prixBase, ingredients);
                plats.add(plat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plats;
    }

    private List<Ingredient> getIngredientsByPlatPrincipalId(int platPrincipalId) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT i.id, i.nom, pi.quantite, pi.unite " +
                "FROM Ingredient i " +
                "JOIN PlatPrincipal_Ingredient pi ON i.id = pi.ingredient_id " +
                "WHERE pi.plat_principal_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, platPrincipalId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Ingredient ingredient = new Ingredient(
                            resultSet.getInt("id"),
                            resultSet.getString("nom"),
                            resultSet.getDouble("quantite"),
                            resultSet.getString("unite")
                    );
                    ingredients.add(ingredient);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ingredients;
    }
}
