package com.roc.dao;

import com.roc.enity.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberDao extends CrudRepository<Player, Long> {

    @Query("SELECT player FROM Player player WHERE player.gid = :gid")
    List<Player> getAllMember(@Param("gid") long gid);

    @Query("SELECT player FROM Player player WHERE player.gid = :gid AND player.name = :name")
    List<Player> getOneOfMember(@Param("gid") long gid,@Param("name") String name);
}
