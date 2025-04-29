package TP2.Service;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import TP2.Model.Serie;
import TP2.Model.Ator;

public class RelacionamentoSerieAtor {

    private Arquivo<Ator> arqAtores;
    private Arquivo<Serie> arqSerie;
    public ArvoreBMais<ParIDSerieAtor> arvoreRelacionamento; // Tornada pública para acesso de diagnóstico

    public void testarInsercaoArvore() {
        try {
            // Cria um par simples para teste
            ParIDSerieAtor par = new ParIDSerieAtor(1, 1);

            // Tenta inserir na árvore
            boolean resultado = arvoreRelacionamento.create(par);

            System.out.println("Resultado da inserção de teste: " + resultado);

            // Verifica se o par foi inserido
            ArrayList<ParIDSerieAtor> busca = arvoreRelacionamento.read(par);
            System.out.println("Elementos encontrados na busca: " + busca.size() + busca.toString());

        } catch (Exception e) {
            System.err.println("Erro no teste de inserção: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public RelacionamentoSerieAtor(Arquivo<Serie> arqSerie, Arquivo<Ator> arqAtores) {
        this.arqSerie = arqSerie;
        this.arqAtores = arqAtores;

        try {
            // IMPORTANTE: Usando caminho absoluto para garantir consistência
            String diretorioAtual = System.getProperty("user.dir");
            String caminhoData = diretorioAtual + "/TP2/dados";
            
            // Verificar se o diretório de dados existe
            java.io.File dataDir = new java.io.File(caminhoData);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
                System.out.println("Diretório de dados criado: " + caminhoData);
            }

            // Usar o caminho completo e absolutamente correto para o arquivo
            String arquivoIndice = caminhoData + "/serieAtor.db";
            
            // Verificar se o arquivo de índice existe
            java.io.File idxFile = new java.io.File(arquivoIndice);
            boolean arquivoNovo = !idxFile.exists();

            // Inicializa a árvore B+ para relacionamento
            Constructor<ParIDSerieAtor> construtor = ParIDSerieAtor.class.getConstructor();
            this.arvoreRelacionamento = new ArvoreBMais<>(
                    construtor,
                    4, // Ordem da árvore
                    arquivoIndice
            );

            // Se o arquivo for novo, inicializa com um registro dummy
            if (arquivoNovo) {
                try {
                    testarInsercaoArvore();
                } catch (Exception e) {
                    System.err.println("Erro ao inicializar árvore B+: " + e.getMessage());
                }
            } else {
                // Verificar estado da árvore
                verificarIntegridadeArvore();
            }

            // Verificar se a árvore funciona corretamente com um teste básico
            try {
                
                // Criar um par de teste
                ParIDSerieAtor parTeste = new ParIDSerieAtor(999, 999);
                
                // Inserir o par na árvore
                boolean resultadoInsercao = arvoreRelacionamento.create(parTeste);
                
                // Buscar o par inserido
                ArrayList<ParIDSerieAtor> resultadoBusca = arvoreRelacionamento.read(parTeste);
                boolean parEncontrado = false;
                for (ParIDSerieAtor par : resultadoBusca) {
                    if (par.getIdSerie() == 999 && par.getIdAtor() == 999) {
                        parEncontrado = true;
                        break;
                    }
                }
                
                // Remover o par de teste
                boolean resultadoRemocao = arvoreRelacionamento.delete(parTeste);
                
                // Verificar se a árvore está funcional
                if (!(resultadoInsercao && parEncontrado && resultadoRemocao)) {
                    reconstruirIndice();
                } 
            } catch (Exception e) {
                System.err.println("ERRO no teste de sanidade da árvore B+: " + e.getMessage());
                e.printStackTrace();
                
                // Se o teste falhar, tenta reconstruir o índice
                reconstruirIndice();
            }

        } catch (Exception e) {
            System.err.println("Erro ao inicializar árvore B+: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void verificarIntegridadeArvore() {
        try {
            boolean vazia = arvoreRelacionamento.empty();
            if (vazia) {
                // Tentar reconstruir o índice automaticamente
                reconstruirIndice();
            }
        } catch (Exception e) {
            System.err.println("Erro ao verificar integridade da árvore: " + e.getMessage());
        }
    }

    // Método para reconstruir o índice completo
    private void reconstruirIndice() {
        try {
            int totalRegistros = 0;

            // Percorre todos os atisódios e reconstrói a árvore
            int ultimoIdAtor = arqAtores.ultimoId();
            for (int i = 1; i <= ultimoIdAtor; i++) {
                Ator at = arqAtores.read(i);
                if (at != null && at.getIDSerie() > 0) {
                    try {
                        ParIDSerieAtor par = new ParIDSerieAtor(at.getIDSerie(), at.getId());
                        arvoreRelacionamento.create(par);
                        totalRegistros++;
                    } catch (Exception e) {
                        System.err.println("Erro ao inserir atisódio " + i + " no índice: " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Falha na reconstrução do índice: " + e.getMessage());
        }
    }
    
    // Adicionar um relacionamento entre série e atisódio
    public boolean adicionarRelacionamento(int idSerie, int idAtor) throws Exception {
        // Verifica se a série existe
        Serie serie = arqSerie.read(idSerie);
        if (serie == null) {
            System.err.println("ERRO: Série ID " + idSerie + " não encontrada ao adicionar relacionamento");
            return false;
        }

        // Verifica se o atisódio existe
        Ator Ator = arqAtores.read(idAtor);
        if (Ator == null) {
            System.err.println("ERRO: atisódio ID " + idAtor + " não encontrado ao adicionar relacionamento");
            return false;
        }

        // Atualiza o ID da série no atisódio, se necessário
        if (Ator.getIDSerie() != idSerie) {
            Ator.setIDSerie(idSerie);
            arqAtores.update(Ator);
        }

        try {
            // Primeiro verifica se o relacionamento já existe na árvore
            ParIDSerieAtor parBusca = new ParIDSerieAtor(idSerie, idAtor);
            ArrayList<ParIDSerieAtor> pares = arvoreRelacionamento.read(parBusca);
            boolean jaExiste = false;

            // Verifica se o par exato já existe
            for (ParIDSerieAtor par : pares) {
                if (par.getIdSerie() == idSerie && par.getIdAtor() == idAtor) {
                    jaExiste = true;
                    break;
                }
            }

            // Se já existe, considera como sucesso
            if (jaExiste) {
                return true;
            }

            // Adiciona o relacionamento na árvore B+
            ParIDSerieAtor par = new ParIDSerieAtor(idSerie, idAtor);

            // Tente até 3 vezes para corrigir possíveis problemas temporários
            boolean resultado = false;
            int tentativas = 0;
            Exception ultimoErro = null;

            while (!resultado && tentativas < 3) {
                tentativas++;
                try {
                    // Verifica se a árvore está vazia antes de tentar inserir
                    boolean arvoreVazia = false;
                    try {
                        arvoreVazia = arvoreRelacionamento.empty();
                        if (arvoreVazia && tentativas == 1) {
                            // Se estiver vazia na primeira tentativa, tenta reconstruir o índice
                            reconstruirIndice();
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao verificar se a árvore está vazia: " + e.getMessage());
                    }


                        // Tenta inserir o elemento
                        resultado = arvoreRelacionamento.create(par);
                        System.out.println("Tentativa " + tentativas + ": " + (resultado ? "Sucesso" : "Falha"));

                    
                    // Verificar se a inserção realmente funcionou fazendo uma leitura direta
                    ArrayList<ParIDSerieAtor> testeLeitura = arvoreRelacionamento.read(par);
                    boolean encontrado = false;
                    for (ParIDSerieAtor p2 : testeLeitura) {
                        if (p2.getIdSerie() == par.getIdSerie() && p2.getIdAtor() == par.getIdAtor()) {
                            encontrado = true;
                            break;
                        }
                    }
                    System.out.println("Verificação de inserção: " + (encontrado ? "O par foi encontrado após inserção" : "O par NÃO foi encontrado após inserção!"));
                    
                    // Se não encontrou mesmo após o sucesso da inserção, algo está errado com a persistência
                    if (resultado && !encontrado) {
                        System.out.println("ALERTA: Árvore B+ retornou sucesso na inserção, mas elemento não foi encontrado datois.");
                        System.out.println("Isto pode indicar um problema de persistência ou corrupção da árvore.");
                        
                        // Forçar a inserção novamente apenas como teste
                        System.out.println("Tentando inserção forçada para diagnóstico...");
                        arvoreRelacionamento.create(new ParIDSerieAtor(idSerie, idAtor));
                        
                        // Verificar se agora o elemento foi inserido
                        testeLeitura = arvoreRelacionamento.read(par);
                        encontrado = false;
                        for (ParIDSerieAtor p2 : testeLeitura) {
                            if (p2.getIdSerie() == par.getIdSerie() && p2.getIdAtor() == par.getIdAtor()) {
                                encontrado = true;
                                break;
                            }
                        }
                        System.out.println("Após inserção forçada: " + (encontrado ? "SUCESSO" : "FALHA PERSISTENTE"));
                    }
                } catch (Exception e) {
                    ultimoErro = e;
                    System.err.println("Tentativa " + tentativas + " falhou: " + e.getMessage());

                    // Pequeno atraso entre tentativas
                    try { Thread.sleep(100); } catch (InterruptedException ie) { }
                }
            }

            if (!resultado && ultimoErro != null) {
                throw ultimoErro;
            }

            return resultado;
        } catch (Exception e) {
            System.err.println("ERRO ao adicionar relacionamento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Remover um relacionamento entre série e atisódio
    public boolean removerRelacionamento(int idSerie, int idAtor) throws Exception {
        ParIDSerieAtor par = new ParIDSerieAtor(idSerie, idAtor);
        return arvoreRelacionamento.delete(par);
    }
    
    // Verificar se uma série tem atisódios
    public boolean serieTemAtores(int idSerie) throws Exception {
        boolean resultado = false;
        
        try {
            // Tentativa 1: Usar a árvore B+
            System.out.println("Verificando se série ID=" + idSerie + " tem atisódios via árvore B+");
            ParIDSerieAtor parBusca = new ParIDSerieAtor(idSerie, -1);
            ArrayList<ParIDSerieAtor> pares = arvoreRelacionamento.read(parBusca);
            
            System.out.println("Árvore B+ retornou " + pares.size() + " pares para a série ID=" + idSerie);
            
            // Verifica se algum par com o idSerie foi encontrado
            for (ParIDSerieAtor par : pares) {
                if (par.getIdSerie() == idSerie) {
                    System.out.println("Encontrado atisódio ID=" + par.getIdAtor() + " para série ID=" + idSerie);
                    resultado = true;
                    break;
                }
            }
            
            // Se não encontrar nada pela árvore, tenta método alternativo
            if (!resultado) {
                System.out.println("Árvore B+ não encontrou atisódios. Usando busca alternativa...");
                resultado = verificarAtorsPorBuscaDireta(idSerie);
            }
            
            return resultado;
        } catch (Exception e) {
            System.err.println("Erro ao verificar atisódios pela árvore B+: " + e.getMessage());
            
            // Em caso de erro, usa o método alternativo
            return verificarAtorsPorBuscaDireta(idSerie);
        }
    }
    
    // Método alternativo para verificar atisódios por busca direta
    private boolean verificarAtorsPorBuscaDireta(int idSerie) throws Exception {
        System.out.println("Verificando atisódios por busca direta para série ID=" + idSerie);
        
        int ultimoId = arqAtores.ultimoId();
        for (int i = 1; i <= ultimoId; i++) {
            Ator at = arqAtores.read(i);
            if (at != null && at.getIDSerie() == idSerie) {
                System.out.println("Encontrado atisódio ID=" + at.getId() + " (busca direta)");
                
                // Tenta corrigir a árvore B+
                System.out.println("Corrigindo relação na árvore B+ para série ID=" + idSerie + 
                                   ", atisódio ID=" + at.getId());
                adicionarRelacionamento(idSerie, at.getId());
                
                return true;
            }
        }
        
        System.out.println("Nenhum atisódio encontrado para série ID=" + idSerie + " (busca direta)");
        return false;
    }
    
    // Obter todos os atisódios de uma série
    public ArrayList<Ator> getAtoresDaSerie(int idSerie) throws Exception {
        ArrayList<Ator> Ators = new ArrayList<>();
        
        try {
            // Abordagem principal: Usar a árvore B+ para buscar atores
            ParIDSerieAtor parBusca = new ParIDSerieAtor(idSerie, -1);
            ArrayList<ParIDSerieAtor> pares = arvoreRelacionamento.read(parBusca);
                        
            // Para cada par encontrado, recupera o atisódio
            for (ParIDSerieAtor par : pares) {
                if (par.getIdSerie() == idSerie) { // Verifica se o ID da série corresponde
                    Ator at = arqAtores.read(par.getIdAtor());
                    if (at != null) {
                        Ators.add(at);
                    }
                } else {
                    // Como a árvore B+ retorna todos os elementos maiores ou iguais,
                    // podemos parar quando o idSerie for diferente
                    break;
                }
            }
            
            // Se não encontrou atisódios pela árvore, tenta o método alternativo
            if (Ators.isEmpty()) {
                
                // Busca linear: verificar todos os atisódios diretamente no arquivo
                int ultimoId = arqAtores.ultimoId();
                for (int i = 1; i <= ultimoId; i++) {
                    Ator at = arqAtores.read(i);
                    if (at != null && at.getIDSerie() == idSerie) {
                        Ators.add(at);
                        
                        // Tenta corrigir a árvore B+ adicionando o relacionamento que faltava
                        adicionarRelacionamento(idSerie, at.getId());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("ERRO na busca por atores: " + e.getMessage());
            
            // Em caso de erro, tenta o método alternativo
            int ultimoId = arqAtores.ultimoId();
            for (int i = 1; i <= ultimoId; i++) {
                Ator at = arqAtores.read(i);
                if (at != null && at.getIDSerie() == idSerie) {
                    Ators.add(at);
                }
            }
        }
        
        return Ators;
    }
    
    // Atualizar os índices após criação, atualização ou remoção de atisódios
    public void atualizarIndicesAposOperacao(Ator Ator, String operacao) throws Exception {
        int idSerie = Ator.getIDSerie();
        int idAtor = Ator.getId();
        
        switch (operacao.toLowerCase()) {
            case "create":
            case "update":
                boolean resultado = adicionarRelacionamento(idSerie, idAtor);
                if (!resultado) {
                    System.err.println("AVISO: Falha ao adicionar relacionamento entre série " + idSerie + 
                                     " e atisódio " + idAtor + " à árvore B+");
                }
                break;
            case "delete":
                removerRelacionamento(idSerie, idAtor);
                break;
        }
    }
    
    // Obter o número total de atisódios de uma série
    public int getTotalAtors(int idSerie) throws Exception {
        return getAtoresDaSerie(idSerie).size();
    }
    
    
    // Buscar série por nome (parcial ou completo)
    public ArrayList<Serie> buscarSeriePorNome(String nomaParcial) throws Exception {
        ArrayList<Serie> seriesEncontradas = new ArrayList<>();
        int ultimoId = arqSerie.ultimoId();
        
        // Converter para minúsculo para busca case-insensitive
        String termoBusca = nomaParcial.toLowerCase();
        
        for (int i = 1; i <= ultimoId; i++) {
            Serie serie = arqSerie.read(i);
            if (serie != null && serie.getNome().toLowerCase().contains(termoBusca)) {
                seriesEncontradas.add(serie);
            }
        }
        
        return seriesEncontradas;
    }
    
    // Buscar atisódio por nome (parcial ou completo)
    public ArrayList<Ator> buscarAtorPorNome(String nomatarcial) throws Exception {
        ArrayList<Ator> AtorsEncontrados = new ArrayList<>();
        int ultimoId = arqAtores.ultimoId();
        
        // Converter para minúsculo para busca case-insensitive
        String termoBusca = nomatarcial.toLowerCase();
        
        for (int i = 1; i <= ultimoId; i++) {
            Ator Ator = arqAtores.read(i);
            if (Ator != null && Ator.getNome().toLowerCase().contains(termoBusca)) {
                AtorsEncontrados.add(Ator);
            }
        }
        
        return AtorsEncontrados;
    }
    
    // Buscar atisódios por nome em uma série específica
    public ArrayList<Ator> buscarAtorPorNomeEmSerie(String nomatarcial, int idSerie) throws Exception {
        ArrayList<Ator> AtorsEncontrados = new ArrayList<>();
        int ultimoId = arqAtores.ultimoId();
        
        // Converter para minúsculo para busca case-insensitive
        String termoBusca = nomatarcial.toLowerCase();
        
        for (int i = 1; i <= ultimoId; i++) {
            Ator Ator = arqAtores.read(i);
            if (Ator != null && 
                Ator.getIDSerie() == idSerie && 
                Ator.getNome().toLowerCase().contains(termoBusca)) {
                AtorsEncontrados.add(Ator);
            }
        }
        
        return AtorsEncontrados;
    }

}
