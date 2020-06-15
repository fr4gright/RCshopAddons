package me.rvt.rcshopaddons.items;

import com.earth2me.essentials.Essentials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.math.BigDecimal;
import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class Books {
    Player pl;
    Block sign;
    FileConfiguration config;
    int numId, currentPrice;
    Sign typeSign, numberSign, buySign;
    List<Enchantment> allEnchants = new ArrayList<>();
    Enchantment selected;
    String[] romanNums = {"I", "II", "III", "IV", "V"};
    HashMap<String, Integer> prices = new HashMap<>();

    Essentials ess = (Essentials) getServer().getPluginManager().getPlugin("Essentials");

    public Books(FileConfiguration config){
        this.config = config;

        typeSign = (Sign) config.getLocation("signs.type").getBlock().getState();
        numberSign = (Sign) config.getLocation("signs.number").getBlock().getState();
        buySign = (Sign) config.getLocation("signs.buy").getBlock().getState();

        for(String s: config.getStringList("prices")){
            String[] temp = s.split(":");

            prices.put(temp[0], Integer.parseInt(temp[1]));
        }
    }

    public void getSign(Player pl, Block sign){
        this.pl = pl;
        this.sign = sign;

        checkSign();
    }

    void checkSign(){
        if(sign.equals(config.getLocation("signs.type").getBlock()))
        {
            setTypeSign();
            setNumberSign(false);
        }
        else if(sign.equals(config.getLocation("signs.number").getBlock()))
        {
            if(selected != null)
                setNumberSign(true);
        }
        else if(sign.equals(config.getLocation("signs.buy").getBlock()))
        {
            if(selected != null)
                setBuySign(true);
        }
    }

    private void setBuySign(boolean buy){
        if(!buy){
            currentPrice = prices.get("default") * numId;

            for (Map.Entry<String, Integer> p : prices.entrySet()) {
                if(selected.toString().contains(p.getKey())){
                    currentPrice = p.getValue() * numId;
                    break;
                }
            }

            buySign.setLine(1, ChatColor.BLUE + "" + ChatColor.BOLD + "BUY");
            buySign.setLine(2, ChatColor.GRAY + "$"+ ChatColor.GREEN
                    + ChatColor.GREEN + currentPrice);
            buySign.update();
        }
        else{
            long balance = ess.getUser(pl).getMoney().longValue();

            if(balance >= currentPrice){
                ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) book.getItemMeta();
                
                bookMeta.addStoredEnchant(selected, numId, false);
                book.setItemMeta(bookMeta);

                pl.getInventory().addItem(book);
                ess.getUser(pl).takeMoney(BigDecimal.valueOf(currentPrice));
            }
            else{
                pl.sendMessage(config.getString("messages.prefix") + config.getString("messages.nofunds"));
            }
        }
    }

    private void setTypeSign(){
        String temp;

        if(allEnchants.isEmpty())
            allEnchants.addAll(Arrays.asList(Enchantment.values()));

        for(int i = 0; i < 4; i++){

            try{
                if(i < 1)
                    temp = ChatColor.GREEN + "" + ChatColor.BOLD + formatEnchants(allEnchants.get(i).toString());
                else
                    temp = ChatColor.GRAY + formatEnchants(allEnchants.get(i).toString());
                }
            catch (IndexOutOfBoundsException e){
                temp = " ";
            }

            typeSign.setLine(i, temp);
        }
        
        selected = allEnchants.remove(0);
        typeSign.update();
    }

    void setNumberSign(boolean roll){
        int max = selected.getMaxLevel();
        String display = "";

        if(roll){
            numId++;
            if(numId > max)
                numId = 1;
        }
        else{
            numId = 1;
        }

        for(int i = 0; i < max; i++){
            if(i == numId-1)
                display += ChatColor.GREEN + "" + ChatColor.BOLD + romanNums[i] + " ";
            else
                display += ChatColor.GRAY + romanNums[i] + " ";
        }

        numberSign.setLine(1, display);
        numberSign.update();

        setBuySign(false);
    }

    private String formatEnchants(String enchant){
        String splitted = enchant.split(":")[1].split(",")[0];

        if(splitted.contains("bane") || splitted.contains("luck"))
            splitted = splitted.split("_")[0];

        return splitted.replaceAll("[\\[\\]]", "").replace("_", " ").toUpperCase();
    }

    public void clearSigns(){
        for(int i = 0; i < 4; i++){
            numberSign.setLine(i, "");
            buySign.setLine(i, "");
            typeSign.setLine(i, "");
        }

        List<String> idleMsg = config.getStringList("messages.idle");

        for(String s: idleMsg){
            typeSign.setLine(idleMsg.indexOf(s), s);
        }

        typeSign.update();
        numberSign.update();
        buySign.update();
    }
}
