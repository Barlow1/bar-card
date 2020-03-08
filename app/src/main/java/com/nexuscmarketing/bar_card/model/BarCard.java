package com.nexuscmarketing.bar_card.model;

import java.io.Serializable;
import java.util.UUID;

public class BarCard implements Serializable {

    private UUID id;
    private int image;
    private String barName;
    private String reward;
    private Integer punchesRemaining;

    public Integer getPunchesRemaining() {
        return punchesRemaining;
    }

    public void setPunchesRemaining(Integer punchesRemaining) {
        this.punchesRemaining = punchesRemaining;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
