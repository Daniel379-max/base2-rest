🐾 Petstore API Automated Tests
Este projeto implementa testes automatizados de API REST para a Swagger Petstore utilizando Java + JUnit + RestAssured.

📋 Requisitos do Desafio
✅ Implementar 50 scripts de testes para uma API REST

✅ Alguns testes devem ler dados de uma planilha Excel (Data-Driven)

✅ 50 scripts podem cobrir mais de 50 CTs via Data-Driven

✅ O projeto deve tratar autenticação OAuth2

✅ Pelo menos um teste com Regex (Expressões Regulares)

📂 Estrutura do Projeto
bash
Copiar
Editar
src
├── main
│    └── java
│         └── models
│              ├── Pet.java
│              ├── Store.java
│              └── User.java
│
└── test
├── java
│    └── tests
│         ├── PetTests.java       # 20 testes
│         ├── StoreTests.java     # 15 testes
│         ├── UserTests.java      # 15 testes (com Data-Driven)
│         └── AuthTests.java      # OAuth2 + Regex
│
└── resources
├── usuarios.xlsx            # 20 linhas para Data-Driven
└── dog.jpeg                 # Arquivo de upload
🛠 Tecnologias e Dependências
Java 11+

JUnit 4

RestAssured

Apache POI (Data-Driven com Excel)

Hamcrest (validações e Regex)

Log4j2 (logs opcionais)

Exemplo pom.xml (trecho)
xml
Copiar
Editar
<dependencies>
<!-- JUnit -->
<dependency>
<groupId>junit</groupId>
<artifactId>junit</artifactId>
<version>4.13.2</version>
<scope>test</scope>
</dependency>

    <!-- RestAssured -->
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>5.3.2</version>
        <scope>test</scope>
    </dependency>

    <!-- Hamcrest -->
    <dependency>
        <groupId>org.hamcrest</groupId>
        <artifactId>hamcrest</artifactId>
        <version>2.2</version>
        <scope>test</scope>
    </dependency>

    <!-- Apache POI para Excel -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.5</version>
    </dependency>

    <!-- Log4j2 (opcional para logs) -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.21.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.21.1</version>
    </dependency>
</dependencies>
🚀 Execução dos Testes
Clonar o repositório

Rodar os testes via Maven ou IDE:

bash
Copiar
Editar
mvn test
Executar uma suíte completa (se existir AllTestsSuite.java):

bash
Copiar
Editar
mvn -Dtest=AllTestsSuite test
🔹 Destaques do Projeto
PetTests (20) → CRUD de pets, uploads, múltiplas fotos/tags

StoreTests (15) → Pedidos, estoque, deleções e variações de quantidade

UserTests (15) → CRUD de usuários, login, criação em lote com Excel Data-Driven

AuthTests (OAuth2 + Regex)

Uso de .auth().oauth2(token)

Validação de token com Regex (JWT)

📊 Cobertura de Requisitos
50 scripts de teste → ✔

Data-Driven com Excel → ✔

Mais de 50 CTs cobertos via Data-Driven → ✔

Autenticação OAuth2 demonstrada → ✔

Validação com Regex → ✔

🏆 Resultado
O projeto cumpre 100% dos requisitos do desafio, com boa organização, logs detalhados, reuso de código via BaseTest e planilha Excel para Data-Driven Testing.

