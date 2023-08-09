package com.lists;

import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class CriarListaTest {

    private int listId;
    String bearerToken = System.getenv("TMDB_BEARER_TOKEN");

    @After
    public void after() {
        given()
            .header("Authorization", "Bearer " + bearerToken)
            .pathParam("list_id", listId)
        .when()
            .delete("/list/{list_id}");
    }

    @Test
    public void testQuandoCrioUmaListaEntaoObtenhoStatusCode201EOsDadosDaResposta() {
        baseURI = "https://api.themoviedb.org/3";

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", "Teste da automação");
        jsonAsMap.put("description", "");
        jsonAsMap.put("language", "pt-br");

        listId = given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .body(jsonAsMap)
        .when()
            .post("/list")
        .then()
            .assertThat()
                .statusCode(201)
                .body("success", equalTo(true))
                .body("status_code", equalTo(1))
                .body("status_message", equalTo("The item/record was created successfully."))
                .body("list_id", isA(Number.class))
            .extract().jsonPath().getInt("list_id");
    }
}

