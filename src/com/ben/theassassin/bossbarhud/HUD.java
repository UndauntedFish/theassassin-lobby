package com.ben.theassassin.bossbarhud;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import com.ben.theassassin.Main;

public class HUD
{
	private BossBar bossbar;
	private String title;
	private BarColor barColor;
	private BarStyle barStyle;
	private BarFlag barFlag;
	private Main main;
	
	public HUD(Main main)
	{
		setBossbar(null);
		this.main = main;
	}
	
	public HUD(String title, BarColor barColor, BarStyle barStyle, BarFlag barFlag, Main main)
	{
		this.setTitle(title);
		this.setBarColor(barColor);
		this.setBarStyle(barStyle);
		this.setBarFlag(barFlag);
		this.main = main;
		
		setBossbar(Bukkit.createBossBar(title, barColor, barStyle, barFlag));
	}
	
	public HUD(String title, BarColor barColor, BarStyle barStyle, Main main)
	{
		this.setTitle(title);
		this.setBarColor(barColor);
		this.setBarStyle(barStyle);
		this.main = main;
		
		setBossbar(Bukkit.createBossBar(title, barColor, barStyle));
	}
	
	public BossBar getBossbar()
	{
		return bossbar;
	}

	public void setBossbar(BossBar bossbar)
	{
		this.bossbar = bossbar;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public BarColor getBarColor()
	{
		return barColor;
	}

	public void setBarColor(BarColor barColor)
	{
		this.barColor = barColor;
	}

	public BarStyle getBarStyle()
	{
		return barStyle;
	}

	public void setBarStyle(BarStyle barStyle)
	{
		this.barStyle = barStyle;
	}

	public BarFlag getBarFlag()
	{
		return barFlag;
	}

	public void setBarFlag(BarFlag barFlag)
	{
		this.barFlag = barFlag;
	}

	// Starts a 1 minute progress bar timer to the HUD
	public void startTimer(double time)
	{
		AtomicInteger currentIteration = new AtomicInteger(1);
		AtomicInteger processId = new AtomicInteger();
		int maxIterations = (int) time - 1; // 59 for 60s, 29 for 30s, etc.
		bossbar.setProgress(1.0 / time); // 1.0/60.0 for 60s, 1.0/30.0 for 30s, etc.
		
		int counter = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() 
		{
			public void run()
			{
				try
				{
					// Increments the progress bar each second
					if (currentIteration.get() != 0)
					{
						int timeLeft = ((int) time - (currentIteration.get() + 1));
						if (timeLeft == 1)
						{
							bossbar.setTitle(timeLeft + " second until the game starts!");
						}
						else if (timeLeft < (int) time && timeLeft > 1)
						{
							bossbar.setTitle(timeLeft + " seconds until the game starts!");
						}
						else
						{
							bossbar.setTitle(ChatColor.GREEN + "Starting game...");
							// call helper function to send players to database and the gameserver
						}
						
						//+ (1.0/60.0) is 60 second timer, + (1.0/30.0) is 30 second timer, etc.
						bossbar.setProgress(bossbar.getProgress() + (1.0 / time));
						
						/* 
						 * Sends to console the value of bossbar progress for each iteration. Used for debugging.
						 * 
						 * Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "" + bossbar.getProgress() 
						 * + ChatColor.RESET + "Task IT #" + currentIteration.get());
						 */
					}
				} catch (IllegalArgumentException e)
				{
					// Program will be here if the bossbar's progress is set to a value > 1.0.
					// We will cancel that attempt to set it to > 1.0, and manually set it to 1.0
					// call helper function to send players to database and the gameserver
					bossbar.setProgress(1.0);
					Bukkit.getScheduler().cancelTask(processId.get());
				}
				
				
				// Standard incrementing and stopping
				int currentIt = currentIteration.incrementAndGet();
				if (currentIt > maxIterations)
				{
					Bukkit.getScheduler().cancelTask(processId.get());
				}
			}
		}, 0L, 20L); // runnable repeats code every 1 second (since 1 sec = 20 ticks, hence 20L)
		
		processId.set(counter);
	}
}
