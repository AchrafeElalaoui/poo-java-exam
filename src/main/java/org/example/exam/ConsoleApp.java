package org.example.exam;

import org.example.exam.dao.*;
import org.example.exam.models.*;

import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        ClientDAO clientDAO = new ClientDAO();
        CommandeDAO commandeDAO = new CommandeDAO();
        RepasDAO repasDAO = new RepasDAO();
        PlatPrincipalDAO platPrincipalDAO = new PlatPrincipalDAO();
        IngredientDAO ingredientDAO = new IngredientDAO();
        SupplementDAO supplementDAO = new SupplementDAO();

        boolean running = true;

        while (running) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Gérer les clients");
            System.out.println("2. Gérer les commandes");
            System.out.println("3. Gérer les repas");
            System.out.println("4. Gérer les plats principaux");
            System.out.println("5. Gérer les ingrédients");
            System.out.println("6. Gérer les suppléments");
            System.out.println("0. Quitter");
            System.out.print("Choisissez une option : ");
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consomme la nouvelle ligne

            switch (choix) {
                case 1:
                    gererClients(clientDAO);
                    break;
                case 2:
                    gererCommandes(commandeDAO, clientDAO);
                    break;
                case 3:
                    gererRepas(repasDAO, platPrincipalDAO, supplementDAO);
                    break;
                case 4:
                    gererPlatsPrincipaux(platPrincipalDAO, ingredientDAO);
                    break;
                case 5:
                    gererIngredients(ingredientDAO);
                    break;
                case 6:
                    gererSupplements(supplementDAO);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Option invalide. Réessayez.");
            }
        }
    }

    private static void gererClients(ClientDAO clientDAO) throws SQLException {
        System.out.println("\n=== Gestion des Clients ===");
        System.out.println("1. Ajouter un client");
        System.out.println("2. Voir tous les clients");
        System.out.println("3. Supprimer un client");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1:
                System.out.print("Entrez le nom du client : ");
                String name = scanner.nextLine();
                Client client = new Client(0, name); // L'ID sera auto-généré par la base de données
                clientDAO.add(client);
                System.out.println("Client ajouté avec succès !");
                break;
            case 2:
                List<Client> clients = clientDAO.readAll();
                System.out.println("Liste des clients :");
                for (Client c : clients) {
                    System.out.println("ID : " + c.getId() + ", Nom : " + c.getName());
                }
                break;
            case 3:
                System.out.print("Entrez l'ID du client à supprimer : ");
                int id = scanner.nextInt();
                clientDAO.delete(id);
                System.out.println("Client supprimé avec succès !");
                break;
            default:
                System.out.println("Option invalide.");
        }
    }

    private static void gererCommandes(CommandeDAO commandeDAO, ClientDAO clientDAO) throws SQLException {
        System.out.println("\n=== Gestion des Commandes ===");
        System.out.println("1. Ajouter une commande");
        System.out.println("2. Voir toutes les commandes");
        System.out.println("3. Supprimer une commande");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1:
                System.out.print("Entrez l'ID du client : ");
                int clientId = scanner.nextInt();
                scanner.nextLine();
                Client client = clientDAO.read(clientId);
                if (client != null) {
                    Commande commande = new Commande(0, client); // L'ID sera auto-généré par la base de données
                    commandeDAO.create(commande);
                    System.out.println("Commande ajoutée avec succès !");
                } else {
                    System.out.println("Client introuvable.");
                }
                break;
            case 2:
                List<Commande> commandes = commandeDAO.readAll();
                System.out.println("Liste des commandes :");
                for (Commande c : commandes) {
                    System.out.println("ID : " + c.getId() + ", Client : " + c.getClient().getName());
                }
                break;
            case 3:
                System.out.print("Entrez l'ID de la commande à supprimer : ");
                int commandeId = scanner.nextInt();
                commandeDAO.delete(commandeId);
                System.out.println("Commande supprimée avec succès !");
                break;
            default:
                System.out.println("Option invalide.");
        }
    }

    private static void gererRepas(RepasDAO repasDAO, PlatPrincipalDAO platPrincipalDAO, SupplementDAO supplementDAO) throws SQLException {
        System.out.println("\n=== Gestion des Repas ===");
        System.out.println("1. Ajouter un repas");
        System.out.println("2. Voir tous les repas");
        System.out.println("3. Supprimer un repas");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1:
                System.out.print("Entrez l'ID du plat principal : ");
                int platPrincipalId = scanner.nextInt();
                scanner.nextLine();
                PlatPrincipal platPrincipal = platPrincipalDAO.read(platPrincipalId);
                if (platPrincipal == null) {
                    System.out.println("Plat principal introuvable.");
                    break;
                }

                System.out.print("Combien de suppléments voulez-vous ajouter ? ");
                int nbSupplements = scanner.nextInt();
                scanner.nextLine();
                List<Supplement> supplements = new ArrayList<>();
                for (int i = 0; i < nbSupplements; i++) {
                    System.out.print("Entrez l'ID du supplément " + (i + 1) + " : ");
                    int supplementId = scanner.nextInt();
                    scanner.nextLine();
                    Supplement supplement = supplementDAO.read(supplementId);
                    if (supplement != null) {
                        supplements.add(supplement);
                    } else {
                        System.out.println("Supplément introuvable. Ignoré.");
                    }
                }

                Repas repas = new Repas(0, platPrincipal, supplements);
                repasDAO.create(repas);
                System.out.println("Repas ajouté avec succès !");
                break;
            case 2:
                List<Repas> repasList = repasDAO.findAll();
                System.out.println("Liste des repas :");
                for (Repas r : repasList) {
                    System.out.println("ID : " + r.getId() + ", Plat principal : " + r.getPlatPrincipal().getNom());
                }
                break;
            case 3:
                System.out.print("Entrez l'ID du repas à supprimer : ");
                int repasId = scanner.nextInt();
                repasDAO.delete(repasId);
                System.out.println("Repas supprimé avec succès !");
                break;
            default:
                System.out.println("Option invalide.");
        }
    }

    private static void gererPlatsPrincipaux(PlatPrincipalDAO platPrincipalDAO, IngredientDAO ingredientDAO) throws SQLException {
        System.out.println("\n=== Gestion des Plats Principaux ===");
        System.out.println("1. Ajouter un plat principal");
        System.out.println("2. Voir tous les plats principaux");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1:
                System.out.print("Entrez le nom du plat principal : ");
                String nom = scanner.nextLine();
                System.out.print("Entrez le prix de base : ");
                double prixBase = scanner.nextDouble();
                scanner.nextLine();

                PlatPrincipal platPrincipal = new PlatPrincipal(0, nom, prixBase, new ArrayList<>());
                platPrincipalDAO.create(platPrincipal);
                System.out.println("Plat principal ajouté avec succès !");
                break;
            case 2:
                List<PlatPrincipal> platsPrincipaux = platPrincipalDAO.findAll();
                System.out.println("Liste des plats principaux :");
                for (PlatPrincipal p : platsPrincipaux) {
                    System.out.println("ID : " + p.getId() + ", Nom : " + p.getNom() + ", Prix : " + p.calculerPrix());
                }
                break;
            default:
                System.out.println("Option invalide.");
        }
    }

    private static void gererIngredients(IngredientDAO ingredientDAO) throws SQLException {
        System.out.println("\n=== Gestion des Ingrédients ===");
        System.out.println("1. Ajouter un ingrédient");
        System.out.println("2. Voir tous les ingrédients");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1:
                System.out.print("Entrez le nom de l'ingrédient : ");
                String nom = scanner.nextLine();
                System.out.print("Entrez la quantité : ");
                double quantite = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Entrez l'unité : ");
                String unite = scanner.nextLine();

                Ingredient ingredient = new Ingredient(0, nom, quantite, unite);
                ingredientDAO.create(ingredient);
                System.out.println("Ingrédient ajouté avec succès !");
                break;
            case 2:
                List<Ingredient> ingredients = ingredientDAO.readAll();
                System.out.println("Liste des ingrédients :");
                for (Ingredient i : ingredients) {
                    System.out.println("ID : " + i.getId() + ", Nom : " + i.getNom() +
                            ", Quantité : " + i.getQuantite() + " " + i.getUnite());
                }
                break;
            default:
                System.out.println("Option invalide.");
        }
    }

    private static void gererSupplements(SupplementDAO supplementDAO) throws SQLException {
        System.out.println("\n=== Gestion des Suppléments ===");
        System.out.println("1. Ajouter un supplément");
        System.out.println("2. Voir tous les suppléments");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1:
                System.out.print("Entrez le nom du supplément : ");
                String nom = scanner.nextLine();
                System.out.print("Entrez le prix : ");
                double prix = scanner.nextDouble();
                scanner.nextLine();

                Supplement supplement = new Supplement(0, nom, prix);
                supplementDAO.create(supplement);
                System.out.println("Supplément ajouté avec succès !");
                break;
            case 2:
                List<Supplement> supplements = supplementDAO.readAll();
                System.out.println("Liste des suppléments :");
                for (Supplement s : supplements) {
                    System.out.println("ID : " + s.getId() + ", Nom : " + s.getNom() + ", Prix : " + s.getPrix());
                }
                break;
            default:
                System.out.println("Option invalide.");
        }
    }
}

//
//public class ConsoleApp {
//    public static void afficherTicket(Client client) {
//        System.out.println("Bienvenue " + client.getName());
//        System.out.println("--------------------------------");
//        System.out.println("-----------------TICKET-----------------");
//        System.out.println("Nom: " + client.getName());
//        System.out.println();
//
//        CommandeDAO commandeDAO = new CommandeDAO();
//        List<Commande> commandes = commandeDAO.getCommandesByClientId(client.getId());
//
//        for (Commande commande : commandes) {
//            System.out.println("nombre de repas: " + commande.getRepas().size());
//            int index = 1;
//            for (Repas repas : commande.getRepas()) {
//                System.out.println("Repas N°:" + index + ": " + repas.getPlatPrincipal().getNom());
//                System.out.println("Ingrédient:");
//
//                for (Ingredient ingredient : repas.getPlatPrincipal().getIngredients()) {
//                    System.out.println(ingredient.getNom() + ": " + ingredient.getQuantite() + " " + ingredient.getUnite());
//                }
//
//                System.out.println("Suppléments:");
//                for (Supplement supplement : repas.getSupplements()) {
//                    System.out.println(supplement.getNom() + ": " + supplement.getPrix());
//                }
//
//                System.out.println("*****");
//                index++;
//            }
//            System.out.println("--------Total: " + String.format("%.2f", commande.calculerTotal()) + "--------");
//        }
//    }
//
//    public static void main(String[] args) throws SQLException {
//        // Initialiser les DAO
//        ClientDAO clientDAO = new ClientDAO();
//
//        Scanner in=new Scanner(System.in);
//        String name =in.nextLine();
//        Client c =new Client();
//        c.setName(name);
//        clientDAO.add(c);
//
//        // Récupérer le client par ID
//        int clientId = 1; // ID du client dans la base de données
//        Client client = clientDAO.read(clientId);
//
//        if (client != null) {
//            afficherTicket(client);
//        } else {
//            System.out.println("Client non trouvé !");
//        }
//    }
//}

//
//public class ConsoleApp {
//    public static void afficherTicket(Client client) {
//        System.out.println("Bienvenue " + client.getName());
//        System.out.println("--------------------------------");
//        System.out.println("-----------------TICKET-----------------");
//        System.out.println("Nom: " + client.getName());
//        System.out.println();
//
//        List<Commande> commandes = client.getCommandes();
//        for (Commande commande : commandes) {
//            System.out.println("nombre de repas: " + commande.getRepas().size());
//            int index = 1;
//            for (Repas repas : commande.getRepas()) {
//                System.out.println("Repas N°:" + index + ": " + repas.getPlatPrincipal().getNom());
//                System.out.println("Ingrédient:");
//                for (Ingredient ingredient : repas.getPlatPrincipal().getIngredients()) {
//                    System.out.println(ingredient.getNom() + ": " + ingredient.getQuantite() + " " + ingredient.getUnite());
//                }
//                System.out.println("Suppléments:");
//                for (Supplement supplement : repas.getSupplements()) {
//                    System.out.println(supplement.getNom() + ": " + supplement.getPrix());
//                }
//                System.out.println("*****");
//                index++;
//            }
//            System.out.println("--------Total: " + String.format("%.2f", commande.calculerTotal()) + "--------");
//        }
//    }
//
//    public static void main(String[] args) {
//        // Créer des ingrédients
//        Ingredient viande = new Ingredient(1, "Viande", 250, "gramme");
//        Ingredient pruneaux = new Ingredient(2, "Pruneaux", 1, "gramme");
//        Ingredient poisson = new Ingredient(3, "Poisson", 250, "gramme");
//        Ingredient carotte = new Ingredient(4, "Carotte", 1, "gramme");
//        Ingredient pommeDeTerre = new Ingredient(5, "Pomme de terre", 1, "gramme");
//        Ingredient olive = new Ingredient(6, "Olive", 1, "gramme");
//
//        // Créer des plats principaux
//        PlatPrincipal tajineViande = new PlatPrincipal(1, "Tajine de viande & Pruneaux", 100, List.of(viande, pruneaux));
//        PlatPrincipal tajinePoulet = new PlatPrincipal(2, "Tajine de poulet & légumes", 120, List.of(poisson, carotte, pommeDeTerre, olive));
//
//        // Créer des suppléments
//        Supplement frites = new Supplement(1, "Frites", 11);
//        Supplement boisson = new Supplement(2, "Boisson", 12);
//        Supplement jusOrange = new Supplement(3, "Jus d'orange", 13);
//        Supplement saladeMarocaine = new Supplement(4, "Salade marocaine", 14);
//
//        // Créer des repas
//        Repas repas1 = new Repas(1, tajineViande, List.of(frites, boisson));
//        Repas repas2 = new Repas(2, tajinePoulet, List.of(jusOrange, saladeMarocaine));
//
//        // Créer une commande
//        Commande commande = new Commande(1, null);
//        commande.ajouterRepas(repas1);
//        commande.ajouterRepas(repas2);
//
//        // Créer un client
//        Client client = new Client(1, "Ali baba");
//        client.ajouterCommande(commande);
//
//        // Afficher le ticket
//        afficherTicket(client);
//    }
//}

