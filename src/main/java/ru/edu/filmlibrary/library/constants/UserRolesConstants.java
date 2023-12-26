package ru.edu.filmlibrary.library.constants;

public enum UserRolesConstants {

    ADMIN("Администратор"),
    MODERATOR("Модератор"),
    USER("Пользователь");

    private final String roleTextDisplay;

    UserRolesConstants(String text) {
        this.roleTextDisplay = text;
    }

    public String getRoleTextDisplay() {
        return roleTextDisplay;
    }

}
