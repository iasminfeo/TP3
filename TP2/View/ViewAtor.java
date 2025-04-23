package TP2.View;

import TP2.Model.*;
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

    public void mostrarAtor(Ator A) {
        System.out.println("ID: " + A.getId());
        System.out.println("Nome: " + A.getNome());
        System.out.println("ID da Série: " + A.getIDSerie());
        System.out.println("Gênero: " + A.getGenero());
    }
}
