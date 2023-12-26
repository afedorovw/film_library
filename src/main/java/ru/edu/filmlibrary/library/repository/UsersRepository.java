package ru.edu.filmlibrary.library.repository;

import org.springframework.stereotype.Repository;
import ru.edu.filmlibrary.library.model.Users;

@Repository
public interface UsersRepository extends GenericRepository<Users> {

    //@Query(nativeQuery = true,
    //value = "select * from users where login = :login")
    Users findUserByLogin(String login);

    Users findUserByEmail(String email);

//    User getUserByPhone(String phone);

}
