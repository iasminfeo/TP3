package TP1.View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


import TP1.Model.Serie;


public class ViewSerie {
    public Scanner sc = new Scanner(System.in);

    public ViewSerie(Scanner sc) {
        this.sc = sc;
    }

    //public void buscarSerieNome() {  }

    public Serie incluirSerie() throws Exception {
        System.out.println("Inclusão de série: ");
        String Nome = "";
        LocalDate AnoLancamento = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String Sinopse = "";
        int SinopseSize = 0;
        String Streaming = "";
        int QtdTemporada = 0;
        boolean dadosCorretos = false;

        System.out.println("Nome:");
        Nome = sc.nextLine();

        do {
            dadosCorretos = false;
            System.out.println("Escreva o ano de lançamento nesse formato (DD/MM/AAAA): ");
            String dataStr = sc.nextLine();
            try {
                AnoLancamento = LocalDate.parse(dataStr, formatter);
                dadosCorretos = true;
            } catch (Exception e) {
                System.err.println("Data inválida! Use o formato DD/MM/AAAA.");
            }
        } while (!dadosCorretos);

        do {
            dadosCorretos = false;
            System.out.println("Escreva a sinopse: ");
            Sinopse = sc.nextLine();
            if (!Sinopse.isEmpty()) {
                dadosCorretos = true;
                SinopseSize = Sinopse.length();
            } else {
                System.err.println("Erro ao criar sinopse.");
            }
        } while (!dadosCorretos);

        do {
            System.out.println("Quantidade de temporada:");
            QtdTemporada = sc.nextInt();
            if (QtdTemporada <= 0) {
                System.err.println("A quantidade de temporada deve ser inteira e positiva.");
            }
        } while (QtdTemporada <= 0);

        do {
            dadosCorretos = false;
            System.out.println(
                    "Escolha o seu streaming: \n 1) Netflix \n 2) Amazon Prime Video \n 3) Max \n 4) Disney Plus \n 5) Globo Play \n 6) Star Plus");
            int opStreaming = 0;
            opStreaming = sc.nextInt();
            sc.nextLine();
            if (opStreaming == 1) {
                Streaming = "Netflix";
                dadosCorretos = true;
            }
            if (opStreaming == 2) {
                Streaming = "Amazon Prime Video";
                dadosCorretos = true;
            }
            if (opStreaming == 3) {
                Streaming = "Max";
                dadosCorretos = true;
            }
            if (opStreaming == 4) {
                Streaming = "Disney Plus";
                dadosCorretos = true;
            }
            if (opStreaming == 5) {
                Streaming = "Globo Play";
                dadosCorretos = true;
            }
            if (opStreaming == 6) {
                Streaming = "Star Plus";
                dadosCorretos = true;
            }
        } while (!dadosCorretos);

        System.out.print("\nConfirma a inclusão da série? (S/N) ");
        char resp = sc.next().charAt(0);

        if (resp == 'S' || resp == 's') {
            try {
                Serie S = new Serie(Nome, AnoLancamento, Sinopse, SinopseSize, Streaming, QtdTemporada);
                return S;
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir a série!");
                return null;
            }
        } else {
            System.out.println("Criação de série cancelada.");
            return null;
        }

    }

    public Serie alterarSerie(Serie S) {
        System.out.println("\nAlteração de série: ");

        // Alteração de nome
        System.out.print("\nNovo nome (deixe em branco para manter o anterior): ");
        String novoNome = sc.nextLine();
        if (!novoNome.isEmpty()) {
            S.setNome(novoNome);
        }

        // Alteração ano de lançamento
        System.out.print("\n Ano de lançamento (DD/MM/AAAA)  (deixe em branco para manter a anterior): ");
        String novaDataStr = sc.nextLine();
        if (!novaDataStr.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate x = LocalDate.parse(novaDataStr, formatter); // Atualiza a data de lançamento se
                S.setAnoLancamento(x);
                // fornecida
            } catch (Exception e) {
                System.err.println("Data inválida. Valor mantido.");
            }
        }

        // SInopse e sinopse size
        System.out.println("\n Escreva a nova sinopse(deixe em branco para manter a anterior): ");
        String sinopse = sc.nextLine();
        if (!sinopse.isEmpty()) {
            try {
                S.setSinopse(sinopse);
            } catch (Exception e) {
                System.err.println("Erro ao modificar sinopse! Sinopse mantida.");
            }
        }

        // Alteração de QTD temporada
        System.out.print("\n Quantidade de temporada(deixe em branco para manter a anterior): ");
        String temporada = sc.nextLine();
        if (!temporada.isEmpty()) {
            try {
                int x = Integer.parseInt(temporada);
                S.setQtdTemporada(x);
            } catch (NumberFormatException e) {
                System.err.println("Quantidade inválida! Valor mantido.");
            }
        }

        // Alteração streaming
        System.out.print("\n Escolha um streaming(Escolha a opção 0 se deseja manter o streaming): ");
        boolean dadosCorretos = false;
        String Streaming = S.getStreaming();
        do {
            dadosCorretos = false;
            System.out.println(
                    "Escolha o seu streaming: \n 1) Netflix \n 2) Amazon Prime Video \n 3) Max \n 4) Disney Plus \n 5) Globo Play \n 6) Star Plus");
            int opStreaming = 0;
            opStreaming = sc.nextInt();
            sc.nextLine();
            if (opStreaming == 0) {
                dadosCorretos = true;
            }
            if (opStreaming == 1) {
                Streaming = "Netflix";
                dadosCorretos = true;
            }
            if (opStreaming == 2) {
                Streaming = "Amazon Prime Video";
                dadosCorretos = true;
            }
            if (opStreaming == 3) {
                Streaming = "Max";
                dadosCorretos = true;
            }
            if (opStreaming == 4) {
                Streaming = "Disney Plus";
                dadosCorretos = true;
            }
            if (opStreaming == 5) {
                Streaming = "Globo Play";
                dadosCorretos = true;
            }
            if (opStreaming == 6) {
                Streaming = "Star Plus";
                dadosCorretos = true;
            }
            S.setStreaming(Streaming);
        } while (!dadosCorretos);

        // Confirmação da alteração
        System.out.print("\nConfirma as alterações? (S/N) ");
        char resp = sc.next().charAt(0);
        if (resp == 'S' || resp == 's') {
            return S;
        } else {
            return null;
        }

    }

    public int lerIDSerie(){
        System.out.println("Digite o ID da série:");
        int id = sc.nextInt();
        sc.nextLine(); // Limpar buffer
        return id;
    }

    public String LerNomeSerie(){
        System.out.println("Digite o nome da série desejda: ");
        String nome = sc.nextLine();
        return nome;
    }

    public void mostraSerie(Serie S) {
        if (S != null) {
            System.out.println("\nDetalhes da Série:");
            System.out.println("------------------------------------");
            System.out.printf("Nome da série..................: %s%n", S.getNome());
            System.out.printf("Quantidade de temporadas.......: %d%n", S.getQtdTemporada());
            System.out.printf("Sinopse........................: %s%n", S.getSinopse());
            System.out.printf("Streaming......................: %s%n", S.getStreaming());
            System.out.printf("Ano de lançamento..............: %s%n",
                    S.getAnoLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("------------------------------------");
        }
    }

    public void mostrarSeriesEncontradas(ArrayList<Serie> S){
        if(S.isEmpty()){
            System.out.println("Nenhuma série encontrada!");
            return;
        }

        System.out.println("Séries encontradas: ");
        System.out.println("Número de séries encontrda: " + S.size());

        for(Serie tmp : S){
            System.out.println("ID: " + tmp.getId() + " | " + tmp.getNome() + " | (" + tmp.getAnoLancamento() + ") | Streaming: " + tmp.getStreaming());
        }
    }

    public void mostraResultadoBuscaSeries(ArrayList<Serie> series) {
        if (series.isEmpty()) {
            System.out.println("\nNenhuma série encontrada com o termo informado.");
            return;
        }
        
        System.out.println("\n=== Séries Encontradas ===");
        System.out.println("Total: " + series.size() + " série(s)");
        
        for (Serie serie : series) {
            System.out.println("\nID: " + serie.getId() + " | " + serie.getNome() + " (" + serie.getAnoLancamento() + ")");
            System.out.println("Streaming: " + serie.getStreaming());
        }
    }
    
}
