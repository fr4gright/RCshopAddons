package me.rvt.rcshopaddons.items;

import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

import static org.bukkit.Bukkit.getServer;

public class Key {
    FileConfiguration config;
    Player p;
    Essentials ess = (Essentials) getServer().getPluginManager().getPlugin("Essentials");

    public Key(Block sign, Player p, FileConfiguration config){
        this.p = p;
        this.config = config;

        if(sign.equals(config.getLocation("signs.key").getBlock())){
            Sign keySign = (Sign) config.getLocation("signs.key").getBlock().getState();

            Bukkit.dispatchCommand(
                    Bukkit.getServer().getConsoleSender(), "key " + p.getName() + " 1");

            ess.getUser(p).takeMoney(BigDecimal.valueOf(
                    Long.parseLong(keySign.getLine(3).replace("$", ""))));
        }
    }
}
