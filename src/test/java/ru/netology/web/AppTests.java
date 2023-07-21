package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppTests {
    private String site = "http://localhost:9999/";

    @Test
    public void shouldFillFormValid() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        assertTrue($(".spin").isDisplayed());
        assertDoesNotThrow(() -> $(".notification").shouldBe(Condition.visible, Duration.ofSeconds(15)));
        assertTrue($(".notification").isDisplayed());
    }

    @Test
    public void shouldFillFormIfDoubleName() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл-Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        assertTrue($(".spin").isDisplayed());
        $(".notification").shouldBe(Condition.visible, Duration.ofSeconds(15));
        String actTextNotification = $(".notification__content").getText();
        String expTextNotification = "Встреча успешно забронирована на " + (LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals(expTextNotification, actTextNotification);
        assertTrue($(".notification").isDisplayed());
    }

    @Test
    public void shouldFillFormIfDoubleWords() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Санкт-Петербург");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл-Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        assertTrue($(".spin").isDisplayed());
        $("[data-test-id='notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));

        String actTextNotification = $(".notification__content").getText();
        String expTextNotification = "Встреча успешно забронирована на " + (LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        assertEquals(expTextNotification, actTextNotification);
        assertTrue($(".notification").isDisplayed());
    }

    @Test
    public void shouldNotFillFormIfSpacesInCity() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск     ");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String actText = $("[data-test-id='city'] .input__sub").getText();
        String expText = "Доставка в выбранный город недоступна";

        assertFalse($(".spin").isDisplayed());
        Thread.sleep(1500);
        assertFalse($(".notification").isDisplayed());
        assertEquals(expText, actText);
    }

    @Test
    public void shouldFillFormIfSpacesInName() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл     ");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        assertTrue($(".spin").isDisplayed());
        $("[data-test-id='notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));

        String actTextNotification = $(".notification__content").getText();
        String expTextNotification = "Встреча успешно забронирована на "
                + (LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        assertEquals(expTextNotification, actTextNotification);
        assertTrue($(".notification").isDisplayed());
    }

    @Test
    public void shouldFillFormIfMoreThanThreeDay() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        for (int i = 0; i < 8; i++) {
            $("[data-test-id='date'] .input__control").sendKeys("\b");
        }
        $("[data-test-id='date'] .input__control").setValue(
                LocalDate.now().plusDays(20).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();
        assertTrue($(".spin").isDisplayed());
        Thread.sleep(15000);
        assertDoesNotThrow(() -> $("[data-test-id='notification']").shouldBe(Condition.partialText("Встреча успешно забронирована на ")));
        assertTrue($(".notification").isDisplayed());
    }

    @Test
    public void shouldFillFormIfSelectedMonthForward() throws InterruptedException {
        String ldt = LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        for (int i = 0; i < 8; i++) {
            $("[data-test-id='date'] .input__control").sendKeys("\b");
        }
        $("[data-test-id='date'] .input__control").setValue(ldt);
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();


        String expTextNotification = "Встреча успешно забронирована на " + ldt;

        assertTrue($(".spin").is(Condition.visible));
        Thread.sleep(15000);
        String actTextNotification = $(".notification__content").getText();
        assertTrue($(".notification").isDisplayed());
        assertEquals(expTextNotification, actTextNotification);
    }

    @Test
    public void shouldFillFormIfSelectedYearForward() throws InterruptedException {
        String ldt = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        for (int i = 0; i < 8; i++) {
            $("[data-test-id='date'] .input__control").sendKeys("\b");
        }
        $("[data-test-id='date'] .input__control").setValue(
                LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String expTextNotification = "Встреча успешно забронирована на " + ldt;

        assertTrue($(".spin").is(Condition.visible));
        Thread.sleep(15000);
        String actTextNotification = $(".notification__content").getText();
        assertTrue($(".notification").isDisplayed());
        assertEquals(expTextNotification, actTextNotification);
    }

    @Test
    public void shouldFillFormIfCity2Letters() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Иж");

        ElementsCollection ec = $$(".input__menu .menu-item__control");
        for (int i = 0; i < 3; i++) {
            if (ec.get(i).getOwnText().equals("Ижевск")) {
                ec.get(i).click();
                break;
            }
        }
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();
        ;
        assertTrue($(".spin").isDisplayed());
        assertDoesNotThrow(() -> $(".notification").shouldBe(Condition.visible, Duration.ofSeconds(15)));
        assertTrue($(".notification").isDisplayed());
    }

    @Test
    public void shouldNotFillFormIfUnderlineName() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл_");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String actText = $("[data-test-id='name'] .input__sub").getText();
        String expName = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        assertEquals(expName, actText);
    }

    @Test
    public void shouldNotFillFormIfEngName() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Воронеж");
        $("[data-test-id='name'] .input__control").sendKeys("Kirill");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String actText = $("[data-test-id='name'] .input__sub").getText();
        String expName = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        assertEquals(expName, actText);
    }

    @Test
    public void shouldNotFillFormIfEngCity() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Voronej");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String actText = $("[data-test-id='city'] .input__sub").getText();
        String expName = "Доставка в выбранный город недоступна";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        assertEquals(expName, actText);
    }

    @Test
    public void shouldNotFillFormWithoutAgreement() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[type='button'].button").click();

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
    }

    @Test
    public void shouldNotFillFormIfPhoneLess() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+8963545");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String expName = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        String actText = $("[data-test-id='phone'] .input__sub").getText();
        assertEquals(expName, actText);
    }

    @Test
    public void shouldNotFillFormIfPhoneMore() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+896354595623456");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String expName = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        String actText = $("[data-test-id='phone'] .input__sub").getText();
        assertEquals(expName, actText);
    }

    @Test
    public void shouldNotFillFormIfNoPlusInPhone() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String expName = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertFalse($(".spin").isDisplayed());
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        String actText = $("[data-test-id='phone'] .input__sub").getText();
        assertEquals(expName, actText);
    }

    @Test
    public void shouldNotFillFormIfSelectedTomorrow() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        for (int i = 0; i < 8; i++) {
            $("[data-test-id='date'] .input__control").sendKeys("\b");
        }
        $("[data-test-id='date'] .input__control").setValue(
                LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String actText = $("[data-test-id='date'] .input__sub").getText();
        String expText = "Заказ на выбранную дату невозможен";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        assertEquals(expText, actText);
    }

    @Test
    public void shouldNotFillFormIfSelectedMonthBack() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        for (int i = 0; i < 8; i++) {
            $("[data-test-id='date'] .input__control").sendKeys("\b");
        }
        $("[data-test-id='date'] .input__control").setValue(
                LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String actText = $("[data-test-id='date'] .input__sub").getText();
        String expText = "Заказ на выбранную дату невозможен";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        assertEquals(expText, actText);
    }

    @Test
    public void shouldNotFillFormIfSelectedYearBack() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        for (int i = 0; i < 8; i++) {
            $("[data-test-id='date'] .input__control").sendKeys("\b");
        }
        $("[data-test-id='date'] .input__control").setValue(
                LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String actText = $("[data-test-id='date'] .input__sub").getText();
        String expText = "Заказ на выбранную дату невозможен";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        assertEquals(expText, actText);
    }

    @Test
    public void shouldNotFillFormIfNameIsEmpty() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String actName = $("[data-test-id='name'] .input__sub").getText();
        String expText = "Поле обязательно для заполнения";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);

        assertTrue($(".notification").is(Condition.hidden));
        assertEquals(expText, actName);
    }

    @Test
    public void shouldNotFillFormIfCityIsEmpty() throws InterruptedException {
        open(site);
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String actCity = $("[data-test-id='city'] .input__sub").getText();
        String expText = "Поле обязательно для заполнения";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        assertEquals(expText, actCity);
    }

    @Test
    public void shouldNotFillFormIfPhoneIsEmpty() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String actCity = $("[data-test-id='phone'] .input__sub").getText();
        String expText = "Поле обязательно для заполнения";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        assertEquals(expText, actCity);
    }

    @Test
    public void shouldNotFillFormIfDateIsEmpty() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        for (int i = 0; i < 8; i++) {
            $("[data-test-id='date'] .input__control").sendKeys("\b");
        }
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        String actCity = $("[data-test-id='date'] .input__sub").getText();
        String expText = "Неверно введена дата";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);

        assertTrue($(".notification").is(Condition.hidden));
        assertEquals(expText, actCity);
    }

    @Test
    public void shouldNotFillFormIfNoAgreement() throws InterruptedException {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[type='button'].button").click();

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertTrue($(".notification").is(Condition.hidden));
        assertDoesNotThrow(() -> $("[data-test-id='agreement'] .input_invalid"));
    }

    @Test
    public void shouldNotFillFormIfAllAreEmpty() throws InterruptedException {
        open(site);
        for (int i = 0; i < 8; i++) {
            $("[data-test-id='date'] .input__control").sendKeys("\b");
        }
        $("[type='button'].button").click();

        String actCity = $("[data-test-id='city'] .input__sub").getText();
        String expCity = "Поле обязательно для заполнения";

        assertTrue($(".spin").is(Condition.hidden));
        Thread.sleep(15000);
        assertEquals(expCity, actCity);
        assertTrue($(".notification").is(Condition.hidden));
        assertDoesNotThrow(() -> $("[data-test-id='agreement'] .input_invalid"));
    }

}