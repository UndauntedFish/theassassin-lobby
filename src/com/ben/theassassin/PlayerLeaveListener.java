package com.ben.theassassin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener
{
    private static Main main;

    public PlayerLeaveListener(Main main)
    {
        PlayerLeaveListener.main = main;
    }

    private void restoreInv(Player player)
    {
        if (main.tempInv.containsKey(player))
        {
            player.getInventory().setContents(main.tempInv.get(player));
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e)
    {
        restoreInv(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        restoreInv(e.getPlayer());
    }
}
