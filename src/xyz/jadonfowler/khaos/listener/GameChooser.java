package xyz.jadonfowler.khaos.listener;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.jadonfowler.khaos.Khaos;
import xyz.jadonfowler.khaos.game.Arena;
import xyz.jadonfowler.khaos.game.ArenaState;
import xyz.jadonfowler.khaos.game.Game;

public class GameChooser implements Listener {

    @EventHandler public void entityInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        Entity n = e.getRightClicked();
        if (!Khaos.getInstance().isInGame(p)) {
            Khaos.getInstance().getLogger().info(p.getName() + " clicked " + n.getCustomName() + ".");
            if (n.hasMetadata("khaos-game")) {
                Khaos.getInstance().getLogger().info(n.getCustomName() + " has the correct metadata.");

                String game = n.getMetadata("khaos-game").get(0).asString();
                Khaos.getInstance().getLogger().info("Got game '" + game + "'.");
                if (Khaos.getInstance().gameExists(ChatColor.stripColor(game))) {
                    Khaos.getInstance().getLogger().info("Game '" + game + "' exists.");
                    openInventory(p, Khaos.getInstance().getGame(ChatColor.stripColor(game)));
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler public void chooseGame(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (Khaos.getInstance().isInGame(p)) return;
            String gameName = ChatColor.stripColor(e.getInventory().getName());
            if (Khaos.getInstance().gameExists(gameName)) {
                e.setCancelled(true);
                String[] parts = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).split(" ");
                int id = Integer.parseInt(parts[parts.length - 1]);
                Arena a = Khaos.getInstance().getGame(gameName).getArena(id);
                if (a.getState() == ArenaState.POST_GAME) {
                    // TODO notify player that the arena is restarting
                }
                else a.addPlayer(p);
            }
        }
    }

    public static void openInventory(Player p, Game g) {
        // Khaos.getInstance().getLogger().info(g.getName() + "(" + p.getName()
        // + "): ");

        Inventory i = Bukkit.createInventory(p, (g.getArenas().size() % 9) * 9,
                ChatColor.RED.toString() + ChatColor.BOLD + g.getName());
        Khaos.getInstance().getLogger().info(g.getName() + "(" + p.getName() + "): Inventory=" + i.toString());
        int index = 0;

        for (Arena a : g.getArenas()) {
            Khaos.getInstance().getLogger()
                    .info(g.getName() + "(" + p.getName() + "): Inventory: Arena loop: Arena=" + a.toString());
            byte color = a.getState() == ArenaState.PRE_GAME ? (byte) 5 : (byte) 14;
            @SuppressWarnings("deprecation") ItemStack wool = new ItemStack(Material.WOOL, 1, (short) 0, color);
            ItemMeta woolMeta = wool.getItemMeta();
            woolMeta.setDisplayName(ChatColor.GREEN + g.getName() + " " + a.getId());

            List<String> s = new ArrayList<String>();
            s.add(a.getState().toString());
            s.add(ChatColor.GOLD + "Players: " + ChatColor.LIGHT_PURPLE + a.getPlayers().size());
            if (a.getState() == ArenaState.IN_GAME)
                s.add(ChatColor.LIGHT_PURPLE + "Map: " + ChatColor.YELLOW + a.getCurrentMap().getName()
                        + ChatColor.LIGHT_PURPLE + " by: " + ChatColor.YELLOW + a.getCurrentMap().getCreator());
            woolMeta.setLore(s);
            wool.setItemMeta(woolMeta);
            i.setItem(index, wool);
            index++;
        }
        p.openInventory(i);
    }

}
