package org.example.exam.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Client {
    private int id;
    private String name;
    private List<Commande> commandes;

    public Client(int id, String name) {
        this.id = id;
        this.name = name;
        this.commandes = new ArrayList<>();
    }
    public void ajouterCommande(Commande commande) {
        commandes.add(commande);
    }

    public void supprimerCommande(Commande commande) {
        commandes.remove(commande);
    }


}
