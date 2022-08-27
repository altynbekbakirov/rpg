package com.game.controller;

import com.game.entity.Player;
import com.game.entity.PlayerResponse;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerService service;

    @Autowired
    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @GetMapping("/players")
    public List<Player> getPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "order", required = false) PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        return service.getAllUsers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, order, pageNumber, pageSize);
    }

    @GetMapping("/players/count")
    public int getUserCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel
            ) {
        return service.getUserCount(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<?> getPlayerById(@PathVariable String id) {
        try {
            if (Long.parseLong(id) == 0) {
                return ResponseEntity.badRequest().body("Zero value not allowed");
            }
            if (!service.getPlayerById(Long.parseLong(id)).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
            }
            return ResponseEntity.ok(service.getPlayerById(Long.parseLong(id)).get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zero value not valid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
        }
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<?> deletePlayerById(@PathVariable String id) {
        try {
            if (Long.parseLong(id) == 0) {
                return ResponseEntity.badRequest().body("Zero value not allowed");
            }

            if (!service.getPlayerById(Long.parseLong(id)).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
            }
            service.deletePlayerById(Long.parseLong(id));
            return ResponseEntity.status(HttpStatus.OK).body("User deleted");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Value is not valid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exception occurred");
        }
    }

    @PostMapping("/players")
    public ResponseEntity<?> createPlayer(@RequestBody PlayerResponse response) {
        if (
                response.getName() == null || response.getTitle() == null
                        || response.getRace() == null || response.getProfession() == null
                        || response.getExperience() == null || response.getBirthday() == null
                        || response.getName().trim().length() > 12 || response.getTitle().trim().length() > 30
                        || response.getExperience() < 0 || response.getExperience() > 10000000
                        || response.getBirthday() < 0
        ) {
            return ResponseEntity.badRequest().body("Not allowed");
        } else {
            int level = (int) ((Math.sqrt(2500 + 200 * response.getExperience()) - 50) / 100);
            int nextLevel = 50 * (level + 1) * (level + 2) - response.getExperience();

            Player newPlayer = new Player();
            newPlayer.setName(response.getName());
            newPlayer.setTitle(response.getTitle());
            newPlayer.setRace(response.getRace());
            newPlayer.setProfession(response.getProfession());
            newPlayer.setExperience(response.getExperience());
            newPlayer.setLevel(level);
            newPlayer.setUntilNextLevel(nextLevel);
            newPlayer.setBirthday(new Date(response.getBirthday()));
            newPlayer.setBanned(response.getBanned() ? response.getBanned() : false);
            service.createPlayer(newPlayer);
            return ResponseEntity.ok(newPlayer);
        }
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<?> updatePlayer(@RequestBody PlayerResponse response, @PathVariable String id) {
        try {
            if (Long.parseLong(id) == 0) {
                return ResponseEntity.badRequest().body("Not allowed");
            }

            if (!service.getPlayerById(Long.parseLong(id)).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
            } else {
                Player player = service.getPlayerById(Long.parseLong(id)).get();
                if (response.getName() != null) player.setName(response.getName());
                if (response.getTitle() != null) player.setTitle(response.getTitle());
                if (response.getRace() != null) player.setRace(response.getRace());
                if (response.getProfession() != null) player.setProfession(response.getProfession());
                if (response.getBirthday() != null) {
                    if (response.getBirthday() < 0) {
                        return ResponseEntity.badRequest().body("Not allowed");
                    } else {
                        player.setBirthday(new Date(response.getBirthday()));
                    }
                }
                if (response.getExperience() != null) {
                    if (response.getExperience() < 0 || response.getExperience() > 10000000) {
                        return ResponseEntity.badRequest().body("Not allowed");
                    } else {
                        int level = (int) ((Math.sqrt(2500 + 200 * response.getExperience()) - 50) / 100);
                        int nextLevel = 50 * (level + 1) * (level + 2) - response.getExperience();
                        player.setExperience(response.getExperience());
                        player.setLevel(level);
                        player.setUntilNextLevel(nextLevel);
                    }
                }
                if (response.getBanned() != null) player.setBanned(response.getBanned());
                return ResponseEntity.ok(player);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Not allowed");
        }
    }
}
