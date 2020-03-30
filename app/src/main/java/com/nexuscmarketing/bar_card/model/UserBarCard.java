package com.nexuscmarketing.bar_card.model;

import java.util.UUID;

public class UserBarCard extends User {
    private UUID barCardId;
    private int punches;

    public int getPunches() {
        return punches;
    }

    public void setPunches(int punches) {
        this.punches = punches;
    }

    public UUID getBarCardId() {
        return barCardId;
    }

    public void setBarCardId(UUID barCardId) {
        this.barCardId = barCardId;
    }
}
