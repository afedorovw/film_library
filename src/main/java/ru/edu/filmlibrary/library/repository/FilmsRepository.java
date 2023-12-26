package ru.edu.filmlibrary.library.repository;

import org.springframework.stereotype.Repository;
import ru.edu.filmlibrary.library.model.Films;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface FilmsRepository extends GenericRepository<Films> {

    @Query(nativeQuery = true,
            value = """
                       select distinct b.*
                       from films b
                                left join films_directors ba on b.id = ba.film_id
                                left join directors a on a.id = ba.director_id
                       where b.title ilike '%' || coalesce(:title, '%')  || '%'
                         and cast(b.genre as char) like coalesce(:genre, '%')
                         and coalesce(a.directors_fio, '%') ilike '%' ||  coalesce(:fio, '%')  || '%'
                         and b.is_deleted = false
                    """)
    Page<Films> searchFilms(@Param(value = "title") String title,
                            @Param(value = "genre") String genre,
                            @Param(value = "fio") String directorFIO,
                            Pageable pageRequest);

    @Query(nativeQuery = true,
            value = """
                    update films
                    set stars = 2.0
                    where id = 1;
                    """)
    Double updateStars(Double var);

    @Query("""
            select case when count(b) > 0 then false else true end
            from Films b join Order bri on b.id = bri.film.id
            where b.id = :id and bri.returned = false
            """)
    boolean isFilmCanBeDeleted(final Long id);

    Page<Films> findAllByTitleContainsIgnoreCase(String title, Pageable pageable);

}
