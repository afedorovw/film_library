package ru.edu.filmlibrary.library.constants;

public interface WEBSecurityList {

    String[] RESOURCES_WHITE_LIST = {
            "/resources/**",
            "/static/**",
            "/js/**",
            "/css/**",
            "/img/**",
            "/",
            "/error/**",
            "/swagger-ui/**",
            "/webjars/bootstrap/5.3.0/**",
            "/webjars/bootstrap/5.3.0/css/**",
            "/webjars/bootstrap/5.3.0/js/**",
            "/v3/api-docs/**"
    };

    String[] ADMIN_LIST = {
            "/films/**",
            "/directors/**",
            "/orders/**",
            "/users/**",
            "/users/profile/{id}"
    };

    String[] FILMS_WHITE_LIST = {
            "/films",
            "/films/search",
            "/films/{id}"
    };

    String[] FILMS_PERMISSION_LIST = {
            "/films/add",
            "/films/update",
            "/films/delete",
            "/films/download/{filmId}",
            "/films/setStar"
    };

    String[] DIRECTORS_WHITE_LIST = {
            "/directors",
            "/directors/search",
            "/films/search/filmsByDirector",
            "/directors/{id}"
    };

    String[] DIRECTORS_PERMISSION_LIST = {
            "/directors/add",
            "/directors/update",
            "/directors/delete"
    };

    String[] USER_WHITE_LIST = {
            "/users/login",
            "/users/registration",
            "/users/remember-password",
            "/users/change-password"
    };

    String[] USERS_PERMISSION_LIST = {
            "/orders/user-films/{id}",
            "/orders/film",
            "/orders/film/{id}",
            "/orders/return-film/{id}",
            "/users/profile/{id}",
            "/users/profile/edit/{id}",
            "/users/change-password/user/{id}",
            "/films/setStar/{id}"
    };
}
