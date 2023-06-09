package net.daechler.lavavision;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

public class LavaVision extends JavaPlugin implements Listener {

    private final HashSet<UUID> playersUsingLavaVision = new HashSet<>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage("§aLavaVision has been enabled!");
        startVisionUpdateTask();
        // Register the event listeners
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage("§cLavaVision has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (command.getName().equalsIgnoreCase("lavavision")) {
            UUID playerUuid = ((Player) sender).getUniqueId();
            if (playersUsingLavaVision.contains(playerUuid)) {
                playersUsingLavaVision.remove(playerUuid);
            } else {
                playersUsingLavaVision.add(playerUuid);
            }
            return true;
        }

        return false;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // When a player quits, remove them from the playersUsingLavaVision set
        playersUsingLavaVision.remove(event.getPlayer().getUniqueId());
    }

    private void startVisionUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updatePlayerVision(player);
                }
            }
        }.runTaskTimer(this, 0, 1);
    }

    private void updatePlayerVision(Player player) {
        UUID playerUuid = player.getUniqueId();
        boolean playerHasLavaVision = playersUsingLavaVision.contains(playerUuid);

        Location location = player.getLocation();
        int radius = 8; // radius of 8 gives us a 16x16 area

        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Location blockLocation = new Location(location.getWorld(), x, y, z);
                    Material originalMaterial = blockLocation.getBlock().getType();

                    if (originalMaterial == Material.LAVA) {
                        Material newMaterial = playerHasLavaVision ? Material.AIR : Material.LAVA;
                        player.sendBlockChange(blockLocation, newMaterial.createBlockData());
                    }
                }
            }
        }
    }
}
