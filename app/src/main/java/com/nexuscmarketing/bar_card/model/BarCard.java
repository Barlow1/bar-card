package com.nexuscmarketing.bar_card.model;

import java.io.Serializable;
import java.util.UUID;

public class BarCard implements Serializable {

    private UUID id;
    private int image;
    private String barName;
    private String reward;
    private Integer punches;
    private Integer rewardPunches;

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


    public Integer getPunches() {
        return punches;
    }

    public void setPunches(Integer punches) {
        this.punches = punches;
    }

    public Integer getRewardPunches() {
        return rewardPunches;
    }

    public void setRewardPunches(Integer rewardPunches) {
        this.rewardPunches = rewardPunches;
    }
}
