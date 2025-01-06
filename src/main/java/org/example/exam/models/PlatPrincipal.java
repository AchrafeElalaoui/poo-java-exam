package org.example.exam.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatPrincipal {
    private int id;
    private String nom;
    private double prixBase;
    private List<Ingredient> ingredients;

    public double calculerPrix() {
        double prixIngredients = ingredients.stream().mapToDouble(Ingredient::calculerPrixIngredient).sum();
        return prixBase + prixIngredients;
    }
}
