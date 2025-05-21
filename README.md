# 🎬 PUCFlix 3.0 - Sistema de Gerenciamento de Séries, Atores e Episódios com Índice Invertido

## 📽️ O que o trabalho faz?

O **PUCFlix 3.0** é a evolução do projeto desenvolvido no TP2, agora com a implementação do **índice invertido** para tornar as buscas de séries, episódios e atores mais rápidas, inteligentes e eficientes.

Além dos relacionamentos já implementados (1:N entre séries e episódios e N:N entre séries e atores), o sistema agora permite que o usuário busque séries, episódios e atores **a partir de termos dos seus títulos ou nomes**, aplicando a lógica de **TFxIDF (Term Frequency — Inverse Document Frequency)**.

🔍 A busca agora não é mais por nome exato, mas sim por termos, processados e normalizados (sem acentos e em letras minúsculas), garantindo resultados mais relevantes, ordenados por peso de correspondência.

O sistema mantém a organização em arquitetura **MVC (Model-View-Controller)** e continua oferecendo todas as funcionalidades de cadastro, edição, exclusão e vinculação, mas agora com uma camada de busca muito mais poderosa.

---

## 🔥 Funcionalidades, Relacionamentos e Novidades do TP3

### ✅ **Novidade Principal**
- Implementação de **índices invertidos** nas entidades:
  - Série (títulos)
  - Episódio (títulos)
  - Ator (nomes)

### ✅ **Funcionamento do Índice Invertido**
- No momento da inclusão, alteração ou exclusão de qualquer entidade, as palavras dos títulos ou nomes são processadas:
  - Remoção de acentos
  - Transformação para minúsculo
  - Remoção de *stop words* (artigos, preposições, etc.)
- Para cada palavra, é criada uma lista com os IDs das entidades que possuem essa palavra, acompanhada de seu peso TF.
- Na busca, o sistema:
  - Calcula o **TFxIDF** de cada termo.
  - Retorna os resultados ordenados do mais relevante para o menos relevante.

---

## 🧠 Estruturas de Dados Utilizadas
- 🔗 **CRUD Genérico**: Persistência dos dados em arquivos binários.
- 🗂️ **Índice Direto**: Hash Extensível, mapeando ID para posição no arquivo.
- 🌳 **Índice Indireto**: Árvore B+ para relacionamentos (Série-Episódio e Série-Ator).
- 📜 **Índice Invertido**: Implementado usando a classe `ListaInvertida`, responsável pelas buscas textuais nas três entidades.

---

## 📂 Estrutura do Projeto

### 🧩 Model (Camada de Dados)
- **Serie.java**: Define os atributos da série. Participa dos relacionamentos e do índice invertido.
- **Episodio.java**: Define atributos dos episódios, vinculados à série.
- **Ator.java**: Define os atores, vinculados às séries.

### 🎮 Menu (Camada de Controle)
- **MenuSerie.java**
  - Busca por lista invertida.
  - CRUD completo.
  - Vinculação com atores.
- **MenuAtor.java**
  - Busca por lista invertida.
  - CRUD completo.
  - Gerencia relacionamento com séries.
- **MenuEpisodio.java**
  - Busca por lista invertida.
  - CRUD completo.
  - Gerencia episódios de uma série.

### 👁️ View (Camada de Interface)
- **ViewSerie.java**, **ViewAtor.java**, **ViewEpisodio.java**:
  - Responsáveis pela interação com o usuário, com visualização dos dados e confirmações.

### 🛠️ Service (Infraestrutura)
- **Arquivo.java**: CRUD genérico.
- **HashExtensivel.java**: Índice direto.
- **ArvoreBMais.java**: Relacionamentos 1:N e N:N.
- **ListaInvertida.java**: Implementa as listas de termos para o índice invertido.
- **IndiceInvertido.java**:
  - 🔥 Principal novidade do TP3.
  - Controla as listas invertidas de séries, episódios e atores.
  - Gerencia inserções, exclusões e buscas TFxIDF.
- **RelacionamentoSerieEpisodio.java** e **RelacionamentoSerieAtor.java**:
  - Gerenciam os relacionamentos entre as entidades.
- **StopWords.java**:
  - Gerencia as palavras irrelevantes para a busca.

---

## ✅ Requisitos Implementados no TP3
- 🔗 CRUD completo para séries, episódios e atores.
- 🔗 Relacionamento 1:N entre séries e episódios usando Árvore B+.
- 🔗 Relacionamento N:N entre séries e atores usando Árvore B+.
- 🔥 **Busca por termos (com TFxIDF) usando índice invertido nas três entidades.**
- 🔥 Atualização automática dos índices invertidos após inserções, alterações e exclusões.
- 🔍 Visualização dos resultados de busca ordenados por relevância (peso TFxIDF).
- 🔒 Restrições de integridade mantidas.
- 🎯 Interface de busca 100% migrada para o índice invertido.

---

## 🚀 Experiência e Desafios
> A implementação do índice invertido foi, sem dúvidas, o maior desafio desse trabalho. Tivemos que entender não só a lógica da frequência dos termos, mas também como calcular corretamente o IDF (inverso da frequência dos documentos) e aplicar o conceito de TFxIDF.  

> Além disso, precisávamos garantir que o índice invertido se mantivesse consistente com as operações CRUD, o que exigiu um bom controle das atualizações nas listas invertidas.

> A etapa mais desafiadora foi implementar corretamente a normalização dos textos e garantir que as buscas realmente ordenassem os resultados de forma correta pela relevância.  

> No fim, conseguimos implementar todas as funcionalidades solicitadas, mantendo a integridade dos dados e melhorando muito a performance e usabilidade do sistema.

---

## ✅ Checklist
| Pergunta | Resposta |
|----------|----------|
| O índice invertido com os termos dos títulos das séries foi criado usando a classe ListaInvertida? | ✅ Sim |
| O índice invertido com os termos dos títulos dos episódios foi criado usando a classe ListaInvertida? | ✅ Sim |
| O índice invertido com os termos dos nomes dos atores foi criado usando a classe ListaInvertida? | ✅ Sim |
| É possível buscar séries por palavras usando o índice invertido? | ✅ Sim |
| É possível buscar episódios por palavras usando o índice invertido? | ✅ Sim |
| É possível buscar atores por palavras usando o índice invertido? | ✅ Sim |
| O trabalho está completo? | ✅ Sim |
| O trabalho é original e não é cópia de nenhum colega? | ✅ Sim |

---

## 👨‍💻 Equipe
- Cauã Costa Alves
- Iasmin Ferreira e Oliveira
- Andriel Mark da Silva Pinto
