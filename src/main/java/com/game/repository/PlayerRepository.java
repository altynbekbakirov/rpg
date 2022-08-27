package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("SELECT p FROM Player p WHERE (:name is null or lower(p.name) like lower(concat('%', :name,'%'))) " +
            "and (:title is null or lower(p.title) like lower(concat('%', :title,'%'))) " +
            "and (:banned is null or p.banned = :banned) " +
            "and (:race is null or p.race = :race) " +
            "and (:profession is null or p.profession = :profession) " +
            "and (:maxLevel is null or p.level < :maxLevel) " +
            "and (:minLevel is null or p.level > :minLevel) " +
            "and (:maxExperience is null or p.experience < :maxExperience) " +
            "and (:minExperience is null or p.experience > :minExperience) " +
            "and (:before is null or p.birthday <= :before) " +
            "and (:after is null or p.birthday >= :after)")
    List<Player> getAll(
            @Param("name") String name,
            @Param("title") String title,
            @Param("banned") Boolean banned,
            @Param("race") Race race,
            @Param("profession") Profession profession,
            @Param("maxLevel") Integer maxLevel,
            @Param("minLevel") Integer minLevel,
            @Param("minExperience") Integer minExperience,
            @Param("maxExperience") Integer maxExperience,
            @Param("before") Date before,
            @Param("after") Date after,
            Pageable pageable
    );

    @Query("SELECT p FROM Player p WHERE (:name is null or lower(p.name) like lower(concat('%', :name,'%'))) " +
            "and (:title is null or lower(p.title) like lower(concat('%', :title,'%'))) " +
            "and (:race is null or p.race = :race) " +
            "and (:profession is null or p.profession = :profession) " +
            "and (:after is null or p.birthday >= :after) " +
            "and (:before is null or p.birthday <= :before) " +
            "and (:banned is null or p.banned = :banned) " +
            "and (:minExperience is null or p.experience > :minExperience) " +
            "and (:maxExperience is null or p.experience < :maxExperience) " +
            "and (:minLevel is null or p.level > :minLevel) " +
            "and (:maxLevel is null or p.level < :maxLevel) " +
            "")
    List<Player> getCount(
            @Param("name") String name,
            @Param("title") String title,
            @Param("banned") Boolean banned,
            @Param("race") Race race,
            @Param("profession") Profession profession,
            @Param("maxLevel") Integer maxLevel,
            @Param("minLevel") Integer minLevel,
            @Param("minExperience") Integer minExperience,
            @Param("maxExperience") Integer maxExperience,
            @Param("before") Date before,
            @Param("after") Date after
            );
}
