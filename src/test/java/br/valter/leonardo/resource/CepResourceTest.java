package br.valter.leonardo.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CepResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/cep/ping")
          .then()
             .statusCode(200)
             .body(is("pong"));
    }

}