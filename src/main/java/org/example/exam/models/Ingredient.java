package org.example.exam.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    private int id;
    private String nom;
    private double quantite;
    private String unite;
    public double calculerPrixIngredient() {
        return quantite * 10.0;// Assume unit price = 10.0
    }
}
