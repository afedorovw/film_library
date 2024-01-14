package ru.edu.filmlibrary.service;

import ru.edu.filmlibrary.library.dto.DirectorsDTO;
import ru.edu.filmlibrary.library.model.Country;
import ru.edu.filmlibrary.library.model.Directors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DirectorTestData {
    DirectorsDTO DIRECTOR_DTO_1 = new DirectorsDTO("directorFio1",
            LocalDate.now(),
            "description1",
            new ArrayList<>());

    DirectorsDTO DIRECTOR_DTO_2 = new DirectorsDTO("directorFio2",
            LocalDate.now(),
            "description2",
            new ArrayList<>());

    DirectorsDTO DIRECTOR_DTO_3_DELETED = new DirectorsDTO("directorFio3",
            LocalDate.now(),
            "description3",
            new ArrayList<>());

    List<DirectorsDTO> DIRECTOR_DTO_LIST = Arrays.asList(DIRECTOR_DTO_1, DIRECTOR_DTO_2, DIRECTOR_DTO_3_DELETED);


    Directors DIRECTOR_1 = new Directors("director1",
            LocalDate.now(),
            Country.USA,
            "description1",
            "photo1",
            null);

    Directors DIRECTOR_2 = new Directors("director2",
            LocalDate.now(),
            Country.UNITED_KINGDOM,
            "bio2",
            "photo2",
            null);

    Directors DIRECTOR_3 = new Directors("director3",
            LocalDate.now(),
            Country.UNITED_KINGDOM,
            "description3",
            "photo3",
            null);

    List<Directors> DIRECTOR_LIST = Arrays.asList(DIRECTOR_1, DIRECTOR_2, DIRECTOR_3);
}
