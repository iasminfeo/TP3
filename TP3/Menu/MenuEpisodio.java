package Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import Model.Episodio;
import Model.Serie;
import Model.ElementoLista;
import Service.Arquivo;
import Service.IndiceInvertido;
import Service.RelacionamentoSerieEpisodio;
import View.ViewEpisodio;
import View.ViewSerie;

public class MenuEpisodio {

    public Scanner sc = new Scanner(System.in);
    Arquivo<Episodio> arqEpisodios;
    Arquivo<Serie> arqSerie;
    ViewEpisodio viewEpisodio;
    ViewSerie viewSerie;
    RelacionamentoSerieEpisodio relacionamento;
    IndiceInvertido indice;

    public MenuEpisodio(Scanner sc, Arquivo<Episodio> arqEpisodios, Arquivo<Serie> arqSerie) throws Exception {
        this.sc = sc;
        this.arqEpisodios = arqEpisodios;
        this.arqSerie = arqSerie;
        this.viewEpisodio = new ViewEpisodio(sc);
        this.viewSerie = new ViewSerie(sc);
        this.relacionamento = new RelacionamentoSerieEpisodio(arqSerie, arqEpisodios);
        this.indice = new IndiceInvertido();
    }

    public void menuEpisodio() throws Exception {
        int opcao;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Episódio");
            System.out.println("\n1 - Listar séries");
            System.out.println("2 - Gerenciar episódios de uma série");
            System.out.println("3 - Buscar episódio");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> listarSeries();
                case 2 -> gerenciarEpisodiosPorNome();
                case 3 -> buscarEpisodioPorNome();
                case 0 -> {}
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    public void buscarEpisodioPorNome() throws Exception {
        String termo = viewEpisodio.lerNomeEpisodio();

        ElementoLista[] elementos = indice.buscarEpisodiosPorIndice(termo);

        Arrays.sort(elementos, Comparator.comparing(ElementoLista::getFrequencia).reversed());

        ArrayList<Episodio> episodios = new ArrayList<>();

        for (ElementoLista el : elementos) {
            Episodio ep = arqEpisodios.read(el.getId());
            if (ep != null) {
                episodios.add(ep);
            }
        }

        viewEpisodio.mostraResultadoBuscaEpisodios(episodios);
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
            System.out.println("Não há séries cadastradas.");
        }
    }

    public void gerenciarEpisodiosPorNome() throws Exception {
        String termo = viewSerie.LerNomeSerie();

        ElementoLista[] elementos = indice.buscarSeriesPorIndice(termo);

        Arrays.sort(elementos, Comparator.comparing(ElementoLista::getFrequencia).reversed());

        ArrayList<Serie> series = new ArrayList<>();

        for (ElementoLista el : elementos) {
            Serie s = arqSerie.read(el.getId());
            if (s != null) {
                series.add(s);
            }
        }

        viewSerie.mostraResultadoBuscaSeries(series);

        if (series.isEmpty()) return;

        System.out.print("\nDigite o ID da série para gerenciar seus episódios (0 para cancelar): ");
        int idSelecionado = sc.nextInt();
        sc.nextLine();

        Serie serie = arqSerie.read(idSelecionado);
        if (serie == null) {
            System.out.println("Série não encontrada.");
            return;
        }

        gerenciarEpisodiosDeSerie(serie.getId());
    }

    private void gerenciarEpisodiosDeSerie(int idSerie) throws Exception {
        Serie serie = arqSerie.read(idSerie);
        if (serie == null) {
            System.out.println("Série não encontrada.");
            return;
        }

        System.out.println("Gerenciando episódios da série: " + serie.getNome());

        int op;
        do {
            System.out.println("\nPUCFlix 1.0\n-----------\n> Início > Episódios > " + serie.getNome());
            System.out.println("1) Incluir ");
            System.out.println("2) Buscar ");
            System.out.println("3) Alterar ");
            System.out.println("4) Excluir ");
            System.out.println("5) Listar todos os episódios");
            System.out.println("0) Retornar ao menu anterior");
            System.out.print("Escolha uma opção: ");

            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> incluirEpisodio(idSerie);
                case 2 -> buscarEpisodioNaSeriePorID(idSerie);
                case 3 -> alterarEpisodioPorNome(idSerie);
                case 4 -> excluirEpisodioPorNome(idSerie);
                case 5 -> listarEpisodiosDaSerie(idSerie);
                default -> {
                    if (op != 0) {
                        System.out.println("Opção inválida!");
                    }
                }
            }
        } while (op != 0);
    }

    public void incluirEpisodio(int idSerie) throws Exception {
        Episodio episodio = viewEpisodio.incluirEpisodio(idSerie);
        if (episodio == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        int id = arqEpisodios.create(episodio);
        episodio.setId(id);
        indice.inserirEpisodio(episodio);

        System.out.println("Episódio criado com sucesso! ID: " + id);
    }

    public void alterarEpisodioPorNome(int idSerie) throws Exception {
        String termo = viewEpisodio.lerNomeEpisodio();

        ElementoLista[] elementos = indice.buscarEpisodiosPorIndice(termo);

        Arrays.sort(elementos, Comparator.comparing(ElementoLista::getFrequencia).reversed());

        ArrayList<Episodio> episodios = new ArrayList<>();

        for (ElementoLista el : elementos) {
            Episodio ep = arqEpisodios.read(el.getId());
            if (ep != null && ep.getIdSerie() == idSerie) {
                episodios.add(ep);
            }
        }

        viewEpisodio.mostraResultadoBuscaEpisodios(episodios);
        if (episodios.isEmpty()) return;

        System.out.print("\nDigite o ID do episódio que deseja alterar (0 para cancelar): ");
        int idSelecionado = sc.nextInt();
        sc.nextLine();

        Episodio epAtual = arqEpisodios.read(idSelecionado);
        if (epAtual == null) {
            System.out.println("Episódio não encontrado.");
            return;
        }

        Episodio epNovo = viewEpisodio.alterarEpisodio(idSerie, epAtual);
        if (epNovo == null) {
            System.out.println("Cancelado.");
            return;
        }

        indice.atualizarEpisodio(epAtual.getNome(), epNovo);

        epNovo.setId(idSelecionado);
        arqEpisodios.update(epNovo);

        System.out.println("Episódio alterado com sucesso!");
    }

    public void excluirEpisodioPorNome(int idSerie) throws Exception {
        String termo = viewEpisodio.lerNomeEpisodio();

        ElementoLista[] elementos = indice.buscarEpisodiosPorIndice(termo);

        Arrays.sort(elementos, Comparator.comparing(ElementoLista::getFrequencia).reversed());

        ArrayList<Episodio> episodios = new ArrayList<>();

        for (ElementoLista el : elementos) {
            Episodio ep = arqEpisodios.read(el.getId());
            if (ep != null && ep.getIdSerie() == idSerie) {
                episodios.add(ep);
            }
        }

        viewEpisodio.mostraResultadoBuscaEpisodios(episodios);
        if (episodios.isEmpty()) return;

        System.out.print("\nDigite o ID do episódio que deseja excluir (0 para cancelar): ");
        int idSelecionado = sc.nextInt();
        sc.nextLine();

        Episodio ep = arqEpisodios.read(idSelecionado);
        if (ep == null) {
            System.out.println("Episódio não encontrado.");
            return;
        }

        indice.excluirEpisodio(ep.getNome(), idSelecionado);
        arqEpisodios.delete(idSelecionado);

        System.out.println("Episódio excluído com sucesso!");
    }

    private void listarEpisodiosDaSerie(int idSerie) throws Exception {
        ArrayList<Episodio> episodios = relacionamento.getEpisodiosDaSerie(idSerie);

        if (episodios.isEmpty()) {
            System.out.println("\nNão há episódios cadastrados.");
            return;
        }

        System.out.println("\n===== Episódios da Série =====");
        for (Episodio ep : episodios) {
            System.out.println("ID: " + ep.getId() + " | Nome: " + ep.getNome());
        }
    }

    public void buscarEpisodioNaSeriePorID(int idSerie) throws Exception {
        int id = viewEpisodio.lerIDEpisodio();
        Episodio episodio = arqEpisodios.read(id);
        if (episodio != null) {
            System.out.println("\nEpisódio: " + episodio.getNome());
        } else {
            System.out.println("\nEpisódio não encontrado.");
        }
    }
}
