package ru.netology.delivery.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class RegistrationByCardInfo {
    private final String city;
    private final String date;
    private final String changeDate;
    private final String name;
    private final String phone;
}
