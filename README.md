[![Build Status](https://travis-ci.org/WenderGalan/desafio-compasso-uol.svg?branch=main)](https://travis-ci.org/WenderGalan/desafio-compasso-uol) 
[![codecov](https://codecov.io/gh/WenderGalan/desafio-compasso-uol/branch/main/graph/badge.svg?token=XSHUL7MQE3)](https://codecov.io/gh/WenderGalan/desafio-compasso-uol)

# Desafio Compasso UOL
Desafio Compasso UOL para vaga de desevolvedor back-end Pleno/Sênior.

- Informações do projeto:
    - O mesmo está integrado ao [Travis CI (Integração continua)](https://travis-ci.org/github/WenderGalan/desafio-compasso-uol), [HEROKU (Deploy automático)](https://desafio-compasso-uol.herokuapp.com/swagger-ui.html), e [CodeCov (Cobertura de código)](https://codecov.io/gh/WenderGalan/desafio-compasso-uol). Tudo de forma automática.
    - Para fazer o teste da aplicação basta [clicar aqui](https://desafio-compasso-uol.herokuapp.com/swagger-ui.html) que será redirecionado a API do heroku é possível que demore alguns segundos para iniciar.
    - O deploy deixei de forma automática no heroku, caso os testes falhem não irá fazer o deploy.
    - Todo o projeto foi feito utilizando TDD para desenvolver a aplicação.

#### Como rodar o projeto na sua máquina?
Assumindo que já tenha o ambiente de desenvolvimento com o JAVA 11, Postgres instalados e configurados você pode entrar no application.properties do projeto e alterar as propriedades para as informações do seu ambiente, e rodar o comando abaixo:
```
   mvn spring-boot:run
```
Para rodar os testes na máquina execute 

```
mvn test
```

#### Utilizando o docker

Assumindo que já tenha o Docker instalado na máquina, execute os procedimentos abaixo.

- Abra o console na pasta raiz da aplicação e execute o comando abaixo (A configuração do Dockerfile está um pouco demorada por ter que baixar as dependências do projeto e buildar o mesmo no container.):

```
  docker-compose up
```

No seu browser acesse as urls:
- Back-end: http://localhost:8080/swagger-ui.html
    - USER: admin
    - PASSWORD: admin
- Banco de dados: http://localhost:8080/h2
    - Para acessar a interface apenas clique em conectar.

#### Dependências:
- JPA Data: Gerenciamento das entidades da aplicação
- Spring Security: Segurança da api (Basic Auth)
- Spring Web
- Spring DevTools
- Lombok: Biblioteca para evitar escrita de código desnecessário
- Swagger: Documentar a API
- H2: Banco de dados em memória para testes e para rodar a aplicação.
- ModelMapper: Biblioteca para fazer as conversões dos DTOs para as entidades e vice-versa.

#### Integração contínua: [Travis CI](https://travis-ci.org/)
- O Travis CI é um serviço web de Integração Contínua na nuvem integrado com o GitHub.

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://desafio-compasso-uol.herokuapp.com/swagger-ui.html)

- ### License:
```
Copyright 2021 Wender Galan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```