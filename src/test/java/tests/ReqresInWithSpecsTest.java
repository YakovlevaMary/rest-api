package tests;

import models.lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.Specs.*;

public class ReqresInWithSpecsTest {
    String expectedToken = "QpwL5tke4Pnpja7X4",
            userName = "morpheus",
            userFirstJob = "zion resident",
            userSecondJob = "leader",
            userFirstEmail = "eve.holt@reqres.in",
            userSecondEmail = "peter@klaven",
            userThirdEmail = "sydney@fife",
            userFirstPassword = "cityslicka",
            userSecondPassword = "pistol",
            expectedErrorMessageNoEmailOrPassword = "Missing email or username",
            expectedErrorMessageNoPassword = "Missing password",
            expectedID = "4",
            expectedLastNameOfFirstUser = "Lawson",
            expectedLastNameOfLastUser = "Howell",
            expectedSingleUserLastName = "Weaver",
            expectedNameOfFirstColor = "cerulean",
            expectedNameOfLastColor = "blue turquoise";
    Integer expectedSingleUserID = 2,
            expectedIDOfFirstUser = 7,
            expectedIDOfLastUser = 12,
            expectedFirstColorID = 1,
            expectedLastColorID = 6;

    @Test
    @DisplayName("User login is successful")
    void successfulLoginWithSpecsTest() {
        UserLoginAndRegistrationRequestModel requestBody = new UserLoginAndRegistrationRequestModel();
        requestBody.setEmail(userFirstEmail);
        requestBody.setPassword(userFirstPassword);

        UserLoginResponseModel loginResponse = step("Make request", () ->
                given(requestSpec)
                        .body(requestBody)
                        .when()
                        .post("login")
                        .then()
                        .spec(responseWithCode200Spec)
                        .extract().as(UserLoginResponseModel.class));
        step("Check token in response", () ->
                assertEquals(expectedToken, loginResponse.getToken()));
    }

    @Test
    @DisplayName("User login and password are invalid (missing json format, error 400)")
    void negativeLoginAndPassword400WithSpecsTest() {
        UserLoginAndRegistrationRequestModel requestBody = new UserLoginAndRegistrationRequestModel();
        requestBody.setEmail(userFirstEmail);
        requestBody.setPassword(userFirstPassword);

        UserLoginResponseModel loginResponse = step("Make request", () ->
                given(loginRequestWithoutJsonFormatSpec)
                        .body(requestBody)
                        .when()
                        .post("login")
                        .then()
                        .spec(responseWithCode400Spec)
                        .extract().as(UserLoginResponseModel.class));
        step("Check error message in response", () ->
                assertEquals(expectedErrorMessageNoEmailOrPassword, loginResponse.getError()));
    }

    @Test
    @DisplayName("User login is unsuccessful as password is missed (error 400)")
    void negativePassword400WithSpecsTest() {
        UserLoginAndRegistrationRequestModel requestBody = new UserLoginAndRegistrationRequestModel();
        requestBody.setEmail(userSecondEmail);

        UserLoginResponseModel loginResponse = step("Make request", () ->
                given(requestSpec)
                        .body(requestBody)
                        .when()
                        .post("login")
                        .then()
                        .spec(responseWithCode400Spec)
                        .extract().as(UserLoginResponseModel.class));

        step("Check error message in response", () ->
                assertEquals(expectedErrorMessageNoPassword, loginResponse.getError()));
    }

    @Test
    @DisplayName("User registration is successful")
    void successfulRegistrationWithSpecsTest() {
        UserLoginAndRegistrationRequestModel requestBody = new UserLoginAndRegistrationRequestModel();
        requestBody.setEmail(userFirstEmail);
        requestBody.setPassword(userSecondPassword);

        UserRegistrationResponseModel registrationResponse = step("Make request", () ->
                given(requestSpec)
                        .body(requestBody)
                        .when()
                        .post("register")
                        .then()
                        .spec(responseWithCode200Spec)
                        .extract().as(UserRegistrationResponseModel.class));
        step("Check ID value in response", () ->
                assertEquals(expectedID, registrationResponse.getID()));
        step("Check token in response", () ->
                assertEquals(expectedToken, registrationResponse.getToken()));
    }

    @Test
    @DisplayName("User registration is unsuccessful (password is missed, error 400)")
    void negativeRegistrationTest() {
        UserLoginAndRegistrationRequestModel requestBody = new UserLoginAndRegistrationRequestModel();
        requestBody.setEmail(userThirdEmail);

        UserRegistrationResponseModel registrationResponse = step("Make request", () ->
                given(requestSpec)
                        .body(requestBody)
                        .when()
                        .post("register")
                        .then()
                        .spec(responseWithCode400Spec)
                        .extract().as(UserRegistrationResponseModel.class));
        step("Check error message in response", () ->
                assertEquals(expectedErrorMessageNoPassword, registrationResponse.getError()));
    }

    @Test
    @DisplayName("Test of delete option")
    void deleteWithSpecsTest() {
        step("Make request", () ->
                given(requestSpec)
                        .when()
                        .delete("users/2")
                        .then()
                        .spec(responseWithCode204Spec));

    }

    @Test
    @DisplayName("Test of patch option")
    void patchWithSpecsTest() {

        CreatePatchPutRequestModel requestBody = new CreatePatchPutRequestModel();
        requestBody.setName(userName);
        requestBody.setJob(userFirstJob);

        CreatePatchPutResponseModel patchResponse = step("Make request", () ->
                given(requestSpec)
                        .body(requestBody)
                        .when()
                        .patch("users/2")
                        .then()
                        .spec(responseWithCode200Spec)
                        .extract().as(CreatePatchPutResponseModel.class));
        step("Check name in response", () ->
                assertEquals(userName, patchResponse.getName()));
        step("Check job in response", () ->
                assertEquals(userFirstJob, patchResponse.getJob()));
        step("Check date of information updating in response", () ->
                patchResponse.getUpdatedAt().startsWith("2023-06"));

    }

    @Test
    @DisplayName("Test of put option")
    void putWithSpecsTest() {
        CreatePatchPutRequestModel requestBody = new CreatePatchPutRequestModel();
        requestBody.setName(userName);
        requestBody.setJob(userFirstJob);

        CreatePatchPutResponseModel putResponse = step("Make request", () ->
                given(requestSpec)
                        .body(requestBody)
                        .when()
                        .put("users/2")
                        .then()
                        .spec(responseWithCode200Spec)
                        .extract().as(CreatePatchPutResponseModel.class));
        step("Check name in response", () ->
                assertEquals(userName, putResponse.getName()));
        step("Check job in response", () ->
                assertEquals(userFirstJob, putResponse.getJob()));
        step("Check date of information updating in response", () ->
                putResponse.getUpdatedAt().startsWith("2023-06"));
    }

    @Test
    @DisplayName("Test of creation via POST option")
    void creationWithSpecsTest() {
        CreatePatchPutRequestModel requestBody = new CreatePatchPutRequestModel();
        requestBody.setName(userName);
        requestBody.setJob(userSecondJob);

        CreatePatchPutResponseModel createResponse = step("Make request", () ->
                given(requestSpec)
                        .body(requestBody)
                        .when()
                        .post("users")
                        .then()
                        .spec(responseWithCode201Spec)
                        .extract().as(CreatePatchPutResponseModel.class));
        step("Check name in response", () ->
                assertEquals(userName, createResponse.getName()));
        step("Check job in response", () ->
                assertEquals(userSecondJob, createResponse.getJob()));
        step("Check ID is not null in response", () ->
                assertThat(createResponse.getId()).isNotNull());
        step("Check date of information creating in response", () ->
                createResponse.getCreatedAt().startsWith("2023-06"));

    }

    @Test
    @DisplayName("Display user list page №2 and check its scheme")
    void getUsersListSchemeWithSpecsTest() {
        step("Make request", () ->
                given(requestSpec)
                        .when()
                        .get("users?page=2")
                        .then()
                        .spec(responseWithCode200Spec)
                        .body(matchesJsonSchemaInClasspath("schemes/users-list-response-scheme.json")));

    }

    @Test
    @DisplayName("Check users list by IDs and names")
    void getUsersListIDsAndNamesWithSpecsTest() {

        UsersListResponseModel response = step("Make request", () ->
                given(requestSpec)
                        .when()
                        .get("users?page=2")
                        .then()
                        .spec(responseWithCode200Spec)
                        .extract().as(UsersListResponseModel.class));
        step("Check ID of the first user in response", () ->
                assertThat(response.getData().getFirst().getID()).isEqualTo(expectedIDOfFirstUser));
        step("Check ID of the last user in response", () ->
                assertThat(response.getData().getLast().getID()).isEqualTo(expectedIDOfLastUser));
        step("Check name of the first user in response", () ->
                assertThat(response.getData().getFirst().getLast_name()).isEqualTo(expectedLastNameOfFirstUser));
        step("Check name of the last user in response", () ->
                assertThat(response.getData().getLast().getLast_name()).isEqualTo(expectedLastNameOfLastUser));

    }

    @Test
    @DisplayName("Test of sending an unknown request (single user №23 doesn't exist)")
    void getNonExistentUserWithSpecsTest() {
        step("Make request", () ->
                given(requestSpec)
                        .when()
                        .get("unknown/23")
                        .then()
                        .spec(responseWithCode404Spec));
    }

    @Test

    @DisplayName("Test of single user data content")
    void getSingleUserDataWithSpecsTest() {

        SingleUserResponseModel response = step("Make request", () ->
                given(requestSpec)
                        .when()
                        .get("users/2")
                        .then()
                        .spec(responseWithCode200Spec)
                        .extract().as(SingleUserResponseModel.class));
        step("Check ID of the single user in response", () ->
                assertThat(response.getData().getID()).isEqualTo(expectedSingleUserID));
        step("Check the last name of the single user in response", () ->
                assertThat(response.getData().getLast_name()).isEqualTo(expectedSingleUserLastName));
    }

    @Test
    @DisplayName("Test of checking IDs and names in color list")
    void getColorListIdsNamesWithSpecsTest() {
        ColorsResponseModel response = step("Make request", () ->
                given(requestSpec)
                        .when()
                        .get("unknown")
                        .then()
                        .spec(responseWithCode200Spec)
                        .extract().as(ColorsResponseModel.class));
        step("Check ID of the first user in response", () ->
                assertThat(response.getData().getFirst().getID()).isEqualTo(expectedFirstColorID));
        step("Check ID of the last user in response", () ->
                assertThat(response.getData().getLast().getID()).isEqualTo(expectedLastColorID));
        step("Check name of the first user in response", () ->
                assertThat(response.getData().getFirst().getName()).isEqualTo(expectedNameOfFirstColor));
        step("Check name of the last user in response", () ->
                assertThat(response.getData().getLast().getName()).isEqualTo(expectedNameOfLastColor));
    }


}
