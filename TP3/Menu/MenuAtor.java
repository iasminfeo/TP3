package Menu;

import java.util.ArrayList;
import java.util.Scanner;

import Model.*;
import Service.*;
import View.*;

public class MenuAtor {
    public Scanner sc = new Scanner(System.in);
    Arquivo<Ator> arqAtor;
    Arquivo<Serie> arqSerie;
    ViewAtor viewAtor;
    ViewSerie viewSerie;
    RelacionamentoSerieAtor relacionamento;
    IndiceInvertido indice;

    public MenuAtor(Scanner sc, Arquivo<Ator> arqAtor, Arquivo<Serie> arqSerie, IndiceInvertido indice) throws Exception {
        this.sc = sc;
        this.arqAtor = arqAtor;
        this.arqSerie = arqSerie;
        this.viewAtor = new ViewAtor(sc);
        this.viewSerie = new ViewSerie(sc);
        this.relacionamento = new RelacionamentoSerieAtor(arqSerie, arqAtor);
        this.indice = indice;
    }

    public void menuAtor() throws Exception {
        int opcao;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Ator");
            System.out.println("\n1 - Buscar Ator");
            System.out.println("2 - Incluir Ator");
            System.out.println("3 - Alterar Ator");
            System.out.println("4 - Excluir Ator");
            System.out.println("5 - Listar Atores");
            System.out.println("6 - Linkar Ator à Série");
            System.out.println("7 - Desvincular Ator da Série");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> buscarAtorPorNome();
                case 2 -> incluirAtor();
                case 3 -> alterarAtor();
                case 4 -> excluirAtor();
                case 5 -> listarAtores();
                case 6 -> linkarAtorSerie();
                case 7 -> desvincularAtorSerie();
                case 0 -> {}
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    public void buscarAtorPorNome() throws Exception {
        String termo = viewAtor.lerNomeAtor();
        ElementoLista[] elementos = indice.buscarAtoresPorIndice(termo);

        ArrayList<Ator> atores = new ArrayList<>();

        for (ElementoLista el : elementos) {
            Ator a = arqAtor.read(el.getId());
            if (a != null) {
                atores.add(a);
            }
        }

        System.out.println("\n===== DEBUG: Resultado da Lista Invertida de Atores =====");
        for (ElementoLista el : elementos) {
            System.out.println("ID: " + el.getId() + " | Peso: " + el.getFrequencia());
        }

        viewAtor.mostraResultadoBuscaAtores(atores);

        if (!atores.isEmpty()) {
            System.out.print("\nDeseja selecionar um Ator para ver mais detalhes? (S/N): ");
            String resposta = sc.nextLine().toUpperCase();

            if (resposta.equals("S")) {
                int idSelecionado = viewAtor.selecionaAtorDoResultado(atores);

                if (idSelecionado > 0) {
                    Ator atorSelecionado = arqAtor.read(idSelecionado);

                    ArrayList<Serie> series = relacionamento.getSeriesDoAtor(idSelecionado);
                    viewAtor.mostraResultadoBuscaSeries(series);

                    viewAtor.mostrarAtor(atorSelecionado);
                }
            }
        }
    }

    public void incluirAtor() throws Exception {
        Ator A = viewAtor.incluirAtor(0);
        if (A == null) {
            System.out.println("Erro ao incluir ator!");
            return;
        }
        int id = arqAtor.create(A);
        A.setId(id);
        indice.inserirAtor(A);
        System.out.println("Ator incluído com sucesso! ID: " + id);
    }

    public void alterarAtor() throws Exception {
        String termo = viewAtor.lerNomeAtor();
        ElementoLista[] elementos = indice.buscarAtoresPorIndice(termo);

        ArrayList<Ator> atores = new ArrayList<>();

        for (ElementoLista el : elementos) {
            Ator a = arqAtor.read(el.getId());
            if (a != null) {
                atores.add(a);
            }
        }

        viewAtor.mostraResultadoBuscaAtores(atores);
        if (atores.isEmpty()) return;

        System.out.print("\nDigite o ID do ator que deseja alterar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Ator antigo = arqAtor.read(id);
        if (antigo == null) {
            System.out.println("Ator não encontrado!");
            return;
        }

        Ator novo = viewAtor.alterarAtor(0, antigo);
        if (novo == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        novo.setId(id);
        boolean result = arqAtor.update(novo);

        if (result) {
            indice.atualizarAtor(antigo.getNome(), novo);
            System.out.println("Ator atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar ator!");
        }
    }

    public void excluirAtor() throws Exception {
        String termo = viewAtor.lerNomeAtor();
        ElementoLista[] elementos = indice.buscarAtoresPorIndice(termo);

        ArrayList<Ator> atores = new ArrayList<>();

        for (ElementoLista el : elementos) {
            Ator a = arqAtor.read(el.getId());
            if (a != null) {
                atores.add(a);
            }
        }

        viewAtor.mostraResultadoBuscaAtores(atores);
        if (atores.isEmpty()) return;

        System.out.print("\nDigite o ID do ator que deseja excluir: ");
        int id = sc.nextInt();
        sc.nextLine();

        Ator A = arqAtor.read(id);
        if (A == null) {
            System.out.println("Ator não encontrado!");
            return;
        }

        System.out.print("\nConfirmar exclusão? (S/N): ");
        String resp = sc.nextLine().toUpperCase();
        if (!resp.equals("S")) {
            System.out.println("Operação cancelada.");
            return;
        }

        boolean result = arqAtor.delete(id);
        if (result) {
            indice.excluirAtor(A.getNome(), id);
            System.out.println("Ator excluído com sucesso!");
        } else {
            System.out.println("Erro ao excluir ator.");
        }
    }

    public void listarAtores() throws Exception {
        System.out.println("\nAtores cadastrados:");
        int ultimoId = arqAtor.ultimoId();
        boolean tem = false;

        for (int i = 1; i <= ultimoId; i++) {
            Ator A = arqAtor.read(i);
            if (A != null) {
                System.out.println("ID: " + A.getId() + " | Nome: " + A.getNome());
                tem = true;
            }
        }

        if (!tem) {
            System.out.println("Nenhum ator cadastrado.");
        }
    }

    public void linkarAtorSerie() throws Exception {
        System.out.print("Digite o ID do ator que deseja vincular: ");
        int idAtor = sc.nextInt();
        sc.nextLine();

        Ator A = arqAtor.read(idAtor);
        if (A == null) {
            System.out.println("Ator não encontrado!");
            return;
        }

        System.out.print("Digite o ID da série para vincular: ");
        int idSerie = sc.nextInt();
        sc.nextLine();

        Serie S = arqSerie.read(idSerie);
        if (S == null) {
            System.out.println("Série não encontrada!");
            return;
        }

        A.setIDSerie(S.getId());
        boolean result = arqAtor.update(A);

        if (result) {
            System.out.println("Ator vinculado à série com sucesso!");
        } else {
            System.out.println("Erro ao vincular ator!");
        }
    }

    public void desvincularAtorSerie() throws Exception {
        System.out.print("Digite o ID do ator que deseja desvincular: ");
        int idAtor = sc.nextInt();
        sc.nextLine();

        Ator A = arqAtor.read(idAtor);
        if (A == null) {
            System.out.println("Ator não encontrado!");
            return;
        }

        if (A.getIDSerie() == 0) {
            System.out.println("Ator não está vinculado a nenhuma série.");
            return;
        }

        A.setIDSerie(0);
        boolean result = arqAtor.update(A);

        if (result) {
            System.out.println("Ator desvinculado da série com sucesso!");
        } else {
            System.out.println("Erro ao desvincular ator!");
        }
    }
}
