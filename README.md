# Testes na API do TMDB com REST Assured

## Sobre o projeto

Essa é uma aplicação de testes automatizados na API do TMDB (The Movie DataBase), que é uma base de dados com informações sobre diversos filmes e séries de TV. Os testes foram aplicados em alguns endpoints da API, cujos resultados são disponibilizados em relatório gerado pelo Allure. 

Além disso, foi desenvolvida uma pipeline no Github Actions para que os testes sejam executados e o relatórios sejam gerados a cada push ou pull request na branch main do repositório.

## Link da API
https://developer.themoviedb.org/docs

## Tecnologias utilizadas

- Rest Assured
- Allure Report
- Github Actions

## Endpoints com testes automatizados


- [POST] Adicionar filme em uma lista específica (/list/{list_id}/add_item)
- [POST] Criar uma lista (/list)
- [POST] Limpar uma lista específica (/list/{list_id}/clear)
- [POST] Remover filme de uma lista específica (/list/{list_id}/remove_item)
- [GET] Ver detalhes de uma lista específica (/list/{list_id})
- [GET] Listar todas as listas de filmes do usuário (/account/{account_id}/lists)
- [DELETE] Excluir uma lista específica (/list/{list_id})

## Relatório gerado
![image](https://github.com/pauloliveiram/tmdb-rest-assured/assets/39312072/b830d3ca-00de-462c-8521-1c2efd56649f)

## Como executar os testes

### Pré-requisitos

- Java 8+
- Maven
- É necessário gerar um token de autenticação no site da API.

```bash

# Clonar repositório
git clone https://github.com/pauloliveiram/tmdb-rest-assured.git

# Entrar na pasta do projeto
cd tmdb-rest-assured

# Armazenar o token de autenticação como variável de ambiente
export TMDB_BEARER_TOKEN=valor-do-token

# Executar os testes
mvn clean test
					
# Gerar os relatórios do Allure
allure serve target/surefire-reports		
```

# Autor
Paulo Oliveira
