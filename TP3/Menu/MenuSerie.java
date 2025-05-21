package Menu;

import java.util.ArrayList;
import java.util.Scanner;

import Model.*;
import Service.*;
import View.*;

public class MenuSerie {
    public Scanner sc = new Scanner(System.in);
    Arquivo<Serie> arqSerie;
    Arquivo<Episodio> arqEpisodios;
    Arquivo<Ator> arqAtor;
    ViewSerie viewSerie;
    ViewAtor viewAtor;
    RelacionamentoSerieEpisodio relacionamento;
    RelacionamentoSerieAtor relacionamentoAtorSerie;
    IndiceInvertido indice;

    public MenuSerie(Scanner sc, Arquivo<Serie> arqSerie, Arquivo<Episodio> arqEpisodio, Arquivo<Ator> arqAtor) throws Exception {
        this.sc = sc;
        this.arqSerie = arqSerie;
        this.arqEpisodios = arqEpisodio;
        this.arqAtor = arqAtor;
        this.viewSerie = new ViewSerie(sc);
        this.viewAtor = new ViewAtor(sc);
        this.relacionamento = new RelacionamentoSerieEpisodio(arqSerie, arqEpisodios);
        this.relacionamentoAtorSerie = new RelacionamentoSerieAtor(arqSerie, arqAtor);
        this.indice = new IndiceInvertido();
    }

    public void menuSerie() throws Exception {
        int opcao;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Série");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Visualizar atores da serie");
            System.out.println("6 - Linkar série em um ator");
            System.out.println("7 - Desvincular série de um ator");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> buscarSeriePorNome();
                case 2 -> incluirSerie();
                case 3 -> alterarSerieID();
                case 4 -> excluirSerieNome();
                case 5 -> visualizarAtoresSerie();
                case 6 -> linkarAtorSerie();
                case 7 -> desvincularAtorSerie();
                case 0 -> {}
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    public void buscarSeriePorNome() throws Exception {
        String termo = viewSerie.LerNomeSerie();

        ElementoLista[] elementos = indice.buscarSeriesPorIndice(termo);

        ArrayList<Serie> series = new ArrayList<>();

        for (ElementoLista el : elementos) {
            Serie s = arqSerie.read(el.getId());
            if (s != null) {
                series.add(s);
            }
        }

        System.out.println("\n===== DEBUG: Resultado da Lista Invertida =====");
        for (ElementoLista el : elementos) {
            System.out.println("ID: " + el.getId() + " | Peso: " + el.getFrequencia());
        }

        viewSerie.mostraResultadoBuscaSeries(series);
    }

    public void incluirSerie() throws Exception {
        Serie S = viewSerie.incluirSerie();
        if (S == null) {
            System.out.println("Não foi possível criar a sua série!");
            return;
        }
        int id = arqSerie.create(S);
        S.setId(id);
        indice.inserirSerie(S);
        System.out.println("Série criada com sucesso! ID : " + id);
    }

    public void alterarSerieID() throws Exception {
        buscarSeriePorNome();
        System.out.print("\nDigite o ID da série que deseja alterar (0 para cancelar): ");
        int id = sc.nextInt();
        sc.nextLine();

        Serie S = arqSerie.read(id);
        if (S == null) {
            System.out.println("Série não encontrada.");
            return;
        }

        Serie NovaS = viewSerie.alterarSerie(S);
        if (NovaS == null) {
            System.out.println("Cancelando alterações.");
            return;
        }

        NovaS.setId(id);
        boolean result = arqSerie.update(NovaS);
        if (result) {
            indice.atualizarSerie(S.getNome(), NovaS);
            System.out.println("Série atualizada com sucesso!");
        } else {
            System.out.println("Erro no sistema ao atualizar série.");
        }
    }

    public void excluirSerieNome() throws Exception {
        buscarSeriePorNome();
        System.out.print("\nDigite o ID da série que deseja excluir (0 para cancelar): ");
        int id = sc.nextInt();
        sc.nextLine();

        Serie S = arqSerie.read(id);
        if (S == null) {
            System.out.println("Série não encontrada.");
            return;
        }

        excluirSerie(id, S);
    }

    public void excluirSerie(int id, Serie S) throws Exception {
        if (relacionamento.serieTemEpisodios(id)) {
            System.out.println("Não é possível excluir! Existem episódios vinculados a esta série.");
            int qtdEP = relacionamento.getTotalEpisodios(id);
            System.out.println("Total de episódios vinculados: " + qtdEP);
            return;
        }

        System.out.print("\nConfirma a exclusão? (S/N) ");
        char resp = sc.next().charAt(0);
        sc.nextLine();

        if (resp == 'S' || resp == 's') {
            boolean result = arqSerie.delete(id);
            if (result) {
                indice.excluirSerie(S.getNome(), id);
                System.out.println("Série excluída com sucesso!");
            } else {
                System.out.println("Erro ao excluir série.");
            }
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    public void visualizarAtoresSerie() throws Exception {
        buscarSeriePorNome();
        System.out.print("\nDigite o ID da série que deseja visualizar os atores (0 para cancelar): ");
        int idSelecionado = sc.nextInt();
        sc.nextLine();

        Serie S = arqSerie.read(idSelecionado);
        if (S == null) {
            System.out.println("Série não encontrada.");
            return;
        }

        ArrayList<Ator> atores = relacionamentoAtorSerie.getAtoresDaSerie(idSelecionado);
        viewAtor.mostraResultadoBuscaAtores(atores);
    }

    public void linkarAtorSerie() throws Exception {
        buscarSeriePorNome();
        System.out.print("\nDigite o ID da série que deseja vincular um ator (0 para cancelar): ");
        int idSelecionadoSerie = sc.nextInt();
        sc.nextLine();

        Serie S = arqSerie.read(idSelecionadoSerie);
        if (S == null) {
            System.out.println("Série não encontrada!");
            return;
        }

        String termoBuscaAtor = viewAtor.lerNomeAtor();
        ArrayList<Ator> atores = relacionamentoAtorSerie.buscarAtorPorNome(termoBuscaAtor);

        viewAtor.mostraResultadoBuscaAtores(atores);
        if (atores.isEmpty()) return;

        System.out.print("\nDigite o ID do ator que deseja vincular à série (0 para cancelar): ");
        int idSelecionadoAtor = sc.nextInt();
        sc.nextLine();

        Ator A = arqAtor.read(idSelecionadoAtor);
        if (A == null) {
            System.out.println("Ator não encontrado!");
            return;
        }

        relacionamento.removerRelacionamento(A.getIDSerie(), A.getId());
        viewSerie.linkarSerieAtor(idSelecionadoAtor, idSelecionadoSerie);
        A.setIDSerie(S.getId());

        boolean result = arqAtor.update(A);
        if (result) {
            relacionamentoAtorSerie.atualizarIndicesAposOperacao(A, "update");
            System.out.println("Ator vinculado à série com sucesso!");
        } else {
            System.out.println("Erro ao vincular ator à série!");
        }
    }

    public void desvincularAtorSerie() throws Exception {
        buscarSeriePorNome();
        System.out.print("\nDigite o ID da série que deseja desvincular do ator (0 para cancelar): ");
        int idSelecionadoSerie = sc.nextInt();
        sc.nextLine();

        Serie S = arqSerie.read(idSelecionadoSerie);
        if (S == null) {
            System.out.println("Série não encontrada!");
            return;
        }

        String termoBuscaAtor = viewAtor.lerNomeAtor();
        ArrayList<Ator> atores = relacionamentoAtorSerie.buscarAtorPorNome(termoBuscaAtor);

        viewAtor.mostraResultadoBuscaAtores(atores);
        if (atores.isEmpty()) return;

        System.out.print("\nDigite o ID do ator que deseja desvincular da série (0 para cancelar): ");
        int idSelecionadoAtor = sc.nextInt();
        sc.nextLine();

        Ator A = arqAtor.read(idSelecionadoAtor);
        if (A == null) {
            System.out.println("Ator não encontrado!");
            return;
        }

        if (A.getIDSerie() != S.getId()) {
            System.out.println("O ator não está vinculado a esta série!");
            return;
        }

        relacionamento.removerRelacionamento(A.getIDSerie(), A.getId());
        S.setIDator(0);

        viewAtor.deslinkarAtorSerie(A.getId(), S.getId());
        boolean result = arqSerie.update(S);

        if (result) {
            relacionamentoAtorSerie.atualizarIndicesAposOperacao(A, "update");
            System.out.println("Ator desvinculado da série com sucesso!");
        } else {
            System.out.println("Erro ao desvincular ator da série!");
        }
    }
}
