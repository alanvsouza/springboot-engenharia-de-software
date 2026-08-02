# Tutorial: como rodar e testar o projeto (validação de formulário)

Este tutorial assume que você **não tem nada instalado** ainda. Ele cobre a pasta
`parte-2`, que é a API REST (Spring Boot + MySQL) para cadastro de pessoas físicas.

O que foi adicionado: validação automática dos dados enviados no cadastro
(`POST /entidade/v1/`) e na atualização (`PUT /entidade/v1/`), inspirada no guia oficial
do Spring [Validating Form Input](https://spring.io/guides/gs/validating-form-input).
Como este projeto é uma API REST (não uma página com formulário Thymeleaf, como no
guia original), a validação foi adaptada: em vez de recarregar uma página HTML com
erros, a API responde com **HTTP 400** e um JSON listando cada campo inválido e o
motivo. Também foi criada uma página de teste simples (`form.html`) para quem preferir
testar pelo navegador em vez de linha de comando.

---

## 1. Pré-requisitos (instalação do zero)

Você vai precisar de três coisas: **Java 21**, **Docker** (para o banco MySQL) e um
terminal. O Maven **não precisa ser instalado**, pois o projeto já traz o "Maven
Wrapper" (`mvnw`), que baixa e usa a versão correta automaticamente.

### 1.1 Instalar o Java 21

**Windows:**
1. Baixe o instalador do Eclipse Temurin 21 (JDK): https://adoptium.net/temurin/releases/?version=21
2. Execute o instalador e siga o padrão (marque a opção "Set JAVA_HOME" se disponível).
3. Abra um novo terminal (PowerShell ou CMD) e confirme:
   ```
   java -version
   ```
   Deve aparecer algo como `openjdk version "21..."`.

**macOS:**
```bash
brew install temurin@21
```
Depois confirme com `java -version`.

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-21-jdk
java -version
```

### 1.2 Instalar o Docker (para o banco de dados MySQL)

O projeto usa Docker Compose para subir o MySQL, então você não precisa instalar o
MySQL manualmente.

- **Windows/macOS:** instale o Docker Desktop: https://www.docker.com/products/docker-desktop/
- **Linux:** siga o guia oficial: https://docs.docker.com/engine/install/ (e também
  instale o plugin `docker compose`, geralmente incluído por padrão nas versões
  recentes).

Confirme a instalação:
```bash
docker --version
docker compose version
```

### 1.3 Descompactar o projeto

Descompacte o ZIP recebido em qualquer pasta e abra um terminal dentro dela. Você
verá as pastas `parte-1`, `parte-2` e `parte-3`. Entre na pasta `parte-2`:

```bash
cd springboot-engenharia-de-software/parte-2
```

---

## 2. Subir o banco de dados (MySQL via Docker)

Ainda dentro de `parte-2`, rode:

```bash
docker compose up -d
```

Isso baixa a imagem do MySQL 8.0 (na primeira vez pode demorar um pouco) e sobe um
container chamado `mysql-container`, já com o banco `springtutorialjpa` criado
(usuário `root`, senha `root-pass123`, porta `3306`).

Para conferir se está rodando:
```bash
docker ps
```
Você deve ver `mysql-container` com status `Up`.

---

## 3. Rodar a aplicação Spring Boot

Ainda em `parte-2`, use o Maven Wrapper (não precisa instalar Maven):

**Linux/macOS:**
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

**Windows:**
```bat
mvnw.cmd spring-boot:run
```

Aguarde até aparecer no terminal algo como:
```
Tomcat started on port 8084
Started SpringtutorialApplication in ...
```

A aplicação está no ar em **http://localhost:8084**.

---

## 4. Testando a validação

Existem três formas de testar. Escolha a que preferir — não é necessário fazer todas.

### 4.1 Pelo navegador (mais simples)

1. Abra http://localhost:8084 — você verá a página inicial, com links para o
   Swagger UI e para o **formulário de teste**.
2. Clique em "Cadastro de pessoa física" (ou acesse diretamente
   http://localhost:8084/form.html).
3. Tente enviar o formulário **vazio**: a API deve responder listando todos os
   campos obrigatórios que faltaram.
4. Preencha os campos, mas coloque um CPF com letras ou menos de 11 dígitos: deve
   aparecer o erro específico de CPF.
5. Agora preencha tudo corretamente (ex: CPF com 11 números, email válido, CEP com
   8 números) e envie: deve aparecer a mensagem de sucesso.

### 4.2 Pelo Swagger UI (documentação interativa)

1. Acesse http://localhost:8084/swagger-ui-entity
2. Abra o endpoint `POST /entidade/v1/`, clique em "Try it out"
3. Use o corpo de exemplo abaixo (propositalmente inválido) e clique em "Execute":
   ```json
   {
     "usuario": "",
     "senha": "123",
     "email": "nao-e-um-email",
     "nome": "A",
     "cpf": "123",
     "endereco": []
   }
   ```
4. Confira que a resposta tem status `400` e um JSON com um erro por campo.
5. Corrija os dados conforme o exemplo válido da seção 4.3 e envie novamente:
   a resposta deve ser `200` com o corpo `true`.

### 4.3 Por linha de comando (curl)

**Requisição inválida** (vários campos obrigatórios faltando/incorretos):
```bash
curl -i -X POST http://localhost:8084/entidade/v1/ \
  -H "Content-Type: application/json" \
  -d '{
        "usuario": "",
        "senha": "123",
        "email": "nao-e-um-email",
        "nome": "A",
        "cpf": "123",
        "endereco": []
      }'
```
Resposta esperada: `HTTP/1.1 400` e um JSON parecido com:
```json
{
  "usuario": "Usuário é obrigatório",
  "senha": "Senha deve ter ao menos 6 caracteres",
  "email": "Email inválido",
  "nome": "Nome deve ter entre 2 e 120 caracteres",
  "cpf": "CPF deve conter exatamente 11 dígitos numéricos"
}
```

**Requisição válida:**
```bash
curl -i -X POST http://localhost:8084/entidade/v1/ \
  -H "Content-Type: application/json" \
  -d '{
        "usuario": "joao.silva",
        "senha": "senha123",
        "email": "joao@example.com",
        "nome": "Joao da Silva",
        "cpf": "12345678900",
        "dataNascimento": "1990-05-20T00:00:00.000+0000",
        "endereco": [
          {
            "rua": "Rua das Flores",
            "numero": 100,
            "bairro": "Centro",
            "cep": "13400000",
            "cidade": "Piracicaba",
            "estado": "SP"
          }
        ]
      }'
```
Resposta esperada: `HTTP/1.1 200` e o corpo `true`.

---

## 5. Regras de validação aplicadas

| Campo (cadastro)     | Regra                                              |
|-----------------------|-----------------------------------------------------|
| `usuario`             | obrigatório                                        |
| `senha`                | obrigatório, mínimo de 6 caracteres                |
| `email`                | obrigatório, formato de email válido               |
| `nome`                 | obrigatório, entre 2 e 120 caracteres              |
| `cpf`                  | obrigatório, exatamente 11 dígitos numéricos       |
| `dataNascimento`       | obrigatório                                        |
| `endereco[].rua`       | obrigatório                                        |
| `endereco[].numero`    | deve ser maior que zero                            |
| `endereco[].bairro`    | obrigatório                                        |
| `endereco[].cep`       | obrigatório, exatamente 8 dígitos numéricos        |
| `endereco[].cidade`    | obrigatório                                        |
| `endereco[].estado`    | obrigatório, sigla com 2 letras (ex: `SP`)         |

---

## 6. Parando tudo

Para parar a aplicação: `Ctrl + C` no terminal onde ela está rodando.

Para parar e remover o banco de dados:
```bash
docker compose down
```
(use `docker compose down -v` se também quiser apagar os dados salvos no volume).

---

## 7. Arquivos alterados/criados nesta mudança

- `pom.xml` — adicionada a dependência `spring-boot-starter-validation`.
- `dto/FisicaDTO.java` e `dto/EnderecoDTO.java` — anotações de validação
  (`@NotBlank`, `@Size`, `@Email`, `@Pattern`, `@Positive`, `@Valid`).
- `resource/FisicaResource.java` — `@Valid` adicionado nos endpoints de
  criação (`POST`) e atualização (`PUT`).
- `resource/exception/GlobalControllerExceptionHandler.java` — novo handler
  que converte erros de validação em uma resposta `400` com um JSON
  `campo -> mensagem`.
- `static/form.html` — novo formulário simples para testar a validação pelo
  navegador.
- `static/index.html` — link adicionado para o novo formulário.
