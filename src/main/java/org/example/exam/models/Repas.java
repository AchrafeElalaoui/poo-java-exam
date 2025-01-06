package org.example.exam.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repas {
    private int id;
    private PlatPrincipal platPrincipal;
    private List<Supplement> supplements;

    public double calculerTotal() {
        double totalSupplements = supplements.stream().mapToDouble(Supplement::getPrix).sum();
        return platPrincipal.calculerPrix() + totalSupplements;
    }
}
