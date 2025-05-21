package View;

import Model.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ViewAtor {
    public Scanner sc = new Scanner(System.in);

    public ViewAtor(Scanner sc) {
        this.sc = sc;
    }

    // -------------------------------
    // Operações de Link e Deslink
    // -------------------------------
    public void linkarAtorSerie(int idAtor, int idSerie) {
        System.out.println("Linkar Ator à Série: ");
        System.out.println("ID do Ator: " + idAtor);
        System.out.println("ID da Série: " + idSerie);

        System.out.print("\nConfirma a inclusão do ator na série? (S/N) ");
        String resp = sc.nextLine().toUpperCase();
        if (resp.equals("S")) {
            System.out.println("Ator vinculado à série com sucesso!");
        } else {
            System.out.println("Vinculação de ator à série cancelada.");
        }
    }

    public void deslinkarAtorSerie(int idAtor, int idSerie) {
        System.out.println("Desvincular Ator da Série: ");
        System.out.println("ID do Ator: " + idAtor);
        System.out.println("ID da Série: " + idSerie);

        System.out.print("\nConfirma a desvinculação do ator da série? (S/N) ");
        String resp = sc.nextLine().toUpperCase();
        if (resp.equals("S")) {
            System.out.println("Ator desvinculado da série com sucesso!");
        } else {
            System.out.println("Desvinculação de ator da série cancelada.");
        }
    }

    // -------------------------------
    // CRUD - Inclusão
    // -------------------------------
    public Ator incluirAtor(int IDSerie) {
        System.out.println("Inclusão de ator:");
        System.out.print("Nome: ");
        String nome = sc.nextLine();

        char genero;
        do {
            System.out.print("Gênero (M/F): ");
            genero = sc.nextLine().toUpperCase().charAt(0);
            if (genero != 'M' && genero != 'F') {
                System.out.println("Gênero inválido. Digite 'M' ou 'F'.");
            }
        } while (genero != 'M' && genero != 'F');

        System.out.println("\nConfirme os dados do Ator:");
        System.out.println("Nome: " + nome);
        System.out.println("ID da Série: " + IDSerie);
        System.out.println("Gênero: " + genero);

        System.out.print("Confirma a inclusão? (S/N): ");
        String resp = sc.nextLine().toUpperCase();
        if (resp.equals("S")) {
            return new Ator(nome, IDSerie, genero);
        } else {
            System.out.println("Criação de ator cancelada.");
            return null;
        }
    }

    // -------------------------------
    // CRUD - Alteração
    // -------------------------------
    public Ator alterarAtor(int idSerie, Ator ator) {
        System.out.println("Alteração de ator:");
        mostrarAtor(ator);

        System.out.print("Novo nome (Enter para manter): ");
        String novoNome = sc.nextLine();
        if (!novoNome.isEmpty()) {
            ator.setNome(novoNome);
        }

        System.out.print("Novo gênero (M/F ou Enter para manter): ");
        String generoInput = sc.nextLine().toUpperCase();
        if (!generoInput.isEmpty()) {
            char novoGenero = generoInput.charAt(0);
            if (novoGenero == 'M' || novoGenero == 'F') {
                ator.setGenero(novoGenero);
            } else {
                System.out.println("Gênero inválido. Mantendo o anterior.");
            }
        }

        System.out.println("\nConfirme os novos dados:");
        mostrarAtor(ator);

        System.out.print("Confirma as alterações? (S/N): ");
        char resp = sc.next().toUpperCase().charAt(0);
        sc.nextLine();
        if (resp == 'S') {
            return ator;
        } else {
            System.out.println("Alterações canceladas.");
            return null;
        }
    }

    // -------------------------------
    // Entrada de dados
    // -------------------------------
    public String lerNomeAtor() {
        System.out.print("Digite o nome do ator: ");
        return sc.nextLine();
    }

    public int LerIDAtor() {
        System.out.print("Digite o ID do ator: ");
        int id = sc.nextInt();
        sc.nextLine();
        return id;
    }

    // -------------------------------
    // Exibição dos resultados
    // -------------------------------
    public void mostraResultadoBuscaAtores(ArrayList<Ator> atores) {
        if (atores.isEmpty()) {
            System.out.println("\nNenhum ator encontrado com o termo informado.");
            return;
        }

        System.out.println("\n=== Atores Encontrados ===");
        System.out.println("Total: " + atores.size() + " ator(es).");

        for (Ator ator : atores) {
            System.out.println("\nID: " + ator.getId() + " | Nome: " + ator.getNome() + " | Gênero: " + ator.getGenero());
        }
    }

    public void mostraResultadoBuscaSeries(ArrayList<Serie> series) {
        if (series.isEmpty()) {
            System.out.println("Nenhuma série encontrada.");
            return;
        }

        System.out.println("\n=== Séries Encontradas ===");
        System.out.println("Total: " + series.size() + " série(s).");

        for (Serie serie : series) {
            System.out.println("\nID: " + serie.getId() + " | Nome: " + serie.getNome());
        }
    }

    public void mostraListaAtores(ArrayList<Ator> atores) {
        if (atores.isEmpty()) {
            System.out.println("Nenhum ator encontrado.");
            return;
        }

        System.out.println("\n=== Lista de Atores ===");
        for (Ator ator : atores) {
            System.out.println("ID: " + ator.getId() + " | Nome: " + ator.getNome() + " | Gênero: " + ator.getGenero());
        }
    }

    public void mostrarAtor(Ator ator) {
        System.out.println("\n=== Dados do Ator ===");
        System.out.println("ID: " + ator.getId());
        System.out.println("Nome: " + ator.getNome());
        System.out.println("Gênero: " + ator.getGenero());
    }

    // -------------------------------
    // Seleção de ID a partir da busca
    // -------------------------------
    public int selecionaAtorDoResultado(ArrayList<Ator> atores) {
        if (atores.isEmpty()) {
            return -1;
        }

        if (atores.size() == 1) {
            System.out.println("\nAtor selecionado automaticamente: " + atores.get(0).getNome());
            return atores.get(0).getId();
        }

        System.out.print("\nDigite o ID do ator que deseja selecionar (0 para cancelar): ");
        int id = sc.nextInt();
        sc.nextLine();

        boolean idExiste = false;
        for (Ator at : atores) {
            if (at.getId() == id) {
                idExiste = true;
                break;
            }
        }

        if (!idExiste) {
            System.out.println("ID inválido! Por favor, selecione um ID da lista apresentada.");
            return selecionaAtorDoResultado(atores);
        }

        return id;
    }
}
