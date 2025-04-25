package TP2.Menu;

import java.util.ArrayList;
import java.util.Scanner;

import TP2.Model.Ator;
import TP2.Model.Episodio;
import TP2.Model.Serie;
import TP2.Service.Arquivo;
import TP2.Service.RelacionamentoSerieEpisodio;
import TP2.View.ViewAtor;
import TP2.View.ViewSerie;

public class MenuAtor {
    public Scanner sc = new Scanner(System.in);
    Arquivo<Ator> arqAtor;
    Arquivo<Serie> arqSerie;
    ViewAtor viewAtor;
    ViewSerie viewSerie;
    RelacionamentoSerieEpisodio relacionamento;

    public MenuAtor(Scanner sc, Arquivo<Ator> arqAtor, Arquivo<Serie> arqSerie) throws Exception {
        this.sc = sc;
        this.arqAtor = arqAtor;
        this.arqSerie = arqSerie;
        this.viewAtor = new ViewAtor(sc);
        this.viewSerie = new ViewSerie(sc);
        this.relacionamento = new RelacionamentoSerieAtor(arqSerie, arqAtor);
    }

    public void menuAtor() throws Exception {
        int opcao;
        do {
             System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Episódio");
            System.out.println("\n1 - Listar séries");
            System.out.println("2 - Gerenciar atores de uma série");
            System.out.println("3 - Buscar atores");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    listarSeries(); // check
                    break;
                case 2:
                    gerenciarAtorPorNome(); //check
                    break;
                case 3:
                    buscarAtorPorNome();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);
    }
    
    private void buscarAtorPorNome() throws Exception {
        String termoBusca = viewAtor.lerNomeAtor();
        
        if (termoBusca.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }
        
        // Realizar a busca em todos os episódios
        ArrayList<Ator> resultados = relacionamento.buscarAtorPorNome(termoBusca);
        
        // Exibir resultados
        viewAtor.mostraResultadoBuscaAtores(resultados);
        
        // Se houver resultados, perguntar se quer selecionar um episódio
        if (!resultados.isEmpty()) {
            System.out.print("\nDeseja selecionar um Ator para ver mais detalhes? (S/N): ");
            String resposta = sc.nextLine().toUpperCase();
            
            if (resposta.equals("S")) {
                int idSelecionado = viewAtor.selecionaEpisodioDoResultado(resultados);
                
                if (idSelecionado > 0) {
                    Episodio episodioSelecionado = arqEpisodios.read(idSelecionado);
                    
                    // Mostrar também o nome da série
                    if (episodioSelecionado != null) {
                        Serie serie = arqSerie.read(episodioSelecionado.getIdSerie());
                        String nomeSerie = (serie != null) ? serie.getNome() : "Série não encontrada";
                        System.out.println("Série: " + nomeSerie);
                    }
                    
                    viewEpisodio.mostraEpisodio(episodioSelecionado);
                }
            }
        }
    }

    public void listarSeries() throws Exception {
        System.out.println("\nSéries disponíveis:");
        int ultimoId = arqSerie.ultimoId();
        boolean temSeries = false;

        for (int i = 1; i <= ultimoId; i++) {
            Serie serie = arqSerie.read(i);
            if (serie != null) {
                System.out.println("ID: " + serie.getId() + " | Nome: " + serie.getNome());
                temSeries = true;
            }
        }

        if (!temSeries) {
            System.out.println("Não há séries cadastradas. Cadastre uma série primeiro.");
        }

    }

    public void menuAtor() throws Exception {
        int opcao;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Ator");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Visualizar ator com série");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarAtorID();
                    break;
                case 2:
                    incluirAtor();
                    break;
                case 3:
                    alterarAtorID();
                    break;
                case 4:
                    excluirAtorNome();
                    break;
                case 5:
                    visualizarAtorSerie();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }

    public void buscarAtorID() throws Exception {
        int id = viewAtor.buscarID();
        Ator ator = arqAtor.read(id);
        if (ator != null) {
            viewAtor.mostrarAtor(ator);
        } else {
            System.out.println("Ator não encontrado!");
        }
    }

    public void incluirAtor() throws Exception {
        Ator ator = viewAtor.incluirAtor();
        if (arqAtor.create(ator)) {
            System.out.println("Ator incluído com sucesso!");
        } else {
            System.out.println("Erro ao incluir ator!");
        }
    }


}


