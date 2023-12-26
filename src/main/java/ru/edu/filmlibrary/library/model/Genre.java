package ru.edu.filmlibrary.library.model;

/**
 * ID жанров для базы данных
 * ACTION = 0
 * ANIMATED = 1
 * COMEDY = 2
 * DRAMA = 3
 * FANTASY = 4
 * HISTORICAL = 5
 * HORROR = 6
 * MUSICAL = 7
 * SCIENCE_FICTION = 8
 * THRILLER = 9
 */

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
