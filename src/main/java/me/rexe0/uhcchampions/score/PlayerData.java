package me.rexe0.uhcchampions.score;

import java.util.UUID;

public class PlayerData {
    private UUID uuid;
    private int wins;
    private int kills;

    public PlayerData(UUID uuid, int wins, int kills) {
        this.uuid = uuid;
        this.wins = wins;
        this.kills = kills;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getWins() {
        return wins;
    }

    public int getKills() {
        return kills;
    }
    public int getScore() {
        return kills+(wins*5);
    }

    public void addWin() {
        wins++;
    }
    public void addKill() {
        kills++;
    }
}
