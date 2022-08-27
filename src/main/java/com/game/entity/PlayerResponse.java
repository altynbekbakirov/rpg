package com.game.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class PlayerResponse {
    private String name;
    private String title;
    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private Profession profession;
    private Long birthday;
    private Integer experience;
    private Boolean banned;


    public PlayerResponse() {
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Race getRace() {
        return race;
    }

    public Profession getProfession() {
        return profession;
    }

    public Long getBirthday() {
        return birthday;
    }

    public Integer getExperience() {
        return experience;
    }

    public Boolean getBanned() {
        return banned;
    }
}
