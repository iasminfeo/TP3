package TP1.View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import TP1.Model.*;

public class ViewEpisodio {
    public Scanner sc = new Scanner(System.in);

    public ViewEpisodio(Scanner sc) {
        this.sc = sc;
    }

    public String lerNomeEpisodio() {
        System.out.println("Digite o nome do episódio: ");
        String nome = sc.nextLine();
        return nome;
    }

    public int lerIDEpisodio() {
        System.out.println("Digite o ID do episódio: ");
        int id = sc.nextInt();
        sc.nextLine();
        return id;
    }

    public Episodio incluirEpisodio(int idSerie) {
        System.out.println("Inclusão de episódio: ");
        String nome = "";
        int temporada = 0;
        LocalDate DataL = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        long duracao = 0;
        int NumeroEpisodio = 0;
        boolean dadosCorretos = false;

        System.out.print("Nome: ");
        nome = sc.nextLine();

        do {
            System.out.print("Temporada: ");
            temporada = sc.nextInt();
            sc.nextLine();
            if (temporada <= 0) {
                System.err.println("A temporada deve ser inteira e positiva.");
            }
        } while (temporada <= 0);

        do {
            dadosCorretos = false;
            System.out.println("Escreva a data de lançamento nesse formato (DD/MM/AAAA): ");
            String dataStr = sc.nextLine();
            try {
                DataL = LocalDate.parse(dataStr, formatter);
                dadosCorretos = true;
            } catch (Exception e) {
                System.err.println("Data inválida! Use o formato DD/MM/AAAA.");
            }
        } while (!dadosCorretos);

        do {
            dadosCorretos = false;
            System.out.print("Duração em minutos: ");
            if (sc.hasNextLong()) {
                duracao = sc.nextLong();
                dadosCorretos = true;
            } else {
                System.err.println("Duração inválida! Insira um número válido.");
            }
        } while (!dadosCorretos);

        do {
            System.out.println("Escolha o número do episódio:");
            NumeroEpisodio = sc.nextInt();
            sc.nextLine();
            if (NumeroEpisodio <= 0) {
                System.err.println("o número do episódio deve ser inteiro e positivo.");
            }
        } while (NumeroEpisodio <= 0);

        // Confirmar a criação
        System.out.println("\nConfirme os dados do episódio:");
        System.out.println("Título: " + nome);
        System.out.println("Temporada: " + temporada);
        System.out.println("Número do episódio: " + NumeroEpisodio);
        System.out.println("Data de lançamento: " + DataL.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Duração: " + duracao + " minutos");
        System.out.println("ID da Série: " + idSerie);

        /*System.out.print("\nConfirma a inclusão do episódio? (S/N) ");
        char resp = sc.nextLine().charAt(0);

        if (resp == 'S' || resp == 's') {
            Episodio E = new Episodio(nome, temporada, DataL, duracao, idSerie, NumeroEpisodio);
            return E;
        }*/

        System.out.print("\nConfirma a inclusão da série? (S/N) ");
        String resp = sc.nextLine().toUpperCase(); // Usar nextLine() para capturar a linha inteira
        System.out.println(resp);
        if (resp.isEmpty() || !(resp.equals("S") || resp.equals("N"))) {
            System.out.println("Resposta inválida. Por favor, digite 'S' para Sim ou 'N' para Não.");
            return null;
        }

        if (resp.equals("S")) {
            try {
                Episodio E = new Episodio(nome, temporada, DataL, duracao, idSerie, NumeroEpisodio);
                return E;
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir a série!");
                return null;
            }
        } else {
            System.out.println("Criação de série cancelada.");
            return null;
        }
       // return null;
    }

    public Episodio alterarEpisodio(int idSerie, Episodio E) {
        System.out.println("\nAlteração de episódio: ");

        if (E != null) {
            System.out.println("Episódio encontrado: ");
            mostraEpisodio(E);

            // Alteração de nome
            System.out.print("\nNovo nome (deixe em branco para manter o anterior): ");
            String novoNome = sc.nextLine();
            if (!novoNome.isEmpty()) {
                E.setNome(novoNome);
            }

            // Alteração de temporada
            System.out.print("\nNova temporada (deixe em branco para manter a anterior): ");
            String temporada = sc.nextLine();
            if (!temporada.isEmpty()) {
                try {
                    int x = Integer.parseInt(temporada);
                    E.setTemporada(x);
                } catch (NumberFormatException e) {
                    System.err.println("Temporada inválida! Valor mantido.");
                }
            }

            // Alteração de DataLançamento
            System.out.print("\nNova data de lançamento (DD/MM/AAAA) (deixe em branco para manter a anterior): ");
            String novaDataStr = sc.nextLine();
            LocalDate x = null;
            if (!novaDataStr.isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    x = LocalDate.parse(novaDataStr, formatter); // Atualiza a data de lançamento se fornecida
                    E.setDataLancamento(x);
                } catch (Exception e) {
                    System.err.println("Data inválida. Valor mantido.");
                }
            }

            // Alteração de duraçao
            System.out.print("\nNova duração (deixe em branco para manter a anterior): ");
            String duracao = sc.nextLine();
            if (!duracao.isEmpty()) {
                try {
                    long d = Long.parseLong(temporada);
                    E.setDuracao(d);
                } catch (NumberFormatException e) {
                    System.err.println("Duração inválida! Valor mantido.");
                }
            }

            // Alteração de numero do episodio
            System.out.print("\nNovo número do episódio (deixe em branco para manter o anterior): ");
            String EPnumero = sc.nextLine();
            if (!EPnumero.isEmpty()) {
                try {
                    int n = Integer.parseInt(EPnumero);
                    E.setNumeroEpisodio(n);
                } catch (NumberFormatException e) {
                    System.err.println("Número de episódio inválida! Valor mantido.");
                }
            }

            // Confirmar a criação
            System.out.println("\nConfirme os dados do episódio:");
            System.out.println("Título: " + novoNome);
            System.out.println("Temporada: " + temporada);
            System.out.println("Número episodio: " + EPnumero);
            System.out
                    .println("Data de lançamento: " + x.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Duração: " + duracao + " minutos");
            System.out.println("ID da Série: " + idSerie);

            // Confirmação da alteração
            System.out.print("\nConfirma as alterações? (S/N) ");
            char resp = sc.next().charAt(0);
            if (resp == 'S' || resp == 's') {
                // Salva as alterações no arquivo
                return E;
            } else {
                System.out.println("Alterações canceladas.");
                return null;
            }
        }
        return null;
    }

    public void mostraResultadoBuscaEpisodios(ArrayList<Episodio> episodios) {
        if (episodios.isEmpty()) {
            System.out.println("\nNenhum episódio encontrado com o termo informado.");
            return;
        }

        System.out.println("\n=== Episódios Encontrados ===");
        System.out.println("Total: " + episodios.size() + " episódio(s)");

        for (Episodio ep : episodios) {
            System.out.println("\nID: " + ep.getId() + " | Nome: " + ep.getNome());
            System.out.println("Temporada: " + ep.getTemporada() + "| Número episódio: " + ep.getNumero()
                    + "| ID da Série: " + ep.getIdSerie());
            System.out.println("Lançamento: " + ep.getDataLancamento() + " | Duração: " + ep.getDuracao() + " min");
        }
    }

    public void mostraEpisodio(Episodio E) {
        if (E == null) {
            System.out.println("Episódio não encontrado!");
            return;
        }
        System.out.println("\nDetalhes do episódio:");
        System.out.println("------------------------------------");
        System.out.printf("Nome do episódio....: %s%n", E.getNome());
        System.out.printf("Temporada ..........: %d%n", E.getTemporada());
        System.out.printf("Número do episodio......: %d%n", E.getNumero());
        System.out.printf("Duração em minutos..: %d%n", E.getDuracao());
        System.out.printf("Data de lançamento..: %s%n",
                E.getDataLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("------------------------------------");

    }

    public void mostraListaEpisodios(ArrayList<Episodio> episodios) {
        if (episodios.isEmpty()) {
            System.out.println("Nenhum episódio encontrado.");
            return;
        }

        System.out.println("\n=== Lista de Episódios ===\n");
        System.out.println("Total de episódios: " + episodios.size());

        for (Episodio ep : episodios) {
            mostraEpisodio(ep);
        }
    }

    public int selecionaEpisodioDoResultado(ArrayList<Episodio> episodios) {
        if (episodios.isEmpty()) {
            return -1;
        }

        if (episodios.size() == 1) {
            System.out.println("\nEpisódio selecionado automaticamente: " + episodios.get(0).getNome());
            return episodios.get(0).getId();
        }

        System.out.print("\nDigite o ID do episódio que deseja selecionar (0 para cancelar): ");
        int id = sc.nextInt();
        sc.nextLine(); // Limpar buffer

        // Verificar se o ID está na lista
        if (id != 0) {
            boolean idExiste = false;
            for (Episodio ep : episodios) {
                if (ep.getId() == id) {
                    idExiste = true;
                    break;
                }
            }

            if (!idExiste) {
                System.out.println("ID inválido! Por favor, selecione um ID da lista apresentada.");
                return selecionaEpisodioDoResultado(episodios); // Recursão para nova tentativa
            }
        }

        return id;
    }

}
