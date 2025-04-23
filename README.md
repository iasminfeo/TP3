# PUCFlix 1.0 - Sistema de Gerenciamento de Séries e Episódios

## 📽️ O que o trabalho faz?

O PUCFlix é um sistema de gerenciamento de séries de streaming, desenvolvido como parte da primeira tarefa prática da disciplina, cujo foco foi a implementação de um relacionamento 1:N entre duas entidades: Série e Episódio.

A aplicação permite ao usuário cadastrar, alterar, buscar e excluir informações sobre séries e seus respectivos episódios, por meio de uma interface simples e interativa. O sistema segue o padrão de arquitetura MVC (Model-View-Controller), separando claramente a lógica de controle, a interface com o usuário e o acesso aos dados.

**Funcionalidades, Relacionamentos e Estrutura:**


Cada série pode conter múltiplos episódios, o que caracteriza o relacionamento de um para muitos (1:N). Para isso, a entidade Episódio possui um ID de série como chave estrangeira, assegurando o vínculo entre as duas entidades. Também foi implementada uma Árvore B+ para registrar esse relacionamento, garantindo uma busca eficiente e estruturada dos episódios a partir das suas respectivas séries.

Além disso, o sistema conta com:

- Um CRUD genérico que opera sobre arquivos binários, com estrutura de lápide, tamanho do registro e vetor de bytes.

- Utilização da Tabela Hash Extensível para índice direto (por ID).

- Utilização da Árvore B+ para índice indireto (por nome e também para mapear os episódios por série).

- Restrições de integridade, como impedir a exclusão de uma série que ainda possui episódios vinculados.

- Uma visualização segmentada por temporada, permitindo que os episódios sejam organizados de forma intuitiva.

- Um menu interativo que facilita a navegação entre séries e episódios, mantendo uma boa experiência de uso.

- Persistência dos dados em arquivos binários


O desenvolvimento partiu da base fornecida em sala (CRUD genérico) e exigiu a adaptação e integração de estruturas mais complexas para manipulação dos dados de forma eficiente. O projeto foi entregue nesse repositório do GitHub, com toda a estrutura separada em pacotes e classes bem definidas, respeitando os princípios da disciplina e facilitando a manutenção e expansão futura.


## 👨‍💻 Equipe

- Cauã Costa Alves
- Iasmin Ferreira e Oliveira
- Andriel Mark da Silva Pinto

## 📂 Estrutura do Projeto

### 🧩 Model (Camada de Dados)

**Serie.java**
- Representa a entidade Série com atributos como título, ano, sinopse e plataforma
- Implementa métodos de serialização e desserialização para persistência em arquivo

**Episodio.java**
- Representa a entidade Episódio com atributos como título, temporada, data de lançamento e duração
- Contém o atributo `idSerie` como chave estrangeira para vinculação à série correspondente
- Implementa métodos de serialização e desserialização para persistência em arquivo

### 🎮 Menu (Camada de Controle)


**MenuEpisodio.java**
- Gerencia a lógica de negócio para episódios
- Métodos principais: `menuEpisodio()`, `gerenciarEpisodiosPorNome()`, `buscarEpisodioPorNome()`, `gerenciarEpisodiosDeSerie()`, `incluirEpisodio(int idSerie)`, `alterarEpisodioPorNome()`, `excluirEpisodioPorNome()´, `listarEpisodiosDaSerie()`
- Garante que episódios sejam vinculados apenas a séries existentes

**MenuSerie.java**
- Gerencia a lógica de negócio para séries
- Métodos principais: `menuSerie()`, `buscarSerieID()`, `incluirSerie()`, `excluirSerieNome()`, `alterarSerieID()`

### 👁️ View (Camada de Visualização)

**ViewSerie.java**
- Interface com o usuário para operações relacionadas a séries
- Métodos principais: `incluirSerie()`, `alterarSerie()`, `mostraSerie()`, `mostraResultadoBuscaSeries()`

**ViewEpisodio.java**
- Interface com o usuário para operações relacionadas a episódios
- Métodos principais: `lerNomeEpisodio()`, `incluirEpisodio()`, `alterarEpisodio()`, `mostraResultadoBuscaEpisodios()`, `mostraEpisodio`

### 🛠️ Service (Camada de Serviço/Infraestrutura)

**Arquivo.java**
- Implementa o CRUD genérico para manipulação de arquivos
- Utiliza HashExtensivel como índice direto para acesso rápido por ID

**HashExtensivel.java**
- Implementa a estrutura de dados Tabela Hash Extensível
- Utilizada como índice direto para acesso por ID

**ArvoreBMais.java**
- Implementa a estrutura de dados Árvore B+
- Utilizada para gerenciar o relacionamento entre séries e episódios

**ParIDSerieEpisodio.java**
- Representa o par (idSerie, idEpisodio) para uso na Árvore B+
- Implementa a interface `RegistroArvoreBMais`

**ParIDEndereco.java**
- Representa o par (id, endereco) para uso na Tabela Hash Extensível
- Implementa a interface `RegistroHashExtensivel`


### ✅ Requisitos Implementados

- CRUD completo para séries e episódios.

-Relacionamento 1:N entre séries e episódios utilizando uma Árvore B+.

-Funcionalidade de busca por nome em ambas as entidades.

-Visualização dos episódios organizados por temporada.

-Verificação de integridade referencial, assegurando a consistência dos dados.


## 📝 Conclusão

O processo de desenvolvimento do PUCFlix foi uma experiência valiosa, pois nos permitiu aplicar conceitos de estruturas de dados em um projeto prático. Apesar dos obstáculos enfrentados, especialmente com a Árvore B+, conseguimos avançar de forma significativa e aprender com os erros e acertos ao longo do caminho.

A estrutura em camadas (MVC) trouxe muitos benefícios, permitindo uma organização clara do código, facilitando a colaboração e a evolução do sistema. Adicionalmente, as estratégias de recuperação implementadas ajudaram a manter o sistema robusto, mesmo diante das falhas na árvore de dados.

