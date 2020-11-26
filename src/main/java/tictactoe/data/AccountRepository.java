package tictactoe.data;

import tictactoe.Account;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
    @Query(value = "SELECT * FROM `account` WHERE username = ?1 and password = ?2", nativeQuery = true)
    ArrayList<Account> findAccount(String un, String pw);
}