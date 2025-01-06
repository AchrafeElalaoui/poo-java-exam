package org.example.exam.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {
    private int id;
    private Client client;
    private List<Repas> repas;
    public Commande(int id, Client client) {
        this.id = id;
        this.client = client;
        this.repas = new ArrayList<>();
    }
    public void ajouterRepas(Repas repas) {
        this.repas.add(repas);
    }

    public void supprimerRepas(Repas repas) {
        this.repas.remove(repas);
    }

    public double calculerTotal() {
        return repas.stream().mapToDouble(Repas::calculerTotal).sum();
    }
}
