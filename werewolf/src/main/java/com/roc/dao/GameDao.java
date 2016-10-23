package com.roc.dao;

import com.roc.enity.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by roc on 2016/10/20.
 */
@Repository
public interface GameDao extends CrudRepository<Game, Long> {
    @Query("SELECT game FROM Game game WHERE game.uid = :uid ORDER BY game.id DESC")
    List<Game> getGameList(@Param("uid") long uid);
}
