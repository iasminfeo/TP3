package TP2.View;

import TP2.Model.*;

import java.util.ArrayList;
import java.util.Scanner;

public class ViewAtor {
    public Scanner sc = new Scanner(System.in);

    public ViewAtor(Scanner sc) {
        this.sc = sc;
    }

    public Ator incluirAtor(int IDSerie) throws Exception {
        System.out.println("Inclusão de ator: ");
        String Nome = "";
        char Genero = ' ';
        boolean dadosCorretos = false;

        System.out.println("Nome:");
        Nome = sc.nextLine();

        do {
            dadosCorretos = false;
            System.out.println("Gênero: ");
            Genero = sc.nextLine().charAt(0);
            if (Genero == 'M' || Genero == 'F') {
                dadosCorretos = true;
            } else {
                System.err.println("Gênero inválido! Use 'M' ou 'F'.");
            }
        } while (!dadosCorretos);
        
        // Confirmar a criação
        System.out.println("\nConfirme os dados do Ator:");
        System.out.println("Nome: " + Nome);
        System.out.println("ID da Série: " + IDSerie);
        System.out.println("Gênero: " + Genero);

        System.out.print("\nConfirma a inclusão da série? (S/N) ");
        String resp = sc.nextLine().toUpperCase(); // Usar nextLine() para capturar a linha inteira
        System.out.println(resp);
        if (resp.isEmpty() || !(resp.equals("S") || resp.equals("N"))) {
            System.out.println("Resposta inválida. Por favor, digite 'S' para Sim ou 'N' para Não.");
            return null;
        }

        if(resp.equals("S")) {
            try {
                Ator A = new Ator(Nome, IDSerie, Genero);
                return A;
            } catch (Exception E){
                System.out.println("Erro do sistema. Não foi possível incluir o ator!" );
                return null;
            }
        } else{
            System.out.println("Criação de ator cancelada.");
            return null;
        }
    }

    public String lerNomeAtor(){
        String termo = sc.nextLine();
        return termo;
    }

    public int LerIDAtor(){
        System.out.print("Digite o ID do Ator: ");
        int id = sc.nextInt();
        sc.nextLine(); // Limpar buffer
        return id;
    }

    public void mostraResultadoBuscaAtores(ArrayList<Ator> atores){
        if(atores.isEmpty()){
            System.out.println("Nenhum ator encontrado com o nome informado.");
            return;
        }

        System.out.println("\n=== Atores Encontrados ===");
        System.out.println("Total: " + atores.size() + " ator(es) encontrado(s).");
        
        for (Ator ator : atores) {
            System.out.println("\nID: " + ator.getId() + " | Nome:" + ator.getNome() + " | Gênero: " + ator.getGenero());
        }
    }

    public Ator alterarAtor(int idSerie, Ator A){
        System.out.println("Alteração de ator:");
        if (A != null) {
            System.out.println("Ator Encontrado: ");
            mostrarAtor(A);

            System.out.println("\nDigite o novo nome do ator (ou deixe em branco para manter o anterior): ");
            String novoNome = sc.nextLine();
            if (!novoNome.isEmpty()) {
                A.setNome(novoNome);
            }

            System.out.println("\nDigite o novo gênero do ator (ou deixe em branco para manter o anterior): ");
            char novoGenero = sc.nextLine().charAt(0);
            if (novoGenero == 'M' || novoGenero == 'F') {
                A.setGenero(novoGenero);
            } else {
                System.err.println("Gênero inválido! Use 'M' ou 'F'.");
            }

            System.out.println("\nConfirme os dados do Ator:");
            System.out.println("Nome: " + A.getNome());
            System.out.println("ID da Série: " + idSerie);
            System.out.println("Gênero: " + A.getGenero());

             // Confirmação da alteração
             System.out.print("\nConfirma as alterações? (S/N) ");
             char resp = sc.next().charAt(0);
             if (resp == 'S' || resp == 's') {
                 // Salva as alterações no arquivo
                 return A;
             } else {
                 System.out.println("Alterações canceladas.");
                 return null;
             }
         }
         return null;
    }

    public int selecionaAtorDoResultado(ArrayList<Ator> Atores) {
        if (Atores.isEmpty()) {
            return -1;
        }

        if (Atores.size() == 1) {
            System.out.println("\nAtores selecionados automaticamente: " + Atores.get(0).getNome());
            return Atores.get(0).getId();
        }

        System.out.print("\nDigite o ID do ator que deseja selecionar (0 para cancelar): ");
        int id = sc.nextInt();
        sc.nextLine(); // Limpar buffer

        // Verificar se o ID está na lista
        if (id != 0) {
            boolean idExiste = false;
            for (Ator at : Atores) {
                if (at.getId() == id) {
                    idExiste = true;
                    break;
                }
            }

            if (!idExiste) {
                System.out.println("ID inválido! Por favor, selecione um ID da lista apresentada.");
                return selecionaAtorDoResultado(Atores); // Recursão para nova tentativa
            }
        }

        return id;
    }

    public void mostraListaAtores(ArrayList<Ator> Atores) {
        if (Atores.isEmpty()) {
            System.out.println("Nenhum ator encontrado.");
            return;
        }

        System.out.println("\n=== Lista de Atores ===");
        for (Ator ator : Atores) {
            System.out.println("ID: " + ator.getId() + " | Nome: " + ator.getNome() + " | Gênero: " + ator.getGenero());
        }
    }

    public void mostrarAtor(Ator A) {
        System.out.println("ID: " + A.getId());
        System.out.println("Nome: " + A.getNome());
        System.out.println("ID da Série: " + A.getIDSerie());
        System.out.println("Gênero: " + A.getGenero());
    }
}
