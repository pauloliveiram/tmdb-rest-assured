package com.lists;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BuscarDetalhesListaTest {

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
    public void testQuandoOsDetalhesDeUmaListaEntaoObtenhoStatusCode200EOsDadosDaResposta() {
        baseURI = "https://api.themoviedb.org/3";

        Map<String, Object> bodyMovie = new HashMap<>();
        bodyMovie.put("media_id", 12);

        given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
            .body(bodyMovie)
        .when()
            .post("/list/{list_id}/add_item");

        given()
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(ContentType.JSON)
            .pathParam("list_id", listId)
        .when()
            .get("/list/{list_id}")
        .then()
            .assertThat()
                .statusCode(200)
                .body("created_by", isA(String.class))
                .body("description", isA(String.class))
                .body("favorite_count", isA(Integer.class))
                .body("id", isA(String.class))
                .body("items", isA(List.class))
                .body("items[0].adult", isA(Boolean.class))
                .body("items[0].backdrop_path", isA(String.class))
                .body("items[0].genre_ids", isA(List.class))
                .body("items[0].id", isA(Integer.class))
                .body("items[0].media_type", isA(String.class))
                .body("items[0].original_language", isA(String.class))
                .body("items[0].original_title", isA(String.class))
                .body("items[0].overview", isA(String.class))
                .body("items[0].popularity", isA(Float.class))
                .body("items[0].poster_path", isA(String.class))
                .body("items[0].release_date", isA(String.class))
                .body("items[0].title", isA(String.class))
                .body("items[0].video", isA(Boolean.class))
                .body("items[0].vote_average", isA(Float.class))
                .body("items[0].vote_count", isA(Integer.class))
                .body("item_count", isA(Integer.class))
                .body("iso_639_1", isA(String.class))
                .body("name", isA(String.class))
                .body("poster_path", nullValue());
    }
}
