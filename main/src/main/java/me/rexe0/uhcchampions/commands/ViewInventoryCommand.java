package me.rexe0.uhcchampions.commands;

import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.players.UhcPlayer;
import me.rexe0.uhcchampions.UHCChampions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ViewInventoryCommand implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!UHCChampions.getConfigLoader().isViewInventory()) {
            sender.sendMessage(ChatColor.RED + "This command is not enabled.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }
        Player player = (Player) sender;
        UhcPlayer uhcPlayer = GameManager.getGameManager().getPlayerManager().getUhcPlayer(player);
        if (!uhcPlayer.isDead()) {
            sender.sendMessage(ChatColor.RED + "You can only use this command while dead.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /viewinventory <player>");
            return true;
        }


        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)  {
            sender.sendMessage(ChatColor.RED + "Not a valid player.");
            return true;
        }
        UhcPlayer uhcTarget = GameManager.getGameManager().getPlayerManager().getUhcPlayer(target);

        if (uhcTarget.isDead())  {
            sender.sendMessage(ChatColor.RED + "Not a valid player.");
            return true;
        }

        boolean teamDead = uhcPlayer.getTeam().getMembers().stream()
                .allMatch(UhcPlayer::isDead);
        if (!teamDead && !uhcPlayer.isInTeamWith(uhcTarget)) {
            sender.sendMessage(ChatColor.RED + "You can only view a teammate's inventory if your team is still alive.");
            return true;
        }

        ItemStack[] contents = target.getInventory().getContents();

        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY+target.getName()+"'s Inventory");
        for (int i = 0; i < 36; i++)
            inv.setItem(i, contents[i]);
        for (int i = 0; i < 4; i++)
            inv.setItem(45+i, target.getInventory().getArmorContents()[i]);

        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED+"Exit");
        item.setItemMeta(meta);
        inv.setItem(53, item);

        player.openInventory(inv);
        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith("'s Inventory")) return;
        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        if (item.getItemMeta().getDisplayName().equals(ChatColor.RED+"Exit"))
            Bukkit.getScheduler().runTaskLater(UHCChampions.getInstance(), e.getWhoClicked()::closeInventory, 1);
    }
}
