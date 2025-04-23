package TP1.Menu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import TP1.Model.Episodio;
import TP1.Model.Serie;
import TP1.Service.Arquivo;
import TP1.Service.ParIDSerieEpisodio;
import TP1.Service.RelacionamentoSerieEpisodio;
import TP1.View.ViewEpisodio;
import TP1.View.ViewSerie;

public class MenuEpisodio {
    public Scanner sc = new Scanner(System.in);
    Arquivo<Episodio> arqEpisodios;
    Arquivo<Serie> arqSerie;
    ViewEpisodio viewEpisodio;
    ViewSerie viewSerie;
    RelacionamentoSerieEpisodio relacionamento;

    public MenuEpisodio(Scanner sc, Arquivo<Episodio> arqEpisodios, Arquivo<Serie> arqSerie) {
        this.sc = sc;
        this.arqEpisodios = arqEpisodios;
        this.arqSerie = arqSerie;
        this.viewEpisodio = new ViewEpisodio(sc);
        this.viewSerie = new ViewSerie(sc);
        this.relacionamento = new RelacionamentoSerieEpisodio(arqSerie, arqEpisodios);
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
                case 1:
                    listarSeries(); // check
                    break;
                case 2:
                    gerenciarEpisodiosPorNome(); //check
                    break;
                case 3:
                    buscarEpisodioPorNome();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);
    }

    private void gerenciarEpisodiosPorNome() throws Exception {
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
        System.out.print("\nDigite o ID da série para gerenciar seus episódios (0 para cancelar): ");
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
        
        gerenciarEpisodiosDeSerie(serieSelecionada.getId());
    }
    
    //somente para testes
    private void buscarEpisodioPorId() throws Exception {
        int id = viewEpisodio.lerIDEpisodio();
        Episodio episodio = arqEpisodios.read(id);
        if (episodio != null) {
            Serie serie = arqSerie.read(episodio.getIdSerie());
            String nomeSerie = (serie != null) ? serie.getNome() : "Série não encontrada";
            System.out.println("Série: " + nomeSerie);
        }
        viewEpisodio.mostraEpisodio(episodio);
    }
    
    private void buscarEpisodioPorNome() throws Exception {
        String termoBusca = viewEpisodio.lerNomeEpisodio();
        
        if (termoBusca.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }
        
        // Realizar a busca em todos os episódios
        ArrayList<Episodio> resultados = relacionamento.buscarEpisodioPorNome(termoBusca);
        
        // Exibir resultados
        viewEpisodio.mostraResultadoBuscaEpisodios(resultados);
        
        // Se houver resultados, perguntar se quer selecionar um episódio
        if (!resultados.isEmpty()) {
            System.out.print("\nDeseja selecionar um episódio para ver mais detalhes? (S/N): ");
            String resposta = sc.nextLine().toUpperCase();
            
            if (resposta.equals("S")) {
                int idSelecionado = viewEpisodio.selecionaEpisodioDoResultado(resultados);
                
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
            sc.nextLine(); // Limpar buffer

            switch (op) {
                case 1 -> incluirEpisodio(idSerie); // check
                case 2 -> buscarEpisodioNaSeriePorID(idSerie); //check
                case 3 -> alterarEpisodioPorNome(idSerie); //check
                case 4 -> excluirEpisodioPorNome(idSerie); //check
                case 5 -> listarEpisodiosDaSerie(idSerie); //check
                default -> {
                    if (op != 0) {
                        System.out.println("Opção inválida!");
                    }
                }
            }
        } while (op != 0);
    }

    public void incluirEpisodio(int idSerie) throws Exception {
        // Verificar se a série existe
        Serie serie = arqSerie.read(idSerie);
        if (serie == null) {
            System.out.println("Série não encontrada.");
            return;
        }

        Episodio episodio = viewEpisodio.incluirEpisodio(idSerie);
        if (episodio == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        int id = arqEpisodios.create(episodio);

        // Importante: atualiza o ID no objeto episódio após a criação
        episodio.setId(id);

        
    }

    public void alterarEpisodioPorNome(int idSerie) throws Exception {
        String nomeEP = viewEpisodio.lerNomeEpisodio();

        if(nomeEP.trim().isEmpty()){
            System.out.println("Nome inváido.");
            return;
        }

        // Realizar a busca em episódios da série específica
        ArrayList<Episodio> resultados = relacionamento.buscarEpisodioPorNomeEmSerie(nomeEP, idSerie);
        
        // Exibir resultados
        viewEpisodio.mostraResultadoBuscaEpisodios(resultados);
        
        if (resultados.isEmpty()) {
            return;
        }
        
        // Selecionar episódio para alterar
        System.out.print("\nDigite o ID do episódio que deseja alterar (0 para cancelar): ");
        int idSelecionado = sc.nextInt();
        sc.nextLine(); // Limpar buffer
        
        if (idSelecionado <= 0) {
            System.out.println("Operação cancelada.");
            return;
        }
        
        // Verificar se o ID está na lista
        boolean encontrado = false;
        for (Episodio ep : resultados) {
            if (ep.getId() == idSelecionado) {
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            System.out.println("ID não encontrado na lista!");
            return;
        }
        
        // Continuar com a alteração do episódio
        alterarEpisodio(idSerie, idSelecionado);
    }

    private void alterarEpisodio(int idSerie, int id) throws Exception {
        // Verificar se o episódio existe e pertence à série atual
        Episodio episodioAtual = arqEpisodios.read(id);
        if (episodioAtual == null) {
            System.out.println("Episódio não encontrado.");
            return;
        }
        
        if (episodioAtual.getIdSerie() != idSerie) {
            System.out.println("Este episódio pertence a outra série. Não é possível editar.");
            return;
        }
        
        Episodio episodioNovo = viewEpisodio.alterarEpisodio(idSerie, episodioAtual);
        if (episodioNovo == null) {
            System.out.println("Operação cancelada.");
            return;
        }
        
        // Remover o relacionamento antigo
        relacionamento.removerRelacionamento(episodioAtual.getIdSerie(), episodioAtual.getId());
        
        episodioNovo.setId(id);
        boolean resultado = arqEpisodios.update(episodioNovo);
        
        if (resultado) {
            // Adicionar o novo relacionamento
            relacionamento.atualizarIndicesAposOperacao(episodioNovo, "update");
            System.out.println("Episódio atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar episódio.");
        }
    }
    //so para teste
    private void excluirEpisodioID() throws Exception {
        int id = viewEpisodio.lerIDEpisodio();
        // Verificar se o episódio existe antes de excluir
        Episodio episodio = arqEpisodios.read(id);
        if (episodio == null) {
            System.out.println("Episódio não encontrado.");
            return;
        }
        
        // Confirmar exclusão
        System.out.print("\nConfirmar exclusão do episódio \"" + episodio.getNome() + "\"? (S/N): ");
        String confirmacao = sc.nextLine().toUpperCase();
        if (!confirmacao.equals("S")) {
            System.out.println("Operação cancelada.");
            return;
        }
        
        // Remover da árvore B+ antes de excluir do arquivo
        relacionamento.atualizarIndicesAposOperacao(episodio, "delete");
        
        boolean resultado = arqEpisodios.delete(id);
        System.out.println(resultado ? "Episódio excluído com sucesso!" : "Erro ao excluir episódio.");
    }
    
    private void excluirEpisodioPorNome(int idSerie) throws Exception {
        String termoBusca = viewEpisodio.lerNomeEpisodio();
        
        if (termoBusca.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }
        
        // Realizar a busca em episódios da série específica
        ArrayList<Episodio> resultados = relacionamento.buscarEpisodioPorNomeEmSerie(termoBusca, idSerie);
        
        // Exibir resultados
        viewEpisodio.mostraResultadoBuscaEpisodios(resultados);
        
        if (resultados.isEmpty()) {
            return;
        }
        
        // Selecionar episódio para excluir
        System.out.print("\nDigite o ID do episódio que deseja excluir (0 para cancelar): ");
        int idSelecionado = sc.nextInt();
        sc.nextLine(); // Limpar buffer
        
        if (idSelecionado <= 0) {
            System.out.println("Operação cancelada.");
            return;
        }
        
        // Verificar se o ID está na lista
        boolean encontrado = false;
        Episodio episodioParaExcluir = null;
        
        for (Episodio ep : resultados) {
            if (ep.getId() == idSelecionado) {
                encontrado = true;
                episodioParaExcluir = ep;
                break;
            }
        }
        
        if (!encontrado) {
            System.out.println("ID não encontrado na lista!");
            return;
        }
        
        // Confirmar exclusão
        System.out.print("\nConfirmar exclusão do episódio \"" + episodioParaExcluir.getNome() + "\"? (S/N): ");
        String confirmacao = sc.nextLine().toUpperCase();
        
        if (!confirmacao.equals("S")) {
            System.out.println("Operação cancelada.");
            return;
        }
        
        // Remover da árvore B+ antes de excluir do arquivo
        relacionamento.atualizarIndicesAposOperacao(episodioParaExcluir, "delete");
        
        boolean resultado = arqEpisodios.delete(idSelecionado);
        System.out.println(resultado ? "Episódio excluído com sucesso!" : "Erro ao excluir episódio.");
    }

    private void listarEpisodiosDaSerie(int idSerie) throws Exception {
        System.out.println("\n======= LISTANDO EPISÓDIOS DA SÉRIE ID: " + idSerie + " =======");
        
        // Primeiro verificar se a série existe
        Serie serie = arqSerie.read(idSerie);
        if (serie == null) {
            System.out.println("Série não encontrada com ID: " + idSerie);
            return;
        }
        
        System.out.println("Série: " + serie.getNome());
        
        // Obter episódios usando o método padrão do relacionamento
        ArrayList<Episodio> episodios = relacionamento.getEpisodiosDaSerie(idSerie);
        
        if (episodios.isEmpty()) {
            System.out.println("\nNão há episódios cadastrados para esta série.");
            return;
        }
        
        // Mostrar quantidade encontrada
        System.out.println("\nForam encontrados " + episodios.size() + " episódio(s):");
        
        // Exibir os episódios
        viewEpisodio.mostraListaEpisodios(episodios);
    }

    public void buscarEpisodioNaSeriePorID(int idSerie) throws Exception {
        int id = viewSerie.lerIDSerie();
        Episodio episodio = arqEpisodios.read(id);
        if (episodio != null) {
            Serie serie = arqSerie.read(episodio.getIdSerie());
            String nomeSerie = (serie != null) ? serie.getNome() : "Série não encontrada";
            System.out.println("Série: " + nomeSerie);
        }
        viewEpisodio.mostraEpisodio(episodio);
    }

}
