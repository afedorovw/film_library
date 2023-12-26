package ru.edu.filmlibrary.library.model;

public enum Genre {
    ACTION("Экшен"),
    ANIMATED("Мультфильм"),
    COMEDY("Комедия"),
    DRAMA("Драма"),
    FANTASY("Фантастика"),
    HISTORICAL("Исторический"),
    HORROR("Хоррор"),
    MUSICAL("Мюзикл"),
    SCIENCE_FICTION("Научная фантастика"),
    THRILLER("Триллер");

    private final String genreTextDisplay;

    Genre(String text) {
        this.genreTextDisplay = text;
    }

    public String getGenreTextDisplay() {
        return genreTextDisplay;
    }


}
