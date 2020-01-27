package com.ben.theassassin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.ben.theassassin.Main;

public class Database
{
	private Connection connection;
	private String host, database, username, password;
	private int port;
	
	public Database(Main main)
	{
		host = main.getConfig().getString("host");
		port = main.getConfig().getInt("port");
		database = main.getConfig().getString("database");
		username = main.getConfig().getString("username");
		password = main.getConfig().getString("password");
		
		try 
		{
			openConnection();
			
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MySQL CONNECTED!");
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Opens a connection from the plugin to the database github test
	private void openConnection() throws SQLException
	{
		// If the connection is already open, then don't attempt to open it again
		if (connection != null && !connection.isClosed())
		{
			return;
		}
		
		connection = DriverManager.getConnection("jdbc:mysql://" + 
		this.host + ":" + this.port + "/" + this.database, this.username, this.password);
	}
	
	
}
