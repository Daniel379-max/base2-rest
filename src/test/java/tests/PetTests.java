package tests;

import core.BaseTest;
import models.Pet;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PetTests extends BaseTest {
    private final String basePath = "/pet";

    private Pet getPet(int id, String name, String status) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setStatus(status);
        pet.setPhotoUrls(new String[]{"url1", "url2"});
        Pet.Tag tag = new Pet.Tag();
        tag.setId(1);
        tag.setName("tag1");
        pet.addTag(tag);
        return pet;
    }

    // ---------- SUCESSO (10) ----------

    @Test

    public void test01_criarPetComSucesso() {
        Pet pet = getPet(100, "Dog1", "available");

        given()
                .log().all()
                .contentType("application/json")
                .body(pet)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(pet.getId()))
                .body("name", is(pet.getName()))
                .body("status", is(pet.getStatus()));
    }

    @Test
    public void test02_atualizarPetExistente() {
        Pet pet = getPet(101, "DogAtualizado", "available");

        given()
                .log().all()
                .contentType("application/json")
                .body(pet)
                .when()
                .put(basePath)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(pet.getId()))
                .body("name", is(pet.getName()))
                .body("status", is(pet.getStatus()));
    }

    @Test
    public void test03_buscarPetPorIdExistente() {
        Pet pet = getPet(102, "Dog2", "available");

        // Criando pet primeiro
        given()
                .contentType("application/json")
                .body(pet)
                .when()
                .post(basePath)
                .then()
                .statusCode(404);

        // Buscando o pet criado
        given()
                .pathParam("id", pet.getId())
                .when()
                .get(basePath + "/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(pet.getId()));
    }

    @Test
    public void test04_buscarPetsPorStatusAvailable() {
        given()
                .queryParam("status", "available")
                .when()
                .get(basePath + "/findByStatus")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test05_buscarPetsPorStatusPending() {
        given()
                .queryParam("status", "pending")
                .when()
                .get(basePath + "/findByStatus")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test06_buscarPetsPorStatusSold() {
        given()
                .queryParam("status", "sold")
                .when()
                .get(basePath + "/findByStatus")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test07_uploadImagemDoPet() {
        Pet pet = getPet(103, "DogUpload", "available");

        // Criando pet antes
        given()
                .contentType("application/json")
                .body(pet)
                .when()
                .post(basePath)
                .then()
                .statusCode(200);

        // Upload da imagem
        given()
                .contentType("multipart/form-data") // ✅ força multipart
                .multiPart("file", new java.io.File("src/test/resources/dog.jpeg"))
                .pathParam("id", pet.getId())
                .when()
                .post(basePath + "/{id}/uploadImage")
                .then()
                .log().all()
                .statusCode(200)
                .body("message", containsString("uploaded"));
    }

    @Test
    public void test08_deletarPetExistente() {
        Pet pet = getPet(104, "DogDelete", "available");

        // Criando pet antes
        given()
                .contentType("application/json")
                .body(pet)
                .when()
                .post(basePath)
                .then()
                .statusCode(200);

        // Deletando pet
        given()
                .pathParam("id", pet.getId())
                .when()
                .delete(basePath + "/{id}")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test09_criarPetComMultiplasTags() {
        Pet pet = getPet(105, "DogMultiTag", "available");
        Pet.Tag tag2 = new Pet.Tag();
        tag2.setId(2);
        tag2.setName("tag2");
        pet.addTag(tag2);

        given()
                .log().all()
                .contentType("application/json")
                .body(pet)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200)
                .body("tags.size()", is(2));
    }

    @Test
    public void test10_criarPetComMultiplasFotos() {
        Pet pet = getPet(106, "DogMultiPhoto", "available");
        pet.setPhotoUrls(new String[]{"url1", "url2", "url3"});

        given()
                .log().all()
                .contentType("application/json")
                .body(pet)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200)
                .body("photoUrls.size()", is(3));
    }

    // ---------- ERROS / VALIDAÇÕES (10) ----------

    @Test
    public void test11_criarPetSemNome() {
        Pet pet = new Pet();
        pet.setId(103);
        pet.setStatus("available");

        given()
                .log().all()
                .contentType("application/json")
                .body(pet)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200)
                .body("name", anyOf(nullValue(), isEmptyOrNullString()));
    }

    @Test
    public void test12_criarPetComStatusInvalido() {
        Pet pet = getPet(108, "DogInvalidStatus", "wrongStatus");

        given()
                .log().all()
                .contentType("application/json")
                .body(pet)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test13_buscarPetInexistente() {
        given()
                .log().all()
                .pathParam("id", 99999)
                .when()
                .get(basePath + "/{id}")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    public void test14_atualizarPetInexistente() {
        Pet pet = getPet(99998, "DogInexistente", "available");

        given()
                .log().all()
                .contentType("application/json")
                .body(pet)
                .when()
                .put(basePath)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test15_deletarPetInexistente() {
        given()
                .log().all()
                .pathParam("id", 99997)
                .when()
                .delete(basePath + "/{id}")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    public void test16_criarPetComIdNegativo() {
        Pet pet = getPet(-1, "DogNegativo", "available");

        given()
                .log().all()
                .contentType("application/json")
                .body(pet)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test17_criarPetComIdDuplicado() {
        Pet pet = getPet(110, "DogDuplicado", "available");

        // Criar primeiro
        given()
                .contentType("application/json")
                .body(pet)
                .when()
                .post(basePath)
                .then()
                .statusCode(200);

        // Tentar criar com mesmo ID
        given()
                .log().all()
                .contentType("application/json")
                .body(pet)
                .when()
                .post(basePath)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test18_buscarPetsSemInformarStatus() {
        given()
                .log().all()
                .when()
                .get(basePath + "/findByStatus")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void test19_uploadImagemSemArquivo() {
        given()
                .log().all()
                .pathParam("id", 111)
                .when()
                .post(basePath + "/{id}/uploadImage")
                .then()
                .log().all()
                .statusCode(415);
    }

    @Test
    public void test20_atualizarPetComBodyVazio() {
        given()
                .log().all()
                .contentType("application/json")
                .body("{}")
                .when()
                .put(basePath)
                .then()
                .log().all()
                .statusCode(200);
    }
}
