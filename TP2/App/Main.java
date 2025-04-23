package TP1.App;

import TP1.Menu.MenuEpisodio;
import TP1.Menu.MenuSerie;
import TP1.Model.*;
import TP1.Service.*;
import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static Arquivo<Episodio> arqEpisodios;
    static Arquivo<Serie> arqSerie;

    public static void main(String[] args) throws Exception {
        Scanner sc;

        try {
            sc = new Scanner(System.in);
            int opcao;
            
            //incializar os arquivos
            arqEpisodios = new Arquivo<>("Episodios", Episodio.class.getConstructor());
            arqSerie = new Arquivo<>("Serie", Serie.class.getConstructor());

            // Inicializa os controladores
            MenuSerie menuSerie = new MenuSerie(sc, arqSerie, arqEpisodios);
            MenuEpisodio menuEp = new MenuEpisodio(sc, arqEpisodios, arqSerie);
            do {

                System.out.println("\n\nAEDsIII");
                System.out.println("-------");
                System.out.println("> Início");
                System.out.println("1 - Série");
                System.out.println("2 - Episódio");
                System.out.println("3 - Atores");
                System.out.println("0 - Sair");
                System.out.print("\nOpção: ");
                try {
                    opcao = Integer.valueOf(sc.nextLine());
                } catch (NumberFormatException e) {
                    opcao = -1;
                }

                switch (opcao) {
                    case 1:
                        menuSerie.menuSerie();
                        break;
                    case 2:
                        menuEp.menuEpisodio();
                        break;
                    case 3:
                        System.out.println("Ainda não implementado.");
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }

            } while (opcao != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
