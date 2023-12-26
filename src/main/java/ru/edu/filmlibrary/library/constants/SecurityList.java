package ru.edu.filmlibrary.library.constants;

public interface SecurityList {

    String[] RESOURCES_WHITE_LIST = {
            "/resources/**",
            "/",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    String[] USERS_REST_WHITE_LIST = {"/users/auth"};

}
