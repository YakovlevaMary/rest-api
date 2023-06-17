package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import models.lombok.LoginBodyLombokModel;
import models.lombok.LoginResponseLombokModel;
import models.pojo.LoginBodyPojoModel;
import models.pojo.LoginResponsePojoModel;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.*;

public class ReqresLessonTests {

    @Test
    void successfulLoginBadPracticeTest() {
        String requestBody = "{ \"email\": \"eve.holt@reqres.in\", " +
                "\"password\": \"cityslicka\" }"; // BAD PRACTICE

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
    void successfulLoginWithPojoModelsTest() {
        LoginBodyPojoModel requestBody = new LoginBodyPojoModel();
        requestBody.setEmail("eve.holt@reqres.in");
        requestBody.setPassword("cityslicka");

        LoginResponsePojoModel loginResponse = given()
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
                .extract().as(LoginResponsePojoModel.class);

        assertEquals("QpwL5tke4Pnpja7X4", loginResponse.getToken());
    }

    @Test
    void successfulLoginWithLombokModelsTest() {
        LoginBodyLombokModel requestBody = new LoginBodyLombokModel();
        requestBody.setEmail("eve.holt@reqres.in");
        requestBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse = given()
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
                .extract().as(LoginResponseLombokModel.class);

        assertEquals("QpwL5tke4Pnpja7X4", loginResponse.getToken());
    }

    @Test
    void successfulLoginWithAllureTest() {
        LoginBodyLombokModel requestBody = new LoginBodyLombokModel();
        requestBody.setEmail("eve.holt@reqres.in");
        requestBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse = given()
                .log().uri()
                .log().body()
                .filter(new AllureRestAssured())
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post("login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseLombokModel.class);

        assertEquals("QpwL5tke4Pnpja7X4", loginResponse.getToken());
    }

    @Test
    void successfulLoginWithAllureAsConfigTest() {
        RestAssured.filters(new AllureRestAssured());

        LoginBodyLombokModel requestBody = new LoginBodyLombokModel();
        requestBody.setEmail("eve.holt@reqres.in");
        requestBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse = given()
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
                .extract().as(LoginResponseLombokModel.class);

        assertEquals("QpwL5tke4Pnpja7X4", loginResponse.getToken());
    }

    @Test
    void successfulLoginWithCustomAllureTest() {
        LoginBodyLombokModel requestBody = new LoginBodyLombokModel();
        requestBody.setEmail("eve.holt@reqres.in");
        requestBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse = given()
                .log().uri()
                .log().body()
                .filter(withCustomTemplates())
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post("login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseLombokModel.class);

        assertEquals("QpwL5tke4Pnpja7X4", loginResponse.getToken());
    }

    @Test
    void successfulLoginWithStepsTest() {
        LoginBodyLombokModel requestBody = new LoginBodyLombokModel();
        requestBody.setEmail("eve.holt@reqres.in");
        requestBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse = step("Make request", () ->
                given()
                        .log().uri()
                        .log().body()
                        .filter(withCustomTemplates())
                        .contentType(JSON)
                        .body(requestBody)
                        .when()
                        .post("login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(LoginResponseLombokModel.class));
        step("Check response", () ->
                assertEquals("QpwL5tke4Pnpja7X4", loginResponse.getToken()));
    }

    @Test
    void successfulLoginWithSpecsTest() {
        LoginBodyLombokModel requestBody = new LoginBodyLombokModel();
        requestBody.setEmail("eve.holt@reqres.in");
        requestBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse = step("Make request", () ->
                given(requestSpec)
//                    .spec(loginRequestSpec)
                        .body(requestBody)
                        .when()
                        .post("login")
                        .then()
                        .spec(responseWithCode200Spec)
                        .extract().as(LoginResponseLombokModel.class));
        step("Check response", () ->
                assertEquals("QpwL5tke4Pnpja7X4", loginResponse.getToken()));
    }

}