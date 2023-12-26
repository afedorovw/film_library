package ru.edu.filmlibrary.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.edu.filmlibrary.library.model.Users;

import java.util.List;

@Repository
public interface UsersRepository extends GenericRepository<Users> {

    Users findUserByLogin(String login);

    Users findUserByEmail(String email);

    Users findUserByLoginAndIsDeletedFalse(String login);

    Users findUserByChangePasswordToken(String uuid);

    @Query(nativeQuery = true,
            value = """
                    select u.*
                    from users u
                    where u.first_name ilike '%' || coalesce(:firstName, '%') || '%'
                    and u.last_name ilike '%' || coalesce(:lastName, '%') || '%'
                    and u.login ilike '%' || coalesce(:login, '%') || '%'
                     """)
    Page<Users> searchUsers(String firstName,
                            String lastName,
                            String login,
                            Pageable pageable);


    @Query(nativeQuery = true,
            value = """
                    select distinct email
                    from Users u 
                    join orders bri on u.id = bri.user_id
                    where bri.return_date < now()
                    and bri.returned = false
                    and u.is_deleted = false
                    """)
    List<String> getDelayedEmails();
}
