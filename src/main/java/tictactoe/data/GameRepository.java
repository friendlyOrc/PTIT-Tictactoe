package tictactoe.data;

import tictactoe.Game;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `game`(id, acc1_id, acc2_id, result) VALUES (?1, ?2, ?3, 0)", nativeQuery = true)
    void addNew(int id, int accID1, int accID2);

    @Query(value = "SELECT * FROM `game` WHERE id = ?1 ", nativeQuery = true)
    ArrayList<Game> findGame(int id);
}