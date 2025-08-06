package tests;

import core.BaseTest;
import models.Store;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StoreTests extends BaseTest {
    private final String basePath = "/store/order";

    private Store getStore(int id, int petId, int quantity, String status, boolean complete) {
        Store store = new Store();
        store.setId(id);
        store.setPetId(petId);
        store.setQuantity(quantity);
        store.setShipDate("2025-07-30T10:00:00.000Z");
        store.setStatus(status);
        store.setComplete(complete);
        return store;
    }
    // ---------- SUCESSO (8) ----------

    @Test
    public void test01_criarPedidoDePetComSucesso() {
        Store store = getStore(1, 100, 3, "placed", true);

        given()
                .log().all()
                .contentType("application/json")
                .body(store)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(store.getId()))
                .body("petId", is(store.getPetId()))
                .body("quantity", is(store.getQuantity()))
                .body("status", is(store.getStatus()));
    }

    @Test
    public void test02_buscarPedidoPorIdExistente() {
        Store store = getStore(2, 101, 2, "placed", true);

        // Cria pedido primeiro
        given()
                .contentType("application/json")
                .body(store)
                .when()
                .post(basePath)
                .then()
                .statusCode(200);

        // Busca pedido
        given()
                .pathParam("id", store.getId())
                .when()
                .get(basePath + "/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(store.getId()))
                .body("petId", is(store.getPetId()));
    }

    @Test
    public void test03_deletarPedidoExistente() {
        Store store = getStore(3, 102, 1, "placed", true);

        // Cria pedido
        given()
                .contentType("application/json")
                .body(store)
                .when()
                .post(basePath)
                .then()
                .statusCode(200);

        // Deleta pedido
        given()
                .pathParam("id", store.getId())
                .when()
                .delete(basePath + "/{id}")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test04_criarPedidoQuantidade1() {
        Store store = getStore(4, 103, 1, "placed", false);

        given()
                .log().all()
                .contentType("application/json")
                .body(store)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200)
                .body("quantity", is(store.getQuantity()));
    }

    @Test
    public void test05_criarPedidoQuantidadeAlta() {
        Store store = getStore(5, 104, 50, "placed", false);

        given()
                .log().all()
                .contentType("application/json")
                .body(store)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200)
                .body("quantity", is(store.getQuantity()));
    }

    @Test
    public void test06_criarPedidoCompletoTrue() {
        Store store = getStore(6, 105, 2, "placed", true);

        given()
                .log().all()
                .contentType("application/json")
                .body(store)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200)
                .body("complete", is(true));
    }

    @Test
    public void test07_criarPedidoComDataFutura() {
        Store store = getStore(7, 106, 2, "placed", true);
        store.setShipDate("2026-01-01T12:00:00.000Z");

        given()
                .log().all()
                .contentType("application/json")
                .body(store)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200)
                .body("shipDate", containsString("2026"));
    }

    @Test
    public void test08_consultarEstoque() {
        given()
                .log().all()
                .when()
                .get("/store/inventory")
                .then()
                .log().all()
                .statusCode(200)
                .body("$", notNullValue());
    }

    // ---------- COMPORTAMENTO / VALIDAÇÃO (7) ----------

    @Test
    public void test09_criarPedidoPetInexistente() {
        Store store = getStore(8, 99999, 1, "placed", true);

        given()
                .log().all()
                .contentType("application/json")
                .body(store)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200); // API aceita mesmo pet inexistente
    }

    @Test
    public void test10_criarPedidoSemPetId() {
        Store store = new Store();
        store.setId(9);
        store.setQuantity(1);
        store.setStatus("placed");
        store.setComplete(true);

        given()
                .log().all()
                .contentType("application/json")
                .body(store)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test11_criarPedidoSemQuantidade() {
        Store store = getStore(10, 108, 0, "placed", true);

        given()
                .log().all()
                .contentType("application/json")
                .body(store)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test12_buscarPedidoInexistente() {
        given()
                .log().all()
                .pathParam("id", 99999)
                .when()
                .get(basePath + "/{id}")
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(404))); // depende do backend
    }

    @Test
    public void test13_deletarPedidoInexistente() {
        given()
                .log().all()
                .pathParam("id", 99998)
                .when()
                .delete(basePath + "/{id}")
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    public void test14_criarPedidoQuantidadeNegativa() {
        Store store = getStore(11, 109, -5, "placed", true);

        given()
                .log().all()
                .contentType("application/json")
                .body(store)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test15_criarPedidoBodyVazio() {
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
}
