package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class AppTests {
    private final String site = "http://localhost:9999/";

    private String generatePlusDate(long toAddDays) {
        return (LocalDate.now().plusDays(toAddDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    private String generateMinusDate(long toMinusDays) {
        return (LocalDate.now().minusDays(toMinusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @Test
    public void shouldFillFormValid() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $(".spin").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldFillFormIfDoubleName() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл-Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $(".spin").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification").shouldHave(Condition.text("Встреча успешно забронирована на " + generatePlusDate(3)),
                        Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldFillFormIfDoubleWords() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Санкт-Петербург");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл-Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $(".spin").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification").shouldHave(Condition.text("Встреча успешно забронирована на " + generatePlusDate(3)),
                        Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldFillFormIfSpacesInName() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл     ");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $(".spin").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.partialText("Встреча успешно забронирована на " + generatePlusDate(3)),
                        Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldFillFormIfMoreThanThreeDay() {
        open(site);
        String ldt = generatePlusDate(10);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").setValue(ldt);
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $(".spin").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.partialText("Встреча успешно забронирована на " + ldt),
                        Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldFillFormIfSelectedMonthForward() {
        String ldt = generatePlusDate(30);
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").setValue(ldt);
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $(".spin").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.partialText("Встреча успешно забронирована на " + ldt),
                        Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldFillFormIfSelectedYearForward() {
        String ldt = generatePlusDate(365);
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").setValue(ldt);
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $(".spin").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + ldt),
                        Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldFillFormIfCity2Letters() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Иж");
        $$(".input__menu .menu-item__control").findBy(Condition.text("Ижевск")).click();

        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();
        $(".spin").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + generatePlusDate(3)),
                        Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldNotFillFormIfUnderlineName() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл_");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='name'] .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfEngName() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Воронеж");
        $("[data-test-id='name'] .input__control").sendKeys("Kirill");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='name'] .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfEngCity() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Voronej");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='city'] .input__sub").shouldHave(Condition.text("Доставка в выбранный город недоступна"));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfSpacesInCity() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск     ");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $("[data-test-id='city'] .input__sub").shouldHave(Condition.text("Доставка в выбранный город недоступна "));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfChineseName() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Воронеж");
        $("[data-test-id='name'] .input__control").sendKeys("二月五 十八月");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='name'] .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfChineseCity() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("二月五 十八月");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='city'] .input__sub").shouldHave(Condition.text("Доставка в выбранный город недоступна"));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormWithoutAgreement() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[type='button'].button").click();

        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfPhoneLess() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+8963545");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='phone'] .input__sub").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfPhoneMore() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+896354595623456");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='phone'] .input__sub").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfNoPlusInPhone() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='phone'] .input__sub").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfSelectedTomorrow() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").setValue(
                generateMinusDate(1)
        );
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='date'] .input__sub").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfSelectedMonthBack() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").setValue(
                generateMinusDate(30)
        );
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='date'] .input__sub").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfSelectedYearBack() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").setValue(
                generateMinusDate(365)
        );
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='date'] .input__sub").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfNameIsEmpty() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='name'] .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfCityIsEmpty() {
        open(site);
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='city'] .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfPhoneIsEmpty() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='phone'] .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfDateIsEmpty() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $("[data-test-id='date'] .input__sub").shouldHave(Condition.text("Неверно введена дата"));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

    @Test
    public void shouldNotFillFormIfNoAgreement() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("Ижевск");
        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635459562");

        $("[type='button'].button").click();

        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $("[data-test-id='agreement'] .input_invalid");
    }

    @Test
    public void shouldNotFillFormIfAllAreEmpty() {
        open(site);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[type='button'].button").click();

        $("[data-test-id='city'] .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"));
        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $("[data-test-id='agreement'] .input_invalid");
    }

    @Test
    public void shouldNotFillFormIfCity1Letters() {
        open(site);
        $("[data-test-id='city'] .input__control").sendKeys("И");
        $$(".input__menu .menu-tem__control").findBy(Condition.text("Ижевск")).shouldBe(Condition.hidden);

        $("[data-test-id='name'] .input__control").sendKeys("Кирилл");
        $("[data-test-id='phone'] .input__control").setValue("+89635452462");

        $("[data-test-id='agreement']").click();
        $("[type='button'].button").click();

        $(".spin").shouldBe(Condition.hidden, Duration.ofSeconds(15));
        $(".notification").shouldBe(Condition.hidden, Duration.ofSeconds(15));
    }

}
