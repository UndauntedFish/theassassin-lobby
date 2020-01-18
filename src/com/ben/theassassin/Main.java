package com.ben.theassassin;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.ben.theassassin.bossbarhud.HUD;
import com.ben.theassassin.bossbarhud.PlayerJoinListener;
import com.ben.theassassin.database.Database;

public class Main extends JavaPlugin
{
    public Player assassin, runaway;
    public HUD lobbyHud;

    @Override
    public void onEnable()
    {
        System.out.println("[TheAssassin] Successfully enabled.");
        loadConfig();
        
        // Commands
        getCommand("menu").setExecutor(new MenuCommand(this));

        // Event Handlers
        Bukkit.getPluginManager().registerEvents(new MenuListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        
        // Lobby HUD Timer creation
        lobbyHud = new HUD(
        		ChatColor.WHITE + "Welcome to " + ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.RED + "THE ASSASSIN",
				BarColor.GREEN,
				BarStyle.SOLID,
				this);
        
        lobbyHud.startTimer(this.getConfig().getDouble("time"));
        
        // MySQL Database creation
        Database myDtb = new Database(this);
        
    }
    
    private void loadConfig()
    {
    	this.getConfig().options().copyDefaults();
		saveDefaultConfig();
    }

    @Override
    public void onDisable()
    {
        /*
         * If there are still players' inventories stored when server shuts down,
         * restore those player's inventories before they get kicked
         */
        if (!tempInv.isEmpty())
        {
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            for (Player player : onlinePlayers)
            {
                if (tempInv.containsKey(player))
                {
                    player.getInventory().setContents(tempInv.get(player));
                }
            }
        }

        System.out.println("[TheAssassin] Successfully disabled.");
    }

    public HashMap<Player, ItemStack[]> tempInv = new HashMap<>();

    private boolean isAssassin(Player player)
    {
        if (player.equals(assassin))
        {
            return true;
        }
        return false;
    }

    private boolean isRunaway(Player player)
    {
        if (player.equals(runaway))
        {
            return true;
        }
        return false;
    }

    /* ASSASSIN/RUNAWAY SELECTION GUI */
    public void applySelectionGUI(Player player)
    {
        // BEGINNING
        Inventory gui = Bukkit.createInventory(null, 27, "The Assassin: Main Menu");


        // LORES
        List<String> disableAssassin = new ArrayList<>();
        disableAssassin.add("Click here again to quit.");

        List<String> enableAssassin = new ArrayList<>();
        enableAssassin.add("Play as the Assassin!");

        List<String> disableRunaway = new ArrayList<>();
        disableRunaway.add("Click here again to quit.");

        List<String> enableRunaway = new ArrayList<>();
        enableRunaway.add("Play as the Runaway!");


        // ITEMSTACKS
        ItemStack toggleAssassin = null, toggleRunaway = null;
        ItemMeta toggleAssassinMeta = null, toggleRunawayMeta = null;

        // Default State: Assassin slot is set to gsword
        toggleAssassin = new ItemStack(Material.GOLDEN_SWORD);
        toggleAssassinMeta = toggleAssassin.getItemMeta();

        toggleAssassinMeta.setDisplayName("The Assassin");
        toggleAssassinMeta.setLore(enableAssassin);

        // Default State: Runaway slot is set to leather boots
        toggleRunaway = new ItemStack(Material.LEATHER_BOOTS);
        toggleRunawayMeta = toggleRunaway.getItemMeta();

        toggleRunawayMeta.setDisplayName("The Runaway");
        toggleRunawayMeta.setLore(enableRunaway);

        // Toggling Mechanism
        if (isAssassin(player))
        {
            // Player is already assassin when they open the menu, this state must reflect that.

            // Assassin slot is set to slimeblock
            toggleAssassin = new ItemStack(Material.SLIME_BALL);
            toggleAssassinMeta = toggleAssassin.getItemMeta();

            toggleAssassinMeta.setDisplayName(ChatColor.GREEN + "You are THE ASSASSIN");
            toggleAssassinMeta.setLore(disableAssassin);

            // Runaway slot is set to leather boots
            toggleRunaway = new ItemStack(Material.LEATHER_BOOTS);
            toggleRunawayMeta = toggleRunaway.getItemMeta();

            toggleRunawayMeta.setDisplayName("The Runaway");
            toggleRunawayMeta.setLore(enableRunaway);
        }
        else if (isRunaway(player))
        {
            // Player is already runaway when they open the menu, this state must reflect that.

            // Assassin slot is set to gsword
            toggleAssassin = new ItemStack(Material.GOLDEN_SWORD);
            toggleAssassinMeta = toggleAssassin.getItemMeta();

            toggleAssassinMeta.setDisplayName("The Assassin");
            toggleAssassinMeta.setLore(enableAssassin);

            // Runaway slot is set to slimeblock
            toggleRunaway = new ItemStack(Material.SLIME_BALL);
            toggleRunawayMeta = toggleRunaway.getItemMeta();

            toggleRunawayMeta.setDisplayName(ChatColor.GREEN + "You are THE RUNAWAY");
            toggleRunawayMeta.setLore(disableRunaway);
        }
        else
        {
            // Player is neither assassin nor runaway when they open the menu, show default state

            // Assassin is set to gsword
            toggleAssassin = new ItemStack(Material.GOLDEN_SWORD);
            toggleAssassinMeta = toggleAssassin.getItemMeta();

            toggleAssassinMeta.setDisplayName("The Assassin");
            toggleAssassinMeta.setLore(enableAssassin);

            // Runaway slot is set to leather boots
            toggleRunaway = new ItemStack(Material.LEATHER_BOOTS);
            toggleRunawayMeta = toggleRunaway.getItemMeta();

            toggleRunawayMeta.setDisplayName("The Runaway");
            toggleRunawayMeta.setLore(enableRunaway);
        }
        toggleAssassin.setItemMeta(toggleAssassinMeta);
        toggleRunaway.setItemMeta(toggleRunawayMeta);


        // ITEM SETTING
        gui.setItem(11, toggleAssassin);
        gui.setItem(15, toggleRunaway);


        // FINAL
        player.openInventory(gui);
    }
}
