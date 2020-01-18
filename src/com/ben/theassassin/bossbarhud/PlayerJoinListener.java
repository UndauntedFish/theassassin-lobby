package com.ben.theassassin.bossbarhud;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.ben.theassassin.Main;

public class PlayerJoinListener implements Listener
{
	private Main main;
	
	public PlayerJoinListener(Main main)
	{
		this.main = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		main.lobbyHud.getBossbar().addPlayer(e.getPlayer());
	}
}
