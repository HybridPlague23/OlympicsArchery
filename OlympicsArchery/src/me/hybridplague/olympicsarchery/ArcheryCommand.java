package me.hybridplague.olympicsarchery;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArcheryCommand implements CommandExecutor {
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		Player p = (Player) sender;
		if (!p.hasPermission("olympics.admin")) return false;
		
		p.getInventory().addItem(getBow());
		p.getInventory().addItem(getArrow());
		
		
		return true;
	}
	
	public ItemStack getArrow() {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.RED + "Archery Arrow");
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBow() {
		ItemStack item = new ItemStack(Material.BOW);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.RED + "Olympics Bow");
		meta.setUnbreakable(true);
		meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		item.setItemMeta(meta);
		
		
		return item;
	}

}
