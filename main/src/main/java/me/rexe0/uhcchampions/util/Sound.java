package me.rexe0.uhcchampions.util;

public enum Sound {
    IRON_GOLEM_DEATH("IRONGOLEM_DEATH", "ENTITY_IRON_GOLEM_DEATH"),
    EXPLODE("EXPLODE", "ENTITY_GENERIC_EXPLODE"),
    WOODEN_DOOR_BREAK("ZOMBIE_WOODBREAK", "ENTITY_ZOMBIE_BREAK_WOODEN_DOOR"),
    ANVIL_USE("ANVIL_USE", "BLOCK_ANVIL_USE"),
    ENDERMAN_TELEPORT("ENDERMAN_TELEPORT", "ENTITY_ENDERMAN_TELEPORT"),
    CLICK("CLICK", "UI_BUTTON_CLICK"),
    LEVEL_UP("LEVEL_UP", "ENTITY_PLAYER_LEVELUP"),
    BURP("BURP", "ENTITY_PLAYER_BURP");


    private final String name8, name19;

    private org.bukkit.Sound sound;

    Sound(String name8, String name19) {
        this.name8 = name8;
        this.name19 = name19;
    }

    public org.bukkit.Sound getSound() {
        if (sound == null) {
            try {
                if (VersionUtils.getVersion().equals("1.8")) sound = org.bukkit.Sound.valueOf(name8);
                else sound = org.bukkit.Sound.valueOf(name19);
            } catch (IllegalArgumentException ignored) {

            }
        }
        return sound;
    }
}
