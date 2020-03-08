package com.nexuscmarketing.bar_card.model;

import java.util.UUID;

public class Bar {

    private UUID id;
    private String barName;
    private String imageName;
    private Integer rewardPunches;
    private String reward;


    public Bar(UUID id, String barName, String imageName, Integer rewardPunches, String reward) {
        this.id = id;
        this.barName = barName;
        this.imageName = imageName;
        this.rewardPunches = rewardPunches;
        this.reward = reward;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getRewardPunches() {
        return rewardPunches;
    }

    public void setRewardPunches(Integer rewardPunches) {
        this.rewardPunches = rewardPunches;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

}
