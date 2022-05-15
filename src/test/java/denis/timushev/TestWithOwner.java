package denis.timushev;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import denis.timushev.helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class TestWithOwner {

    @BeforeAll
    static void beforeAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        Configuration.baseUrl = "https://demoqa.com";
        Configuration.browserSize = "1920x1080";
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }

    @Test
    @DisplayName("Checking the registration form")
    void fillFormTest() {
        step("Opening the form", () -> {
            open("/automation-practice-form");
            $(".practice-form-wrapper").shouldHave(text("Student Registration Form"));
            executeJavaScript("$('footer').remove()");
            executeJavaScript("$('#fixedban').remove()");
        });

        step("Entering data", () -> {
            $("#firstName").setValue("Alexander");
            $("#lastName").setValue("Pushkin");
            $("#userEmail").setValue("alexanderpushkin@mail.ru");
            $("#gender-radio-1").parent().click();
            $("#userNumber").setValue("9261234567");
            $("#dateOfBirthInput").click();
            $(".react-datepicker__month-select").selectOption("May");
            $(".react-datepicker__year-select").selectOption("1999");
            $(".react-datepicker__day--026").click();
            $("#subjectsInput").setValue("English").pressEnter();
            $("#hobbies-checkbox-2").parent().click();
            $("#uploadPicture").uploadFromClasspath("img/1.png");
            $("#currentAddress").setValue("st. Prechistenka 12/2");
            $("#state").click();
            $("#stateCity-wrapper").$(byText("Haryana")).click();
            $("#city").click();
            $("#stateCity-wrapper").$(byText("Karnal")).click();
            $("#submit").click();
        });

        step("Checking the data", () -> {
            $("#example-modal-sizes-title-lg").shouldHave(text("Thanks for submitting the form"));
            $(".table-responsive").shouldHave(text("Alexander Pushkin"), text("alexanderpushkin@mail.ru"),
                    text("9261234567"), text("st. Prechistenka 12/2"), text("English"),
                    text("Haryana"), text("Karnal"));
        });
    }
}