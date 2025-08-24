package com.example.lazyco.backend.core.DateUtils;


import lombok.Getter;

@Getter
public enum DateTimeFormatEnum {
    yyyy_MM_dd_T_HH_mm_ssXXX("yyyy-MM-dd'T'HH:mm:ssXXX"),
    yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
    yyyy_MM_dd("yyyy-MM-dd"),
    HH_mm_ss("HH:mm:ss");

    private final String value;

    DateTimeFormatEnum(String value) {
        this.value = value;
    }
}