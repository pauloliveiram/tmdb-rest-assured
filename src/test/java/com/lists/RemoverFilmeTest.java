package com.lists;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RemoverFilmeTest {

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

        Map<String, Object> bodyAddMovie = new HashMap<>();
        bodyAddMovie.put("media_id", 12);

        given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
            .body(bodyAddMovie)
        .when()
            .post("/list/{list_id}/add_item");
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
    public void testQuandoRemovoUmFilmeNaListaEntaoObtenhoStatusCode200EOsDadosDaResposta() {
        baseURI = "https://api.themoviedb.org/3";

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("media_id", 12);

        given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
            .body(jsonAsMap)
        .when()
            .post("/list/{list_id}/remove_item")
        .then()
            .assertThat()
            .statusCode(200)
            .body("success", equalTo(true))
            .body("status_code", equalTo(13))
            .body("status_message", equalTo("The item/record was deleted successfully."));
    }
}
