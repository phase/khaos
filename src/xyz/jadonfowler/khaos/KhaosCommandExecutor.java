package xyz.jadonfowler.khaos;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KhaosCommandExecutor implements CommandExecutor {

    @Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if (sender instanceof Player) {
                    if (args.length == 3) {
                        String game = args[1];
                        String mobName = args[2].replace(' ', '_').toUpperCase();
                        EntityType type = null;
                        try {
                            type = EntityType.valueOf(mobName);
                        }
                        catch (IllegalArgumentException exp) {
                            sender.sendMessage("That is not a mob.");
                        }
                        if (type != null) {
                            Player p = (Player) sender;
                            Entity n = p.getWorld().spawnEntity(p.getLocation(), type);
                            
                            n.setCustomName(ChatColor.BOLD + game);
                            n.setCustomNameVisible(true);
                            n.setMetadata("khaos-game", new FixedMetadataValue(Khaos.getInstance(), game));
                            
                            Khaos.getInstance().getLogger()
                                    .info("Entity '" + mobName + "' created for game '" + game + "'.");
                            if (n instanceof LivingEntity) {
                                ((LivingEntity) n)
                                        .addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000, 100000));
                            }
                            return true;
                        }
                    }
                    else {
                        sender.sendMessage("/khaos create <game> <entity>");
                    }
                }
                else {
                    sender.sendMessage("You need to be a player to create entities.");
                }
            }
            return false;
        }
        else {
            // TODO Display Help message for Ops
            sender.sendMessage("TODO Display Help message for Ops");
            return false;
        }
    }

}
