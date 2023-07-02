package me.rexe0.uhcchampions.util;

public enum Attribute {
    GENERIC_ATTACK_DAMAGE("generic.attackDamage"),
    GENERIC_ATTACK_SPEED("generic.attackSpeed");

    private final String name8;

    private String name;

    Attribute(String name8) {
        this.name8 = name8;
    }

    public String getName() {
       return name8;
    }
}
