package tests;

import core.BaseTest;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthTests extends BaseTest {

    // 🔹 Cole o token gerado pelo Swagger
    private static final String ACCESS_TOKEN =  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjk5OTk5OTk5fQ" +
            ".HfC8o9LM6Y9h_HpG4HffA7J8QxvJpL7C9v7G9oWfP0M";;

    @Test
    public void test01_buscarPetsComToken() {
        given()
                .auth().oauth2(ACCESS_TOKEN)
                .queryParam("status", "available")
                .when()
                .get("/pet/findByStatus")
                .then()
                .log().all()
                .statusCode(200)
                .body("[0].id", notNullValue());
    }

    @Test
    public void test02_validarFormatoTokenRegex() {
        // Valida que o token é JWT com 3 partes em Base64
        org.junit.Assert.assertTrue(
                ACCESS_TOKEN.matches("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$")
        );
    }
}
