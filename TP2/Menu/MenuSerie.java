package TP2.Menu;

import java.util.ArrayList;
import java.util.Scanner;

import TP2.Model.Episodio;
import TP2.Model.Serie;
import TP2.Model.Ator;
import TP2.Service.*;
import TP2.View.*;


public class MenuSerie {
    public Scanner sc = new Scanner(System.in);
    Arquivo<Serie> arqSerie;
    Arquivo<Episodio> arqEpisodios;
    Arquivo<Ator> arqAtor;
    ViewSerie viewSerie;
    ViewAtor viewAtor;
    RelacionamentoSerieEpisodio relacionamento;
    RelacionamentoSerieAtor relacionamentoAtorSerie;


    public MenuSerie(Scanner sc, Arquivo<Serie> arqSerie, Arquivo<Episodio> arqEpisodio, Arquivo<Ator> arqAtor ) throws Exception{
        this.sc = sc;
        this.arqSerie = arqSerie;
        this.arqEpisodios = arqEpisodio;
        this.arqAtor = arqAtor;
        this.viewSerie = new ViewSerie(sc);
        this.viewAtor = new ViewAtor(sc);
        this.relacionamento = new RelacionamentoSerieEpisodio(arqSerie,arqEpisodio);
        this.relacionamentoAtorSerie = new RelacionamentoSerieAtor(arqSerie,arqAtor);
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
            System.out.println("6 -  Linkar série em um ator");
            System.out.println("7 - Desvincular série de um ator ");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarSerieID( );
                    break;
                case 2:
                    incluirSerie();
                    break;
                case 3:
                    alterarSerieID();
                    break;
                case 4:
                    excluirSerieNome();
                    break;
                case 5:
                    visualizarAtoresSerie();
                    break;
                case 6:
                    linkarAtorSerie();
                    break;
                case 7:
                    desvincularAtorSerie();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);
    }

    public void buscarSerieID() throws Exception {
        int id = viewSerie.lerIDSerie();
        Serie S = arqSerie.read(id);
        if(S == null){
            System.out.println("Série não encontrada.");
            return;
        }
        viewSerie.mostraSerie(S);
    }

    public void incluirSerie() throws Exception {
        Serie S = viewSerie.incluirSerie();
        if(S == null){
            System.out.println("Não foi possivel criar a sua série!");
            return;
        }else{
            int id = arqSerie.create(S);
            System.out.println("Série criada com sucesso!   ID : " + id);
        }
    }

    public void alterarSerieID() throws Exception {
        int id = viewSerie.lerIDSerie();
        Serie S = arqSerie.read(id);
        if(S == null){
            System.out.println("Série não encontrada.");
            return;
        }
        System.out.println("Série encontrada: ");
        viewSerie.mostraSerie(S);

        Serie NovaS = S;
        NovaS = viewSerie.alterarSerie(S);

        if(NovaS == null){
            System.out.println("Cancelando alterações.");
            return;
        }

        NovaS.setId(id);
        boolean result = arqSerie.update(NovaS);
        if(result){
            System.out.println("Série atualizada com suscesso!");
        }else{
            System.out.println("Erro no sistema ao atualizar série.");
        }
        
    }

    public void excluirSerieNome() throws Exception {
       String termoBusca = viewSerie.LerNomeSerie();
        
        if (termoBusca.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }
        
        // Realizar a busca
        ArrayList<Serie> resultados = relacionamento.buscarSeriePorNome(termoBusca);
        
        // Exibir resultados
        viewSerie.mostrarSeriesEncontradas(resultados);
        
        if (resultados.isEmpty()) {
            return;
        }
        
        // Selecionar série para excluir
        System.out.print("\nDigite o ID da série que deseja excluir (0 para cancelar): ");
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
        
        // Continuar com a exclusão usando o ID selecionado
        excluirSerie(idSelecionado);
    }

    public void excluirSerie(int id) throws Exception{
        Serie S = arqSerie.read(id);

        //verifica se serie existe
        if(S == null){
            System.out.println("Série não encontrada.");
            return;
        }
        
        //verifica se exite episodios vinculados a serie
        if(relacionamento.serieTemEpisodios(id)){
            System.out.println("Não é possível exlcuir! Existem episódios vinculados a esta série.");
            System.out.println("Exclua todos episódios antes de continuar.");

            //mostrar qtd dos episódios
            int qtdEP = relacionamento.getTotalEpisodios(id);
            System.out.println("Total de episódios vinculados: " + qtdEP);
            return;
        }

        //confirmar a exclusao se possivel
        boolean result = false;
        System.out.print("\nConfirma a exclusão? (S/N) ");
        char resp = sc.next().charAt(0);
        if (resp == 'S' || resp == 's') {
            result = arqSerie.delete(id);
        } else {
            System.out.println("Exclusão cancelada.");
        }

        if(result){
            System.out.println("Série excluida com sucesso!");
        }else{
            System.out.println("Eroo ao excluir série.");
        }

    }

    public void visualizarAtoresSerie() throws Exception {
        String termobusco = viewSerie.LerNomeSerie();

        if (termobusco.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }

        // Realizar a busca
        ArrayList<Serie> resultados = relacionamento.buscarSeriePorNome(termobusco);

        // Exibir resultados
        viewSerie.mostrarSeriesEncontradas(resultados);

        if (resultados.isEmpty()) {
            return;
        }

        //Escolher série para visualizar atores
        System.out.print("\nDigite o ID da série que deseja visualizar os atores (0 para cancelar): ");
        int idSelecionado = sc.nextInt();
        sc.nextLine(); // Limpar buffer



        Serie S = arqSerie.read(idSelecionado);
        if(S == null){
            System.out.println("Série não encontrada.");
            return;
        }
        ArrayList<Ator> atores = relacionamentoAtorSerie.getAtoresDaSerie(idSelecionado);
        viewAtor.mostraResultadoBuscaAtores(atores);
    }

    public void linkarAtorSerie() throws Exception {
        String termoBusca = viewSerie.LerNomeSerie();
        
        if (termoBusca.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }
        
        // Realizar a busca
        ArrayList<Serie> resultados = relacionamento.buscarSeriePorNome(termoBusca);
        
        // Exibir resultados
        viewSerie.mostrarSeriesEncontradas(resultados);
        
        if (resultados.isEmpty()) {
            return;
        }
        
        // Selecionar série para vincular ator
        System.out.print("\nDigite o ID da série que deseja vincular um ator (0 para cancelar): ");
        int idSelecionadoSerie = sc.nextInt();
        sc.nextLine(); // Limpar buffer
        
        if (idSelecionadoSerie <= 0) {
            System.out.println("Operação cancelada.");
            return;
        }
        
        Serie S = arqSerie.read(idSelecionadoSerie);
        if (S == null) {
            System.out.println("Série não encontrada!");
            return;
        }

        
        // Verificar se o ID está na lista
        String termoBuscaAtor = viewAtor.lerNomeAtor();
        if (termoBuscaAtor.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }

        // Realizar a busca
        ArrayList<Ator> resultadosAtor = relacionamentoAtorSerie.buscarAtorPorNome(termoBuscaAtor);

        //exibir resultados
        viewAtor.mostraResultadoBuscaAtores(resultadosAtor);

        if (resultadosAtor.isEmpty()) {
            return;
        }
        
        // Selecionar ator para vincular à série
        System.out.print("\nDigite o ID do ator que deseja vincular à série (0 para cancelar): ");
        int idSelecionadoAtor = sc.nextInt();
        sc.nextLine(); // Limpar buffer

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

    public void desvincularAtorSerie() throws Exception{
        // Buscar série pelo nome
        String termoBusca = viewSerie.LerNomeSerie();
        if (termoBusca.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }

        // Realizar a busca
        ArrayList<Serie> resultado = relacionamento.buscarSeriePorNome(termoBusca);

        // Exibir resultados
        viewSerie.mostraResultadoBuscaSeries(resultado);

        if (resultado.isEmpty()) {
            System.out.println("Nenhuma série encontrada com o nome: " + termoBusca);
            return;
        }

        // Selecionar série para linkar
        System.out.print("\nDigite o ID da série que deseja deslinkar ao ator (0 para cancelar): ");
        int idSerieSelecionada = sc.nextInt();
        sc.nextLine(); // Limpar buffer

        Serie S = arqSerie.read(idSerieSelecionada);
        if (S == null) {
            System.out.println("Série não encontrada!");
            return;
        }
        
        if (S.getIDator() == 0) {
            System.out.println("A série não está vinculada a nenhuma ator!");
            return;
        }

        String AtorNome = viewAtor.lerNomeAtor();
        if (AtorNome.trim().isEmpty()) {
            System.out.println("Termo de busca inválido!");
            return;
        }

        // Realizar a busca
        ArrayList<Ator> resultados = relacionamentoAtorSerie.buscarAtorPorNome(AtorNome);

        // Exibir resultados
        viewAtor.mostraResultadoBuscaAtores(resultados);

        if (resultados.isEmpty()) {
            System.out.println("Nenhum ator encontrado com o nome: " + AtorNome);
            return;
        }

        // Selecionar Ator para deslinkar
        System.out.print("\nDigite o ID do ator que deseja deslinkar de uma série (0 para cancelar): ");
        int idSelecionado = sc.nextInt();
        sc.nextLine(); // Limpar buffer

        Ator A = arqAtor.read(idSelecionado);
        if (A == null) {
            System.out.println("Ator não encontrado!");
            return;
        }
        
        if (A.getIDSerie() == 0) {
            System.out.println("O ator não está vinculado a nenhuma série!");
            return;
        }

        if (A.getIDSerie() != S.getId()) {
            System.out.println("O ator não está vinculado a esta série!");
            return;
        }

        // Remover o relacionamento
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
