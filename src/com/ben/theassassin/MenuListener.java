package com.ben.theassassin;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuListener implements Listener
{
    private Main main;

    public MenuListener(Main main)
    {
        this.main = main;
    }

    // Gives the assassin kit to a specific player, and stores their previous inventory
    private void equipAssassin(Player player)
    {
        Inventory i = player.getInventory();

        // LORES
        List<String> killSwordLore = new ArrayList<>();
        killSwordLore.add("Strike the Runaway just once,");
        killSwordLore.add("and he drops. Yippie kai yay marfaka!");

        List<String> trackerLore = new ArrayList<>();
        trackerLore.add("Due to recent advances in technology,");
        trackerLore.add("compasses can now stalk people.");


        // ITEMS
        ItemStack killSword = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta killSwordMeta = killSword.getItemMeta();
        killSwordMeta.setDisplayName(ChatColor.GOLD + "The Silent Whisper");
        killSwordMeta.setLore(killSwordLore);
        killSwordMeta.setUnbreakable(true);
        killSword.setItemMeta(killSwordMeta);

        ItemStack tracker = new ItemStack(Material.COMPASS);
        ItemMeta trackerMeta = tracker.getItemMeta();
        trackerMeta.setDisplayName("Runaway Tracker");
        trackerMeta.setLore(trackerLore);
        tracker.setItemMeta(trackerMeta);

        // SAVES PLAYER'S CURRENT INVENTORY if it isn't empty, then clears their inventory
        if (!i.equals(null))
        {
            main.tempInv.put(player, i.getContents());
            i.clear();
        }

        // POPULATES PLAYER'S INVENTORY WITH ASSASSIN KIT
        i.setItem(0, killSword);
        i.setItem(8, tracker);

        // FINAL
        main.assassin = player;
        player.sendMessage(ChatColor.GREEN + "You are now the Assassin!");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
    }

    // Gives the runaway kit to a specified player, and stores their previous inventory
    private void equipRunaway(Player player)
    {
        Inventory i = player.getInventory();

        // SAVE PREVIOUS INVENTORY if current inventory isn't empty, then clears inventory
        if (!i.equals(null))
        {
            main.tempInv.put(player, i.getContents());
            i.clear();
        }

        // FINAL
        main.runaway = player;
        player.sendMessage(ChatColor.GREEN + "You are now the Runaway!");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

        /*
         * The runaway just plays in survival mode, trying to outrun the assassin.
         * No special items needed here, just a plain empty inventory.
         */
    }

    // Dequips the Assassin/Runaway kits from a specified player, and restores their previous inventory
    private void dequip(Player player)
    {
        Inventory i = player.getInventory();

        if (main.tempInv.containsKey(player))
        {
            i.setContents(main.tempInv.get(player));

            if (main.assassin == player)
            {
                main.assassin = null;
            }
            else if (main.runaway == player)
            {
                main.runaway = null;
            }
        }
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent e)
    {
        Player player = (Player) e.getWhoClicked();

        // If opened GUI's title is equal to "The Assassin: Main Menu", don't let the player take any items from there
        if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals("The Assassin: Main Menu"))
        {
            if (e.getCurrentItem() != null)
            {
                e.setCancelled(true);
            }
            else
            {
            	// This makes sure that no error is thrown if the player selects an empty slot in the GUI
                return;
            }
           
            switch (e.getCurrentItem().getType())
            {
                case GOLDEN_SWORD:
                    if (main.runaway == player)
                    {
                        dequip(player);
                        equipAssassin(player);
                    } else
                    {
                        equipAssassin(player);
                    }
                    player.closeInventory();
                    break;
                case SLIME_BALL:
                    if (main.assassin == player)
                    {
                        player.sendMessage("You are no longer the Assassin");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                        dequip(player);
                    } else if (main.runaway == player)
                    {
                        player.sendMessage("You are no longer the Runaway");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                        dequip(player);
                    }
                    player.closeInventory();
                    break;
                case LEATHER_BOOTS:
                    if (main.assassin == player)
                    {
                        dequip(player);
                        equipRunaway(player);
                    } else
                    {
                        equipRunaway(player);
                    }
                    player.closeInventory();
                default:
                    return;
            }
        }
    }
}