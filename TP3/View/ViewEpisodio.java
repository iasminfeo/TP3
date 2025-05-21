package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import Model.Episodio;

public class ViewEpisodio {
    public Scanner sc = new Scanner(System.in);

    public ViewEpisodio(Scanner sc) {
        this.sc = sc;
    }

    public String lerNomeEpisodio() {
        System.out.print("Digite o nome do episódio: ");
        return sc.nextLine();
    }

    public int lerIDEpisodio() {
        System.out.print("Digite o ID do episódio: ");
        int id = sc.nextInt();
        sc.nextLine();
        return id;
    }

    public Episodio incluirEpisodio(int idSerie) {
        System.out.println("\nInclusão de episódio:");

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        int temporada = lerIntPositivo("Temporada");

        LocalDate dataLancamento = lerData("Data de lançamento (dd/MM/yyyy)");

        long duracao = lerLongPositivo("Duração (em minutos)");

        int numeroEpisodio = lerIntPositivo("Número do episódio");

        // Confirmar inclusão
        System.out.println("\n=== Confirmar Dados ===");
        System.out.println("Título: " + nome);
        System.out.println("Temporada: " + temporada);
        System.out.println("Número do episódio: " + numeroEpisodio);
        System.out.println("Data de lançamento: " + dataLancamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Duração: " + duracao + " minutos");
        System.out.println("ID da série: " + idSerie);

        System.out.print("\nConfirma a inclusão? (S/N): ");
        String resp = sc.nextLine().toUpperCase();

        if (resp.equals("S")) {
            return new Episodio(nome, temporada, dataLancamento, duracao, idSerie, numeroEpisodio);
        } else {
            System.out.println("Inclusão cancelada.");
            return null;
        }
    }

    public Episodio alterarEpisodio(int idSerie, Episodio E) {
        System.out.println("\nAlteração de episódio:");
        mostraEpisodio(E);

        System.out.print("\nNovo nome (ENTER para manter): ");
        String nome = sc.nextLine();
        if (!nome.isBlank()) E.setNome(nome);

        System.out.print("Nova temporada (ENTER para manter): ");
        String tempStr = sc.nextLine();
        if (!tempStr.isBlank()) {
            try { E.setTemporada(Integer.parseInt(tempStr)); }
            catch (Exception ex) { System.out.println("Valor inválido. Mantido."); }
        }

        System.out.print("Nova data de lançamento (dd/MM/yyyy) (ENTER para manter): ");
        String dataStr = sc.nextLine();
        if (!dataStr.isBlank()) {
            try {
                LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                E.setDataLancamento(data);
            } catch (Exception ex) {
                System.out.println("Data inválida. Mantido.");
            }
        }

        System.out.print("Nova duração (em minutos) (ENTER para manter): ");
        String durStr = sc.nextLine();
        if (!durStr.isBlank()) {
            try { E.setDuracao(Long.parseLong(durStr)); }
            catch (Exception ex) { System.out.println("Valor inválido. Mantido."); }
        }

        System.out.print("Novo número do episódio (ENTER para manter): ");
        String numStr = sc.nextLine();
        if (!numStr.isBlank()) {
            try { E.setNumeroEpisodio(Integer.parseInt(numStr)); }
            catch (Exception ex) { System.out.println("Valor inválido. Mantido."); }
        }

        System.out.print("\nConfirma as alterações? (S/N): ");
        String resp = sc.nextLine().toUpperCase();
        if (resp.equals("S")) {
            return E;
        } else {
            System.out.println("Alteração cancelada.");
            return null;
        }
    }

    public void mostraResultadoBuscaEpisodios(ArrayList<Episodio> episodios) {
        if (episodios.isEmpty()) {
            System.out.println("\nNenhum episódio encontrado.");
            return;
        }

        System.out.println("\n=== Episódios Encontrados ===");
        for (Episodio ep : episodios) {
            mostraEpisodio(ep);
        }
    }

    public void mostraEpisodio(Episodio E) {
        if (E == null) {
            System.out.println("Episódio não encontrado.");
            return;
        }

        System.out.println("\n---------------------------");
        System.out.println("ID...............: " + E.getId());
        System.out.println("Nome.............: " + E.getNome());
        System.out.println("Temporada........: " + E.getTemporada());
        System.out.println("Número episódio..: " + E.getNumero());
        System.out.println("Data lançamento..: " + E.getDataLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Duração..........: " + E.getDuracao() + " min");
        System.out.println("ID da série......: " + E.getIdSerie());
        System.out.println("---------------------------");
    }

    public void mostraListaEpisodios(ArrayList<Episodio> episodios) {
        if (episodios.isEmpty()) {
            System.out.println("\nNenhum episódio encontrado.");
            return;
        }

        System.out.println("\n=== Lista de Episódios ===");
        for (Episodio ep : episodios) {
            mostraEpisodio(ep);
        }
    }

    public int selecionaEpisodioDoResultado(ArrayList<Episodio> episodios) {
        if (episodios.isEmpty()) return -1;

        if (episodios.size() == 1) {
            System.out.println("\nEpisódio selecionado automaticamente: " + episodios.get(0).getNome());
            return episodios.get(0).getId();
        }

        System.out.print("\nDigite o ID do episódio que deseja selecionar (0 para cancelar): ");
        int id = sc.nextInt();
        sc.nextLine();

        boolean existe = episodios.stream().anyMatch(ep -> ep.getId() == id);

        if (!existe && id != 0) {
            System.out.println("ID inválido. Tente novamente.");
            return selecionaEpisodioDoResultado(episodios);
        }

        return id;
    }

    // ----------------------
    // Funções auxiliares
    // ----------------------

    private int lerIntPositivo(String mensagem) {
        int valor;
        do {
            System.out.print(mensagem + ": ");
            while (!sc.hasNextInt()) {
                System.out.print("Digite um número inteiro válido. " + mensagem + ": ");
                sc.next();
            }
            valor = sc.nextInt();
            sc.nextLine();
            if (valor <= 0) {
                System.out.println("O valor deve ser positivo.");
            }
        } while (valor <= 0);
        return valor;
    }

    private long lerLongPositivo(String mensagem) {
        long valor;
        do {
            System.out.print(mensagem + ": ");
            while (!sc.hasNextLong()) {
                System.out.print("Digite um número válido. " + mensagem + ": ");
                sc.next();
            }
            valor = sc.nextLong();
            sc.nextLine();
            if (valor <= 0) {
                System.out.println("O valor deve ser positivo.");
            }
        } while (valor <= 0);
        return valor;
    }

    private LocalDate lerData(String mensagem) {
        LocalDate data = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean ok = false;
        do {
            System.out.print(mensagem + ": ");
            String entrada = sc.nextLine();
            try {
                data = LocalDate.parse(entrada, formatter);
                ok = true;
            } catch (Exception e) {
                System.out.println("Data inválida! Use o formato dd/MM/yyyy.");
            }
        } while (!ok);
        return data;
    }
}
