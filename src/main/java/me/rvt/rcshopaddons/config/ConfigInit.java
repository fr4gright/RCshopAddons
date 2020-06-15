package me.rvt.rcshopaddons.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigInit {
    private FileConfiguration config;

    public ConfigInit(Plugin plugin) {
        loadConfig(plugin);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void loadConfig(Plugin plugin) {
        File conf = new File(plugin.getDataFolder(), "config.yml");

        config = YamlConfiguration.loadConfiguration(conf);

        if (!config.contains("signs")) {

            init();

            try {
                config.save(conf);
            } catch (IOException var3) {
                System.out.println("[RCshopAddons] Unable to save config!");
            }
        }
    }

    private void init() {
        Location type = new Location(Bukkit.getWorld("world"), 72,75,75);
        Location number = new Location(Bukkit.getWorld("world"), 72,75,76);
        Location buy = new Location(Bukkit.getWorld("world"), 72,75,77);
        Location key = new Location(Bukkit.getWorld("world"), 56,67,75);

        String[] prices = {
        "default:3000", "loyalty:10000", "strider:8000", "luck:7000",
        "affinity:5000", "blast:2500", "channeling:5000", "falling:3000",
        "aspect:10000", "flame:10000", "frost:5000", "impaling:4000",
        "infinity:10000", "knockback:4000", "looting:5000", "lure:8000",
        "multishot:5000", "power:3500", "projectile:3500", "punch:4000",
        "quick:3000", "respiration:5000", "riptide:7000", "smite:3000",
        "edge:5000", "thorns:4000", "unbreaking:5000", "fortune:10000",
        "silk:30000", "mending:50000"
        };

        List<String> idleMsg = new ArrayList<>();

        idleMsg.add(ChatColor.GRAY + "Click to");
        idleMsg.add(ChatColor.GRAY + "Select");
        idleMsg.add(ChatColor.GRAY + "a Book");

        config.set("signs.type", type);
        config.set("signs.number", number);
        config.set("signs.buy", buy);
        config.set("signs.key", key);
        config.set("prices", Arrays.asList(prices));

        config.set("messages.idle", idleMsg);
        config.set("messages.prefix", ChatColor.WHITE + "" + ChatColor.BOLD + "[" +
                ChatColor.AQUA + ChatColor.BOLD + "RC" + ChatColor.WHITE + ChatColor.BOLD + "][" +
                ChatColor.GOLD + ChatColor.GREEN + "Shop" + ChatColor.WHITE + ChatColor.BOLD + "]" +
                ChatColor.RESET + " ");
        config.set("messages.nofunds", ChatColor.RED + "Insufficient funds!");

        config.set("var.destroyObject", 180);
    }
}