package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import core.BaseTest;
import models.Pet;
import models.Store;
import org.junit.Test;


public class PetstoreTest extends BaseTest {

    @Test
    public void cadastrarPedidoDePetSucesso() {
        Store store = getStore();

        given()
                .log().all()
                .contentType("application/json")
                .body(store)
                .when()
                .post("/store/order")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("petId", is(store.getPetId()))
                .body("quantity", is(store.getQuantity()))
                .body("status", is(store.getStatus()))

        ;
    }


    @Test
    public void pesquisarPorPetInexistente() {

        given()
                .log().all()
                .pathParam("id", "32424")
                .when()
                .get("/pet/{id}")
                .then()
                .log().all()
                .statusCode(404)
                .body("code", is(1))
                .body("type", is("error"))
                .body("message",is("Pet not found"))

        ;
    }

    @Test
    public void atualizarPetExistente() {
        Pet pet = getPet();

        given()
                .log().all()
                .contentType("application/json")
                .body(pet)
                .when()
                .put("/pet")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("name", is(pet.getName()))
                .body("status", is(pet.getStatus()))
        ;
    }

    @Test
    public void pesquisarPetsStatusPending() {

        given()
                .log().all()
                .queryParam("status", "available")
                .when()
                .get("/pet/findByStatus")
                .then()
                .log().all()
                .statusCode(200)
        ;
    }



    private Store getStore() {
        Store store = new Store();
        store.setId(0);
        store.setPetId(100);
        store.setQuantity(3);
        store.setShipDate("2024-02-29T22:35:28.749Z");
        store.setStatus("available");
        store.setComplete(true);
        return store;
    }

    private Pet getPet() {
        String[] newPhotoUrls = {"url1", "url2", "url3"};
        Pet pet = new Pet();
        Pet.Tag tag1 = new Pet.Tag();


        pet.setId(120);
//		pet.getCategory().setId(0);
//		pet.getCategory().setName("categoria teste atualizada");
        pet.setName("Nome atualizado novo");
        pet.setPhotoUrls(newPhotoUrls);
        tag1.setId(1);
        tag1.setName("Tag1");
        pet.setStatus("available");


        pet.addTag(tag1);
        return pet;
    }

}
