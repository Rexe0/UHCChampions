/*
 * Copyright (c) 2022 Thomas Bringemeier - All Rights Reserved
 * This file is a part of Divinity and no users except
 * the author have the right to use, merge, publish,
 * copy, sell, modify and/or distribute this file or copies of it
 * unless otherwise explicitly stated by the author.
 *
 * The above copyright notice and this permission notice shall be included in
 * and applies to all copies or substantial portions of the Software/Project.
 */

package me.rexe0.uhcchampions.util;

import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.UhcPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class HomingArrowRunnable extends BukkitRunnable {

    private Arrow arrow;
    private LivingEntity target;
    private Player shooter;
    private double speed;

    public HomingArrowRunnable(Arrow arrow, Player shooter) {
        this.arrow = arrow;
        this.shooter = shooter;

        Vector velocity = arrow.getVelocity();
        BigDecimal x = new BigDecimal(velocity.getX() * velocity.getX());
        BigDecimal y = new BigDecimal(velocity.getY() * velocity.getY());
        BigDecimal z = new BigDecimal(velocity.getZ() * velocity.getZ());
        speed = Math.min(1.5, sqrt(x.add(y).add(z), 32).doubleValue());
    }

    @Override
    public void run() {
        if (arrow.isDead()) {
            cancel();
            return;
        }
        if (arrow.isOnGround()) {
            arrow.remove();
            cancel();
            return;
        }

        if (target == null) {
            setTarget();
            if (target == null) return;
        }
        if (target.isDead()) {
            target = null;
            return;
        }

        Location location = arrow.getLocation();
        float locX = (float) location.getX();
        float locY = (float) location.getY();
        float locZ = (float) location.getZ();
        Location targetLocation = target.getEyeLocation();

        double distance = location.distance(targetLocation);
        double targetX = targetLocation.getX();
        double targetY = targetLocation.getY();
        double targetZ = targetLocation.getZ();
        double diffX = targetX - locX;
        double diffZ = targetZ - locZ;
        double plainDistance = Math.sqrt(diffX * diffX + diffZ * diffZ);
        double heightDistance = targetY - locY;
        double plainRatio = plainDistance / distance;
        double heightRatio = heightDistance / distance;
        double plainMotion = plainRatio / speed;
        double xRatio = diffX / plainDistance;
        double zRatio = diffZ / plainDistance;
        double motionX = xRatio / plainMotion;
        double motionY = heightRatio / speed;
        double motionZ = zRatio / plainMotion;


        arrow.setVelocity(new Vector(motionX, motionY, motionZ));
    }


    private void setTarget() {
        List<Player> nearbyEntities = new ArrayList<>();
        UhcPlayer player = GameManager.getGameManager().getPlayerManager().getUhcPlayer(shooter);
        for (Entity entity : arrow.getNearbyEntities(8, 8, 8)) {
            if (!(entity instanceof Player)) continue;
            Player p = (Player) entity;
            if (!p.hasLineOfSight(arrow)) continue;
            if (GameManager.getGameManager().getPlayerManager().getUhcPlayer(p).isInTeamWith(player)) continue;
            nearbyEntities.add(p);
        }

        if (nearbyEntities.size() == 0)
            return;

        Optional<Player> optionalEntity = nearbyEntities.stream()
                .min(Comparator.comparing(entity -> entity.getLocation().distanceSquared(arrow.getLocation())));

        target = optionalEntity.get();
    }

    private static BigDecimal sqrt(BigDecimal A, final int SCALE) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(BigDecimal.valueOf(2), SCALE, ROUND_HALF_UP);

        }
        return x1;
    }
}