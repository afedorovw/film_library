package ru.edu.filmlibrary.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.edu.filmlibrary.library.model.Directors;

@Repository
public interface DirectorsRepository extends GenericRepository<Directors> {

    Page<Directors> searchAllByDirectorsFIOContainingIgnoreCase(String fio, Pageable pageable);

    @Query(value = """
            select case when count(d) > 0 then false else true end
            from Directors d
            join films f on f.id = d.id
            join orders o on f.id = o.id
            where d.id = :directorId
            and o.returned = false
            """)
    boolean checkDirectorForDeletion(final Long directorId);
}
