package ru.netology.delivery.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;

//import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;
import ru.netology.delivery.data.RegistrationByCardInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.*;


public class CardDeliveryTest {

    @BeforeAll
    static void setUp() {
        Configuration.headless = true;
    }

    RegistrationByCardInfo info = DataGenerator.Registration.generateByCard("ru", 3, 7);

    @Test
    void shouldTestWithCorrectData() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue(info.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(info.getDate()));
        $("[data-test-id='name'] input").setValue(info.getName());
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $(".notification__content").shouldBe(visible, ofSeconds(15)).shouldHave(exactText("Встреча успешно запланирована на " + info.getDate()));
    }

    @Test
    void shouldTestWithCorrectDataAndRescheduledDate() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue(info.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(info.getDate()));
        $("[data-test-id='name'] input").setValue(info.getName());
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $(".notification__content").shouldBe(visible, ofSeconds(15)).shouldHave(exactText("Встреча успешно запланирована на " + info.getDate()));
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(info.getChangeDate()));
        $("[class=button__text]").click();
        $("[data-test-id='replan-notification'] .button__text").shouldBe(visible, ofSeconds(15)).click();
        $(".notification__content").shouldBe(visible, ofSeconds(15)).shouldHave(exactText("Встреча успешно запланирована на " + info.getChangeDate()));
    }

    @Test
    void shouldTestWithAIncorrectCity() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Ekaterinburg");
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(info.getDate()));
        $("[data-test-id='name'] input").setValue(info.getName());
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id=city] .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestWithoutCity() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(info.getDate()));
        $("[data-test-id='name'] input").setValue(info.getName());
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id=city] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestWithIncorrectDate() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] input").setValue(info.getName());
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id='date'] .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldTestWithoutDate() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='name'] input").setValue(info.getName());
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id=date] .input__sub").shouldHave(text("Неверно введена дата"));
    }

    @Test
    void shouldTestWithIncorrectNameAndSurname() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(info.getDate()));
        $("[data-test-id=name] input").setValue("Anton Volkov");
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id=name] .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestWithoutNameAndSurname() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(info.getDate()));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id=name] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    //@Disabled
    @Test
    void shouldTestWithRussianLetterYoInNameAndSurname() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(String.valueOf(info.getDate()));
        $("[data-test-id=name] input").setValue("Алфёров Сергей");
        $("[data-test-id='phone'] input").setValue(info.getPhone());
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $(".notification__content").shouldBe(visible, ofSeconds(15)).shouldHave(exactText("Встреча успешно запланирована на " + info.getDate()));
    }

    //@Disabled
    @Test
    void shouldTestWithIncorrectPhoneNumberTenDigits() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(info.getDate());
        $("[data-test-id='name'] input").setValue("Смирнова Мария-Антануетта");
        $("[data-test-id='phone'] input").setValue("+3954001874");
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id='phone'] .input__sub").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestWithoutPhoneNumber() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(info.getDate());
        $("[data-test-id='name'] input").setValue("Смирнова Мария-Антануетта");
        $("[data-test-id=phone] input").setValue("");
        $("[class=checkbox__box]").click();
        $("[class=button__text]").click();
        $("[data-test-id=phone] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestWithoutCheckbox() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Екатеринбург");
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(info.getDate());
        $("[data-test-id=name] input").setValue("Смирнова Мария-Антануетта");
        $("[data-test-id=phone] input").setValue(info.getPhone());
        $("[class=button__text]").click();
        $("[class=checkbox__text]").shouldHave(text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldTestWithEmptyForm() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id='date'] input").setValue("");
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("");
        $("[class=button__text]").click();
        $("[data-test-id=city] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }
}
