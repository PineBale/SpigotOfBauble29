package dev.pinebale.minecraft.spigotofbauble29;

import dev.pinebale.minecraft.spigotofbauble29.events.PlayerMessageEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UtilPlayerBase {

    /**
     * Sends multiple messages to a player, respecting {@link PlayerMessageEvent} cancellation.
     */
    public static void sendMessage(Player client, List<String> messageList) {
        for (String message : messageList) {
            sendMessage(client, message);
        }
    }

    /**
     * Sends multiple messages to any CommandSender (no event firing).
     */
    public static void sendMessage(CommandSender recipient, List<String> messageList) {
        for (String message : messageList) {
            sendMessage(recipient, message);
        }
    }

    /**
     * Sends a single message to a Player, firing {@link PlayerMessageEvent} first.
     * If the event is cancelled, the message is not sent.
     */
    public static void sendMessage(Player client, String message) {
        PlayerMessageEvent event = new PlayerMessageEvent(client, message);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        sendMessage((CommandSender) client, event.getMessage());
    }

    /**
     * Sends a single message to any CommandSender with color code translation.
     * Does NOT fire {@link PlayerMessageEvent}.
     */
    public static void sendMessage(CommandSender recipient, String message) {
        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Searches for an online player by name (exact or partial match).
     *
     * <p>Behavior:</p>
     * <ul>
     *   <li>Exact match (case-insensitive) → returns immediately</li>
     *   <li>One partial match → returns that player</li>
     *   <li>Multiple partial matches → informs caller (if inform=true) and returns null</li>
     *   <li>No matches → returns null</li>
     * </ul>
     *
     * @param caller The player who is performing the search (receives feedback if needed)
     * @param target The name or partial name to search for
     * @param inform Whether to send feedback message to caller when multiple matches are found
     * @return The matched Player, or null if no unique match
     */
    public static Player searchOnline(Player caller, String target, boolean inform) {
        List<Player> matchList = new ArrayList<>();

        String targetLower = target.toLowerCase(Locale.ROOT);

        for (Player player : Bukkit.getOnlinePlayers()) {
            String playerName = player.getName();
            String playerNameLower = playerName.toLowerCase(Locale.ROOT);

            if (playerNameLower.equals(targetLower)) {
                return player;
            }

            if (playerNameLower.contains(targetLower)) {
                matchList.add(player);
            }
        }

        if (matchList.isEmpty()) {
            return null;
        }

        if (matchList.size() == 1) {
            return matchList.get(0);
        }

        if (!inform) {
            return null;
        }

        StringBuilder names = new StringBuilder();
        for (int i = 0; i < matchList.size(); i++) {
            if (i > 0) {
                names.append("&r, &e");
            }
            names.append(matchList.get(i).getName());
        }

        String feedback = String.format(
                "&e%d&r possible matches for [&e%s&r]:\n&e%s&r",
                matchList.size(),
                target,
                names
        );

        sendMessage(caller, feedback);

        return null;
    }
}