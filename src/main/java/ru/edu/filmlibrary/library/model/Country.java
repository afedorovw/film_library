package ru.edu.filmlibrary.library.model;

public enum Country {

    USA("США"),
    UNITED_KINGDOM("Великобритания");

    private final String countryTextDisplay;

    Country(String text) {
        this.countryTextDisplay = text;
    }

    public String getCountryTextDisplay() {
        return countryTextDisplay;
    }


}

