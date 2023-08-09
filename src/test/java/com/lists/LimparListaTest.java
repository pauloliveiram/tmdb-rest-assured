package com.lists;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LimparListaTest {

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
    public void testQuandoLimpoUmaListaEntaoObtenhoStatusCode201EOsDadosDaResposta() {
        baseURI = "https://api.themoviedb.org/3";

        Map<String, Object> bodyFirstMovie = new HashMap<>();
        bodyFirstMovie.put("media_id", 26);

        given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
            .body(bodyFirstMovie)
        .when()
            .post("/list/{list_id}/add_item");

        Map<String, Object> bodySecondMovie = new HashMap<>();
        bodySecondMovie.put("media_id", 27);

        given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
            .body(bodySecondMovie)
        .when()
            .post("/list/{list_id}/add_item");

    given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
        .when()
            .post("/list/{list_id}/clear?confirm=true")
        .then()
            .assertThat()
                .statusCode(201)
                .body("success", equalTo(true))
                .body("status_code", equalTo(12))
                .body("status_message", equalTo("The item/record was updated successfully."));
    }
}
