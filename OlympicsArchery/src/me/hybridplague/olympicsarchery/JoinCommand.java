package me.hybridplague.olympicsarchery;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class JoinCommand implements CommandExecutor {

	public static List<Player> participants = new ArrayList<Player>();
	
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		Player p = (Player) sender;
		
		if (!p.hasPermission("olympics.admin")) return false;
		if (args.length == 0) {
			p.sendMessage("joinarchery <player | list | endcontest>");
			return true;
		} else {
			
			if (args[0].equalsIgnoreCase("list")) {
				List<String> names = new ArrayList<String>();
				for (Player players : participants) {
					names.add(ChatColor.GREEN + players.getName());
				}
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.join("&7, &a", names)));
				return true;
			}
			
			if (args[0].equalsIgnoreCase("endcontest")) {
				try {
					for (Player t : participants) {
						t.setMaxHealth(20D);
						t.setHealth(20D);
					}
					participants.clear();
				} catch (ConcurrentModificationException ex) {
					
				}
				p.sendMessage("All contestants have been cleared and health restored! Don't forget to return their items.");
				return true;
			}
			
			if (!Bukkit.getOfflinePlayer(args[0]).isOnline()) {
				p.sendMessage("Player is not online.");
				return true;
			} else {
				Player op = Bukkit.getPlayer(args[0]);
				op.sendMessage("You were entered into the Olympics Archery contest! Your health was set to 2 hearts and you will receive your equipment by the host soon!");
				p.sendMessage("Your entered " + op.getName() + " into the archery contest! Don't forget to give them their equipment with /archeryequipment");
				op.setMaxHealth(4D);
				participants.add(op);
			}
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static void endGame() {
		for (Player t : participants) {
			t.setMaxHealth(20D);
			t.setHealth(20D);
			participants.remove(t);
		}
		participants.clear();
	}
	
}
