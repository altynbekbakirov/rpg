package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    @Autowired
    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public List<Player> getPlayers() {
        return repository.findAll();
    }

    public int getUserCount(String name, String title, Race race,
                            Profession profession, Long after, Long before,
                            Boolean banned, Integer minExperience, Integer maxExperience,
                            Integer minLevel, Integer maxLevel) {
        Date begDate = null, endDate = null;
        if (before != null) {
            begDate = new Date(before);
        }
        if (after != null) {
            endDate = new Date(after);
        }
        return repository.getCount(
                name,
                title,
                banned,
                race,
                profession,
                maxLevel,
                minLevel,
                minExperience,
                maxExperience,
                begDate,
                endDate
        ).size();
    }

    public List<Player> getAllUsers(String name, String title, Race race,
                                    Profession profession, Long after, Long before,
                                    Boolean banned, Integer minExperience, Integer maxExperience,
                                    Integer minLevel, Integer maxLevel, PlayerOrder order, Integer pageNumber, Integer pageSize) {
        Date begDate = null, endDate = null;
        if (before != null) {
            begDate = new Date(before);
        }
        if (after != null) {
            endDate = new Date(after);
        }
        if (order == null) {
            order = PlayerOrder.ID;
        }
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 3;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "id"));

        return repository.getAll(
                name,
                title,
                banned,
                race,
                profession,
                maxLevel,
                minLevel,
                minExperience,
                maxExperience,
                begDate,
                endDate,
                pageable
        );
    }

    public Optional<Player> getPlayerById(Long id) {
        return repository.findById(id);
    }

    public void deletePlayerById(Long id) {
        repository.deleteById(id);
    }

    public Player createPlayer(Player player) {
        return repository.save(player);
    }


}
