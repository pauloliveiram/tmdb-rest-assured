package com.lists;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class AdicionarFilmeTest {

    private int listId;
    String bearerToken = System.getenv("TMDB_BEARER_TOKEN");

    @Before
    public void before() {
        baseURI = "https://api.themoviedb.org/3";

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", "Teste da automação");
        jsonAsMap.put("description", "");
        jsonAsMap.put("language", "pt-br");

        Response response =
            given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .body(jsonAsMap)
            .when()
                .post("/list")
            .then()
                .contentType(ContentType.JSON)
            .extract()
                .response();

        listId = response.jsonPath().getInt("list_id");
    }

    @After
    public void after() {
        given()
            .header("Authorization", "Bearer " + bearerToken)
            .pathParam("list_id", listId)
        .when()
            .delete("/list/{list_id}");
    }

    @Test
    public void testQuandoAdicionoUmFilmeNaListaEntaoObtenhoStatusCode201EOsDadosDaResposta() {
        baseURI = "https://api.themoviedb.org/3";

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("media_id", 12);

        given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
            .body(jsonAsMap)
        .when()
            .post("/list/{list_id}/add_item")
        .then()
            .assertThat()
                .statusCode(201)
                .body("success", equalTo(true))
                .body("status_code", equalTo(12))
                .body("status_message", equalTo("The item/record was updated successfully."));
    }

    @Test
    public void testQuandoAdicionoUmFilmeRepetidoNaListaEntaoObtenhoStatusCode403EOsDadosDaResposta() {
        baseURI = "https://api.themoviedb.org/3";

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("media_id", 12);

        given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
            .body(jsonAsMap)
        .when()
            .post("/list/{list_id}/add_item");

    given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
            .body(jsonAsMap)
        .when()
            .post("/list/{list_id}/add_item")
        .then()
            .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("status_code", equalTo(8))
                .body("status_message", equalTo("Duplicate entry: The data you tried to submit already exists."));
    }

    @Test
    public void testQuandoAdicionoUmFilmeSemEstarAutorizadoEntaoObtenhoStatusCode401EOsDadosDaResposta() {
        baseURI = "https://api.themoviedb.org/3";

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("media_id", 12);

        given()
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
            .body(jsonAsMap)
        .when()
            .post("/list/{list_id}/add_item")
        .then()
            .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("status_code", equalTo(7))
                .body("status_message", equalTo("Invalid API key: You must be granted a valid key."));
    }

    @Test
    public void testQuandoAdicionoUmFilmeInvalidoEntaoObtenhoStatusCode404EOsDadosDaResposta() {
        baseURI = "https://api.themoviedb.org/3";

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("media_id", "inv");

        given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
            .body(jsonAsMap)
        .when()
            .post("/list/{list_id}/add_item")
        .then()
            .assertThat()
                .statusCode(404)
                .body("success", equalTo(false))
                .body("status_code", equalTo(34))
                .body("status_message", equalTo("The resource you requested could not be found."));
    }


}
