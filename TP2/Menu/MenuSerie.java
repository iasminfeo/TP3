package TP1.Menu;

import java.util.ArrayList;
import java.util.Scanner;

import TP1.Model.Episodio;
import TP1.Model.Serie;
import TP1.Service.*;
import TP1.View.*;

public class MenuSerie {
    public Scanner sc = new Scanner(System.in);
    Arquivo<Serie> arqSerie;
    Arquivo<Episodio> arqEpisodios;
    ViewSerie viewSerie;
    RelacionamentoSerieEpisodio relacionamento;


    public MenuSerie(Scanner sc, Arquivo<Serie> arqSerie, Arquivo<Episodio> arqEpisodio ) throws Exception{
        this.sc = sc;
        this.arqSerie = arqSerie;
        this.arqEpisodios = arqEpisodio;
        this.viewSerie = new ViewSerie(sc);
        this.relacionamento = new RelacionamentoSerieEpisodio(arqSerie,arqEpisodio);
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
            System.out.println("5 - Visualizar série com episódios por temporada");
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

}
