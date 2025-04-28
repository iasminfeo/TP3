package TP2.Menu;

import java.util.ArrayList;
import java.util.Scanner;

import TP2.Model.Ator;
import TP2.Model.Episodio;
import TP2.Model.Ator;
import TP2.Model.Serie;
import TP2.Service.Arquivo;
import TP2.Service.RelacionamentoSerieAtor;
import TP2.View.ViewAtor;
import TP2.View.ViewSerie;

public class MenuAtor {
    public Scanner sc = new Scanner(System.in);
    Arquivo<Ator> arqAtor;
    Arquivo<Serie> arqSerie;
    ViewAtor viewAtor;
    ViewSerie viewSerie;
    RelacionamentoSerieAtor relacionamento;

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
            System.out.println("\n1 - Listar  atores");
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
                    listarAtors(); // check
                    break;
                case 2:
                    gerenciarAtorPorNome(); // check
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
                int idSelecionado = viewAtor.selecionaAtorDoResultado(resultados);

                if (idSelecionado > 0) {
                    Ator AtorSelecionado = arqAtor.read(idSelecionado);

                    // Mostrar também o nome da série
                    if (AtorSelecionado != null) {
                        Serie Serie = arqSerie.read(AtorSelecionado.getIDSerie());
                        String nomeSerie = (Serie != null) ? Serie.getNome() : "Série não encontrada";
                        System.out.println("Série: " + nomeSerie);
                    }

                    viewAtor.mostrarAtor(AtorSelecionado);
                }
            }
        }
    }

    public void listarAtors() throws Exception {
        System.out.println("\nSéries disponíveis:");
        int ultimoId = arqAtor.ultimoId();
        boolean temAtors = false;

        for (int i = 1; i <= ultimoId; i++) {
            Ator Ator = arqAtor.read(i);
            if (Ator != null) {
                System.out.println("ID: " + Ator.getId() + " | Nome: " + Ator.getNome());
                temAtors = true;
            }
        }

        if (!temAtors) {
            System.out.println("Não há séries cadastradas. Cadastre uma série primeiro.");
        }

    }

    public void gerenciarAtorPorNome() throws Exception {
        String termoBusca = viewSerie.LerNomeSerie();

        if (termoBusca.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }

        // Realizar a busca
        ArrayList<Serie> resultados = relacionamento.buscarSeriePorNome(termoBusca);

        // Exibir resultados
        viewSerie.mostraResultadoBuscaSeries(resultados);

        if (resultados.isEmpty()) {
            return;
        }

        // Selecionar série para gerenciar episódios
        System.out.print("\nDigite o ID da série para gerenciar seus atores (0 para cancelar): ");
        int idSelecionado = sc.nextInt();
        sc.nextLine(); // Limpar buffer

        if (idSelecionado <= 0) {
            System.out.println("Operação cancelada.");
            return;
        }

        // Verificar se o ID está na lista
        boolean encontrado = false;
        Serie serieSelecionada = null;
        for (Serie s : resultados) {
            if (s.getId() == idSelecionado) {
                encontrado = true;
                serieSelecionada = s;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("ID não encontrado na lista!");
            return;
        }

        gerenciarAtoresDeSerie(serieSelecionada.getId());
    }

    public void gerenciarAtoresDeSerie(int idSerie) throws Exception {
        Serie serie = arqSerie.read(idSerie);
        if (serie == null) {
            System.out.println("Série não encontrada!");
            return;
        }

        System.out.println("Gerenciando atores da série: " + serie.getNome());

        int opcao;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Ator > " + serie.getNome());
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Listar todos os atores da série");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarAtorSeriePorID(idSerie);
                    break;
                case 2:
                    incluirAtor(idSerie);
                    break;
                case 3:
                    alterarAtorPorNome(idSerie);
                    break;
                case 4:
                    excluirAtorNome(idSerie);
                    break;
                case 5:
                    visualizarAtoresDaSerie(idSerie);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }

    public void buscarAtorSeriePorID(int idSerie) throws Exception {
        int id = viewAtor.LerIDAtor();
        Ator ator = arqAtor.read(id);
        if (ator != null) {
            viewAtor.mostrarAtor(ator);
        } else {
            System.out.println("Ator não encontrado!");
        }
    }

    public void incluirAtor(int idSerie) throws Exception {
        Serie serie = arqSerie.read(idSerie);
        if (serie == null) {
            System.out.println("Série não encontrada!");
            return;
        }

        Ator ator = viewAtor.incluirAtor(idSerie);
        if (ator == null) {
            System.out.println("Erro ao incluir ator!");
            return;
        }

        int id = arqAtor.create(ator);
        ator.setId(id);
    }

    public void alterarAtorPorNome(int idSerie) throws Exception {
        String termoBusca = viewAtor.lerNomeAtor();

        if (termoBusca.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }

        // Realizar a busca em todos os episódios
        ArrayList<Ator> resultados = relacionamento.buscarAtorPorNomeEmSerie(termoBusca, idSerie);

        // Exibir resultados
        viewAtor.mostraResultadoBuscaAtores(resultados);

        if (resultados.isEmpty()) {
            return;
        }

        // Selecionar Ator para alterar
        System.out.print("\nDigite o ID do ator que deseja alterar (0 para cancelar): ");
        int idSelecionado = sc.nextInt();
        sc.nextLine(); // Limpar buffer

        if (idSelecionado <= 0) {
            System.out.println("Operação cancelada.");
            return;
        }

        // Verificar se o ID está na lista
        boolean encontrado = false;
        for (Ator at : resultados) {
            if (at.getId() == idSelecionado) {
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("ID não encontrado na lista!");
            return;
        }

        // Continuar com a alteração do episódio
        alterarAtor(idSerie, idSelecionado);
    }

    public void alterarAtor(int idSerie, int idAtor) throws Exception {
        Ator atorAtual = arqAtor.read(idAtor);
        if (atorAtual == null) {
            System.out.println("Ator não encontrado!");
            return;
        }

        if (atorAtual.getIDSerie() != idSerie) {
            System.out.println("O ator não pertence a esta série!");
            return;
        }

        Ator atorNovo = viewAtor.alterarAtor(idSerie, atorAtual);
        if (atorNovo == null) {
            System.out.println("Erro ao alterar ator!");
            return;
        }

        relacionamento.removerRelacionamento(atorAtual.getIDSerie(), atorAtual.getId());
        boolean resultado = arqAtor.update(atorNovo);

        if (resultado) {
            relacionamento.atualizarIndicesAposOperacao(atorNovo, "update");
            System.out.println("Ator atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar ator!");
        }
    }

    public void excluirAtorNome(int idSerie) throws Exception {

        String termoBusca = viewAtor.lerNomeAtor();

        if (termoBusca.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }

        // Realizar a busca em episódios da série específica
        ArrayList<Ator> resultados = relacionamento.buscarAtorPorNomeEmSerie(termoBusca, idSerie);

        // Exibir resultados
        viewAtor.mostraResultadoBuscaAtores(resultados);

        if (resultados.isEmpty()) {
            return;
        }

        // Selecionar episódio para excluir
        System.out.print("\nDigite o ID do ator que deseja excluir (0 para cancelar): ");
        int idSelecionado = sc.nextInt();
        sc.nextLine(); // Limpar buffer

        if (idSelecionado <= 0) {
            System.out.println("Operação cancelada.");
            return;
        }

        // Verificar se o ID está na lista
        boolean encontrado = false;
        Ator AtorParaExcluir = null;

        for (Ator at : resultados) {
            if (at.getId() == idSelecionado) {
                encontrado = true;
                AtorParaExcluir = at;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("ID não encontrado na lista!");
            return;
        }

        // Confirmar exclusão
        System.out.print("\nConfirmar exclusão do ator \"" + AtorParaExcluir.getNome() + "\"? (S/N): ");
        String confirmacao = sc.nextLine().toUpperCase();

        if (!confirmacao.equals("S")) {
            System.out.println("Operação cancelada.");
            return;
        }

        // Remover da árvore B+ antes de excluir do arquivo
        relacionamento.atualizarIndicesAposOperacao(AtorParaExcluir, "delete");

        boolean resultado = arqAtor.delete(idSelecionado);
        System.out.println(resultado ? "Ator excluído com sucesso!" : "Erro ao excluir Ator.");
    }

    public void visualizarAtoresDaSerie(int idSerie) throws Exception {
        System.out.println("\n======= LISTANDO ATORES DA SÉRIE ID: " + idSerie + " =======");

        // Primeiro verificar se a série existe
        Serie serie = arqSerie.read(idSerie);
        if (serie == null) {
            System.out.println("Série não encontrada com ID: " + idSerie);
            return;
        }

        System.out.println("Série: " + serie.getNome());

        // Obter episódios usando o método padrão do relacionamento
        ArrayList<Ator> Atores = relacionamento.getAtoresDaSerie(idSerie);

        if (Atores.isEmpty()) {
            System.out.println("\nNão há episódios cadastrados para esta série.");
            return;
        }

        // Mostrar quantidade encontrada
        System.out.println("\nForam encontrados " + Atores.size() + " ator(es):");

        // Exibir os episódios
        viewAtor.mostraListaAtores(Atores);
    }

}
