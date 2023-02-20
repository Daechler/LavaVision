package net.daechler.lavavision;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LavaVision extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("LavaVision enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("LavaVision disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("lavavision")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("lavavision.use")) {
                    player.sendMessage("LavaVision enabled");
                    for (int x = -30; x <= 30; x++) {
                        for (int y = -30; y <= 30; y++) {
                            for (int z = -30; z <= 30; z++) {
                                if (player.getLocation().getBlock().getRelative(x, y, z).getType() == Material.LAVA) {
                                    player.sendBlockChange(player.getLocation().getBlock().getRelative(x, y, z).getLocation(), Material.AIR.createBlockData());
                                }
                            }
                        }
                    }
                    return true;
                } else {
                    player.sendMessage("You don't have permission to use this command.");
                    return true;
                }
            } else {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
        }
        return false;
    }
}