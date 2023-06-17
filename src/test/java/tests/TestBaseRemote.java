package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import io.qameta.allure.selenide.AllureSelenide;

public class TestBaseRemote {
    @BeforeAll
    static void beforeAll() {
        Configuration.pageLoadStrategy = "eager";
        String selenoidUrl = System.getProperty("selenoid_url");
        String userLoginPassword = System.getProperty("user_login_password");
        selenoidUrl = selenoidUrl.replaceAll("https://", "");
        Configuration.remote = "https://" + userLoginPassword + "@" + selenoidUrl;
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api/";

    }
    @BeforeEach
    void beforeEach() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

}