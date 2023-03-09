package me.rexe0.uhcchampions.util;

public enum Material {
    PLAYER_HEAD("SKULL_ITEM", "PLAYER_HEAD", (short) 3),
    WITHER_SKELETON_SKULL("SKULL_ITEM", "WITHER_SKELETON_SKULL", (short) 1),
    NETHER_WART("NETHER_WARTS", "NETHER_WART");

    private final String name8, name19;
    private final short data8;

    private org.bukkit.Material material;

    Material(String name8, String name19, short data8) {
        this.name8 = name8;
        this.name19 = name19;
        this.data8 = data8;
    }

    Material(String name8, String name19) {
        this(name8, name19, (short) 0);
    }


    public org.bukkit.Material getMaterial() {
        if (material == null) {
            try {
                if (VersionUtils.getVersion().equals("1.8")) material = org.bukkit.Material.valueOf(name8);
                else material = org.bukkit.Material.valueOf(name19);
            } catch (IllegalArgumentException ignored) {

            }
        }
        return material;
    }

    public short getData() {
        return data8;
    }
}
