package me.hybridplague.olympicsarchery;

import org.bukkit.plugin.java.JavaPlugin;

public class OlympicsArchery extends JavaPlugin {

	@Override
	public void onEnable() {
		this.getCommand("archeryequipment").setExecutor(new ArcheryCommand());
		this.getCommand("joinarchery").setExecutor(new JoinCommand());
		this.getServer().getPluginManager().registerEvents(new ArcheryListener(), this);
	}
	
	@Override
	public void onDisable() {
		JoinCommand.endGame();
	}
	
	
	
	
}
