package TP2.App;

import TP2.Menu.MenuAtor;
import TP2.Menu.MenuEpisodio;
import TP2.Menu.MenuSerie;
import TP2.Model.*;
import TP2.Service.*;
import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static Arquivo<Episodio> arqEpisodios;
    static Arquivo<Serie> arqSerie;
    static Arquivo<Ator> arqAtores;

    public static void main(String[] args) throws Exception {
        Scanner sc;

        try {
            sc = new Scanner(System.in);
            int opcao;
            
            //incializar os arquivos
            arqEpisodios = new Arquivo<>("Episodios", Episodio.class.getConstructor());
            arqSerie = new Arquivo<>("Serie", Serie.class.getConstructor());
            arqAtores = new Arquivo<>("Atores", Ator.class.getConstructor());

            // Inicializa os controladores
            MenuSerie menuSerie = new MenuSerie(sc, arqSerie, arqEpisodios, arqAtores);
            MenuEpisodio menuEp = new MenuEpisodio(sc, arqEpisodios, arqSerie);
            MenuAtor menuAt = new MenuAtor(sc, arqAtores, arqSerie);

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
                        menuAt.menuAtor();
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
