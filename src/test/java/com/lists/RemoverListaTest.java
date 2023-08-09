package com.lists;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RemoverListaTest {

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

    @Test
    public void testQuandoRemovoUmaListaEntaoObtenhoStatusCode500EOsDadosDaResposta() {
        baseURI = "https://api.themoviedb.org/3";

        given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
        .when()
            .delete("/list/{list_id}")
        .then()
            .assertThat()
                .statusCode(500)
                .body("success", equalTo(false))
                .body("status_code", equalTo(11))
                .body("status_message", equalTo("Internal error: Something went wrong, contact TMDb."));
    }
}
