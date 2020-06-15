package me.rvt.rcshopaddons;

import me.rvt.rcshopaddons.config.ConfigInit;
import me.rvt.rcshopaddons.items.Books;
import me.rvt.rcshopaddons.items.Key;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class RCshopAddons extends JavaPlugin implements Listener {
    FileConfiguration config;
    Books books;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        config = new ConfigInit(this).getConfig();
    }

    @EventHandler
    private void playerSignInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(e.getClickedBlock().getType().toString().contains("SIGN")){

                new Key(e.getClickedBlock(), e.getPlayer(), config);

                if(books != null) {
                    books.getSign(e.getPlayer(), e.getClickedBlock());
                }
                else {
                    books = new Books(config);

                    books.getSign(e.getPlayer(), e.getClickedBlock());

                    getServer().getScheduler().scheduleSyncDelayedTask(
                            this, this::destroyObject,
                            config.getInt("var.destroyObject") * 20);
                }
            }
        }
    }

    void destroyObject() { books.clearSigns(); books = null; }
}
