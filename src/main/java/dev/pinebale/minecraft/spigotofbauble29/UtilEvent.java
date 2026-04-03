package dev.pinebale.minecraft.spigotofbauble29;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public final class UtilEvent {

    public static boolean isAction(PlayerInteractEvent event, ActionType actionType) {
        return isAction(event.getAction(), actionType);
    }

    public static boolean isAction(Action action, ActionType actionType) {
        switch (actionType) {
            case L:
                return action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK);
            case L_AIR:
                return action.equals(Action.LEFT_CLICK_AIR);
            case L_BLOCK:
                return action.equals(Action.LEFT_CLICK_BLOCK);
            case R:
                return action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
            case R_AIR:
                return action.equals(Action.RIGHT_CLICK_AIR);
            case R_BLOCK:
                return action.equals(Action.RIGHT_CLICK_BLOCK);
            case ANY:
                return !action.equals(Action.PHYSICAL);
            default:
                return false;
        }
    }

    public static boolean isBowDamage(EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent)) {
            return false;
        }
        EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) event;
        return edbe.getDamager() instanceof Arrow;
    }

    public static LivingEntity getDamagerEntity(EntityDamageEvent event) {
        return getDamagerEntity(event, true);
    }

    /**
     * Returns the LivingEntity that caused the damage (the "damager").
     * For ranged attacks (arrows, trident, snowball, egg, etc.), returns the shooter if ranged = true.
     *
     * @param event  the damage event
     * @param ranged whether to consider projectiles and return their shooter
     * @return the LivingEntity responsible for the damage, or null
     */
    public static LivingEntity getDamagerEntity(EntityDamageEvent event, boolean ranged) {
        if (!(event instanceof EntityDamageByEntityEvent)) {
            return null;
        }

        EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) event;

        // Direct living entity damager (punch, sword, etc.)
        if (edbe.getDamager() instanceof LivingEntity) {
            return (LivingEntity) edbe.getDamager();
        }

        if (!ranged) {
            return null;
        }

        if (!(edbe.getDamager() instanceof Projectile)) {
            return null;
        }

        Projectile projectile = (Projectile) edbe.getDamager();
        if (projectile.getShooter() == null) {
            return null;
        }

        if (!(projectile.getShooter() instanceof LivingEntity)) {
            return null;
        }

        return (LivingEntity) projectile.getShooter();
    }

    public enum ActionType {
        L,
        L_AIR,
        L_BLOCK,
        R,
        R_AIR,
        R_BLOCK,
        ANY
    }
}
