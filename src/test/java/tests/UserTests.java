package tests;

import core.BaseTest;
import models.User;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import utils.ExcelUtils;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTests extends BaseTest {

    private final String basePath = "/user";

    private User getUser(int id, String username, String email, String password) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName("Nome");
        user.setLastName("Sobrenome");
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone("11999999999");
        user.setUserStatus(0);
        return user;
    }

    // ---------- SUCESSO (7) ----------

    @Test
    public void test01_criarUsuarioComSucesso() {
        User user = getUser(1, "usuario1", "usuario1@email.com", "123456");

        given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("message", is(String.valueOf(user.getId())));
    }

    @Test
    public void test02_loginUsuarioValido() {
        given()
                .log().all()
                .queryParam("username", "usuario1")
                .queryParam("password", "123456")
                .when()
                .get(basePath + "/login")
                .then()
                .log().all()
                .statusCode(200)
                .body("message", containsString("logged in user session"));
    }

    @Test
    public void test03_buscarUsuarioPorUsername() {
        given()
                .log().all()
                .pathParam("username", "usuario1")
                .when()
                .get(basePath + "/{username}")
                .then()
                .log().all()
                .statusCode(200)
                .body("username", is("usuario1"));
    }

    @Test
    public void test04_atualizarUsuarioExistente() {
        User user = getUser(1, "usuario1", "usuario1@email.com", "novaSenha");

        given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .pathParam("username", user.getUsername())
                .when()
                .put(basePath + "/{username}")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test05_deletarUsuarioExistente() {
        given()
                .log().all()
                .pathParam("username", "usuario1")
                .when()
                .delete(basePath + "/{username}")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test06_criarMultiplosUsuariosArray() {
        User[] users = {
                getUser(2, "usuarioArray1", "array1@email.com", "123456"),
                getUser(3, "usuarioArray2", "array2@email.com", "123456")
        };

        given()
                .log().all()
                .contentType("application/json")
                .body(users)
                .when()
                .post(basePath + "/createWithArray")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test07_criarMultiplosUsuariosLista() {
        User[] users = {
                getUser(4, "usuarioList1", "list1@email.com", "123456"),
                getUser(5, "usuarioList2", "list2@email.com", "123456")
        };

        given()
                .log().all()
                .contentType("application/json")
                .body(users)
                .when()
                .post(basePath + "/createWithList")
                .then()
                .log().all()
                .statusCode(200);
    }

    // ---------- COMPORTAMENTO / VALIDAÇÃO (8) ----------

    @Test
    public void test08_criarUsuarioSemUsername() {
        User user = new User();
        user.setId(6);
        user.setEmail("semusername@email.com");
        user.setPassword("123456");

        given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200); // API não valida campos obrigatórios
    }

    @Test
    public void test09_criarUsuarioEmailInvalido() {
        User user = getUser(7, "usuarioEmailInvalido", "email-invalido", "123456");

        given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test10_loginSenhaInvalida() {
        given()
                .log().all()
                .queryParam("username", "usuario1")
                .queryParam("password", "senhaErrada")
                .when()
                .get(basePath + "/login")
                .then()
                .log().all()
                .statusCode(200); // API retorna 200 mesmo com senha errada
    }

    @Test
    public void test11_loginUsuarioInexistente() {
        given()
                .log().all()
                .queryParam("username", "naoExiste")
                .queryParam("password", "123456")
                .when()
                .get(basePath + "/login")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test12_buscarUsuarioInexistente() {
        given()
                .log().all()
                .pathParam("username", "naoExiste")
                .when()
                .get(basePath + "/{username}")
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    public void test13_atualizarUsuarioInexistente() {
        User user = getUser(999, "naoExiste", "naoexiste@email.com", "123456");

        given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .pathParam("username", user.getUsername())
                .when()
                .put(basePath + "/{username}")
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    public void test14_deletarUsuarioInexistente() {
        given()
                .log().all()
                .pathParam("username", "naoExiste")
                .when()
                .delete(basePath + "/{username}")
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    public void test15_criarUsuarioBodyVazio() {
        given()
                .log().all()
                .contentType("application/json")
                .body("{}")
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test16_criarUsuariosDataDriven() throws IOException {
        ExcelUtils excel = new ExcelUtils("src/test/resources/usuarios.xlsx", "Sheet1");

        for (int i = 1; i < excel.getRowCount(); i++) {
            int id = (int) Double.parseDouble(excel.getCellData(i, 0));
            String username = excel.getCellData(i, 1);
            String email = excel.getCellData(i, 2);
            String password = excel.getCellData(i, 3);

            User user = getUser(id, username, email, password);

            given()
                    .log().all()
                    .contentType("application/json")
                    .body(user)
                    .when()
                    .post("/user")
                    .then()
                    .log().all()
                    .statusCode(200);
        }
    }

}
