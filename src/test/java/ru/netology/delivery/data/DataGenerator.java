package ru.netology.delivery.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private DataGenerator() {
    }

    public static class Registration {

        private Registration() {
        }

        public static RegistrationByCardInfo generateByCard(String locale, int days, int changeDays) {
            Faker faker = new Faker(new Locale(locale));
            return new RegistrationByCardInfo(
                    faker.address().city(),
                    LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    LocalDate.now().plusDays(changeDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    faker.name().lastName() + " " + faker.name().firstName(),
                    faker.phoneNumber().phoneNumber()
            );
        }
    }
}
