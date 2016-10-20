package com.roc.dao;

import com.roc.enity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by roc on 2016/10/20.
 */
@Repository
public interface GameDao extends CrudRepository<Game, Long> {
}
