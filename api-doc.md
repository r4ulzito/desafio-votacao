# Sobre a Aplicação

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. Partindo deste
viés, o objetivo da aplicação é permitir a criação de tópicos que serão votados pelos associados através de sessões de
votação, as mesmas com um tempo delimitado.

Tudo foi desenvolvido em linguagem Java, com auxílio do framework [Spring Boot](https://spring.io/).

# Modelo de Entidades

![Untitled](https://i.postimg.cc/k5d5tgW5/modelos-entidades-desafio-votacao.png)

# Rotas

### ⇒ Associate

- **POST →** /associates
    - Cria um novo associado
    - Corpo da requisição

        ```
        {
            "name": "Exemplo Nome"
        }
        
        ```

- **GET →** /associates
    - Busca todos os associados

        ```
        //Response Body
        [
            {
                "id": 1,
                "name": "Associado 1"
            }
        [
        ```

### ⇒ Topic

- **POST →** /topics
    - Cria um novo tópico
    - Corpo da requisição

        ```
        {
            "title": "Exemplo Título",
            "description": "Exemplo Descrição"
        }
        ```

- **GET →** /topics
    - Busca todos os tópicos

        ```
        //Response Body
        [
            {
                "id": 1,
                "title": "Topico 1",
                "description": "Descrição do topico 1"
            }
        ]
        ```

- **GET →** /topics/result/{id}
    - Busca o resultado da votação de um determinado tópico
    - Exemplo:

        ```
        //Requisição http://localhost:8080//topics/results/1
        //Response Body
        {
            "totalVotes": 4,
            "yesVotes": 3,
            "noVotes": 1,
            "result": "YES"
        }
        ```

### ⇒ Session

- **POST →** /sessions
    - Cria uma nova seção
    - Corpo da requisição

        ```
        {
            "data_end": "2045-02-28T13:30:00",
            "topic_id": 1
        }
        ```

- **POST →** /sessions/vote
    - Cria um novo voto no tópico referente a sessão
    - Corpo da requisição

        ```
        {
            "session_id": 1,
            "associate_id": 1,
            "answer": "YES" // ou NO
        }
        ```

- **GET →** /sessions
    - Busca todas as sessões

        ```
        //Response Body
        [
            {
                "id": 1,
                "dateStart": "2023-03-02T18:01:28.742131",
                "dateEnd": "2028-02-28T13:30:00",
                "isOpen": true,
                "topic": {
                    "id": 1,
                    "title": "Topico 1",
                    "description": "Descrição do topico 1"
                }
            }
        ]
        ```

# Como usar?

### 1. Clone o projeto

```markdown
#No seu terminal
git clone https://github.com/r4ulzito/desafio-votacao.git
```

### 2. Configure o banco de dados

- Nesta aplicação foi utilizado o [MySQL](https://www.mysql.com/), banco de dados relacional
- É preciso criar um um arquivo chamado `application.properties` dentro do diretório `src/main/resources`
- Dentro dele é colocado as configuraçõe do banco de dados, você pode faze-la seguindo o exemplo do
  arquivo `application.template.properties` em `src/main/resources`

### 3. Inicie a aplicação
