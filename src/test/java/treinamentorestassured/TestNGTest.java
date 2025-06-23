package treinamentorestassured;


import org.testng.annotations.Test;
import static  io.restassured.RestAssured.*;


public class TestNGTest {

    @Test
    public void meuPrimeitoTest(){
        given().
                baseUri("http://petstore.swagger.io/v2").
                basePath("/pet/{petId}").
                pathParam("petId", 444).
        when().
            get().
        then().
            statusCode(200);

    }
}
