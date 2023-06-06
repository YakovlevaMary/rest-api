package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class ReqresInTests extends TestBase {
    @Test
    @DisplayName("User login and password are valid")
    void successfulLoginTest() {
        String requestBody = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }"; // BAD PRACTICE

        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post("login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("User login and password are invalid (missing json format, error 400)")
    void negativeLoginAndPassword400Test() {
        String requestBody = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }"; // BAD PRACTICE

        given()
                .log().uri()
                .log().body()
                .body(requestBody)
                .when()
                .post("login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    @DisplayName("Negative login test (absence of request body, error 415)")
    void negativeLoginTest() {
        given()
                .log().uri()
                .log().body()
                .when()
                .post("login")
                .then()
                .log().status()
                .log().body()
                .statusCode(415);
    }

    @Test
    @DisplayName("User password is missed (error 400)")
    void negativePassword400Test() {
        String requestBody = "{ \"email\": \"peter@klaven\"}";
        given()
                .log().uri()
                .log().body()
                .body(requestBody)
                .when()
                .post("login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    @DisplayName("User registration is successful")
    void successfulRegistrationTest() {
        String requestBody = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post("register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4),
                        "token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("User registration is unsuccessful (password is missed, error 400)")
    void negativeRegistrationTest() {
        String requestBody = "{ \"email\": \"sydney@fife\"}";
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post("register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Test of delete option")
    void deleteTest() {

        given()
                .log().uri()
                .log().body()
                .when()
                .delete("users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }

    @Test
    @DisplayName("Test of patch option")
    void patchTest() {
        String requestBody = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .patch("users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("morpheus"),
                        "job", is("zion resident"),
                        "updatedAt", startsWith("2023-06"));
    }

    @Test
    @DisplayName("Test of put option")
    void putTest() {
        String requestBody = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .put("users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("morpheus"),
                        "job", is("zion resident"),
                        "updatedAt", startsWith("2023-06"));
    }

    @Test
    @DisplayName("Test of creation via POST option")
    void creationTest() {
        String requestBody = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post("users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"),
                        "job", is("leader"),
                        "id", is(notNullValue()),
                        "createdAt", startsWith("2023-06"));
    }

    @Test
    @DisplayName("Display user list page №2 and check its scheme")
    void getUserListSchemeTest() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/users-list-response-scheme.json"));

    }

    @Test
    @DisplayName("Test of sending an unknown request (single user №23 doesn't exist)")
    void getNonExistentUserTest() {
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .when()
                .get("unknown/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    @DisplayName("Test of single user data content")
    void getSingleUserDataTest() {
        given()
                .log().uri()
                .when()
                .get("users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(2),
                        "data.first_name", is("Janet"),
                        "data.last_name", is("Weaver"));
    }

    @Test
    @DisplayName("Test of checking IDs and names in user list")
    void getUsersListBodyIdsNamesTest() {
        given()
                .log().uri()
                .when()
                .get("unknown")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", hasItems(1, 2, 3, 4, 5, 6),
                        "data.name", hasItems("cerulean", "fuchsia rose", "true red", "aqua sky", "tigerlily", "blue turquoise"));
    }
}
