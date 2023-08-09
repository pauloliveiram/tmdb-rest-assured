package com.account;

import io.restassured.http.ContentType;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BuscarListasTest {

    String bearerToken = System.getenv("TMDB_BEARER_TOKEN");

    @Test
    public void testQuandoBuscoAsListasEntaoObtenhoStatusCode200EOsDadosDaResposta() {
        baseURI = "https://api.themoviedb.org/3";

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + bearerToken)
        .when()
            .get("/account/9961072/lists")
        .then()
            .assertThat()
                .statusCode(200)
                .body("page", isA(Integer.class))
                .body("results", isA(List.class))
                .body("results[0].description", isA(String.class))
                .body("results[0].favorite_count", isA(Integer.class))
                .body("results[0].id", isA(Integer.class))
                .body("results[0].item_count", isA(Integer.class))
                .body("results[0].iso_639_1", isA(String.class))
                .body("results[0].list_type", isA(String.class))
                .body("results[0].name", isA(String.class))
                .body("results[0].poster_path", nullValue())
                .body("total_pages", isA(Integer.class))
                .body("total_results", isA(Integer.class));
    }
}
