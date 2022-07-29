package me.hybridplague.olympicsarchery;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class ArcheryListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (JoinCommand.participants.contains(p)) {
			JoinCommand.participants.remove(p);
			p.spigot().respawn();
			p.setMaxHealth(20);
			p.setHealth(20);
			double x = 117.500;
			double y = 78.0;
			double z = 1025.500;
			float yaw = 180f;
			float pitch = 0f;
			
			Location loc = new Location(p.getWorld(), x, y, z);
			loc.setYaw(yaw);
			loc.setPitch(pitch);
			p.teleport(loc);
			
			Collection<Entity> nearbyEntities = p.getWorld().getNearbyEntities(p.getLocation(), 50, 50, 50);
			for (Entity entity : nearbyEntities) {
				if (entity instanceof Player) {
					((Player) entity).sendMessage(ChatColor.RED + p.getName() + " has been eliminated from the round!");
				}
			}
		}
	}
	
	@EventHandler
	public void onRegen(EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (JoinCommand.participants.contains(p)) {
				e.setCancelled(true);
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (JoinCommand.participants.contains(e.getPlayer())) {
			JoinCommand.participants.remove(e.getPlayer());
			e.getPlayer().setMaxHealth(20D);
			e.getPlayer().setHealth(20D);
		}
	}

	@EventHandler
	public void arrowShoot(EntityShootBowEvent e) {
		ItemStack bow = e.getBow();
		if (bow.hasItemMeta() && bow.getItemMeta().getDisplayName().equals(ChatColor.RED + "Olympics Bow")) {
			Arrow a = (Arrow) e.getProjectile();
			if (!JoinCommand.participants.contains((Player) a.getShooter())) {
				e.setCancelled(true);
				((Player)a.getShooter()).sendMessage("You cannot shoot this bow since you are not participating in the event!");
				return;
			}
		}
	}
	
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		Entity damagee = e.getEntity();
		Entity damager = e.getDamager();
		
		if (damagee instanceof Player) {
			/*
			 * harmed entity is a player
			 */
			if (JoinCommand.participants.contains(((Player) damagee))) {
				/*
				 * harmed player is apart of the event
				 */
				if (damager instanceof Projectile) {
					/*
					 * damage caused by a projectile
					 */
					Projectile projectile = (Projectile) e.getDamager();
					Object shooter = projectile.getShooter();
					
					if ((shooter instanceof Player) && (projectile instanceof Arrow)) {
						/*
						 * shooter is a player, not a mob
						 * projectile is an arrow
						 * 
						 */
						Arrow a = (Arrow) projectile;
						Player s = (Player) a.getShooter();
						Player d = (Player) damagee;
						
						if (JoinCommand.participants.contains(d)) {
							if (!JoinCommand.participants.contains(s)) {
								s.sendMessage(ChatColor.RED + "You are not participating in this event!");
								e.setCancelled(true);
								return;
							}
						}
						
						if (JoinCommand.participants.contains(s)) {
							if (!JoinCommand.participants.contains(d)) {
								s.sendMessage(ChatColor.RED + "Only shoot at your opponents!");
								e.setCancelled(true);
								return;
							}
						}
						
						if (a.getCustomName() == null) return;
						if (a.getCustomName().equals(ChatColor.RED + "Archery Arrow")) {
							e.setCancelled(true);
							if (s == d) {
								a.remove();
								return;
							}
							d.damage(2D);
							if (Math.round(d.getHealth()) == 2) {
								s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + d.getName() + "'s Health: &f1 &4♥"));
								return;
							} else {
								s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have slain " + d.getName()));
								d.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou were slain by " + s.getName() + " and are now spectating!"));
							}
						} else {
							e.setCancelled(true);
							s.sendMessage(ChatColor.RED + "You must use the Olympics Bow!");
							return;
						}
					}
				} else {
					/*
					 * damage not caused by a projectile
					 */
					e.setCancelled(true);
					if (damager instanceof Player) {
						((Player) damager).sendMessage(ChatColor.RED + "You must use the Olympics Bow!");
					}
				}
				
			}
			/*
			 * damagee is not apart of event
			 */
			if (damager instanceof Player) {
				if (JoinCommand.participants.contains((Player) damager)) {
					/*
					 * attacker is in the event
					 */
					e.setCancelled(true);
					((Player) damager).sendMessage("You may only attack your opponents!");
					return;
				}
			}
			
		}
		return;
	}
	
}
