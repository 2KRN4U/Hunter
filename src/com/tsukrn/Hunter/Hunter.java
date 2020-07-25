package com.tsukrn.Hunter;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.StructureType;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Hunter extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
    	PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(this, this);

    }
    
    @Override
    public void onDisable() {

    }
    
    String trackName = "";
    Location loc;
    Location netherLoc;
    Location fortress;
    Boolean clone = false;
    CompassMeta compNormal;
    
    
    @EventHandler
	public void onPlayerUse(PlayerInteractEvent event) {
		Player p = (Player) this.getServer().getPlayer(trackName);
    	Player h = event.getPlayer();

    	if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand().equals(EquipmentSlot.HAND))) {
    		if(h.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
    			if(trackName.equals("")) {
    				h.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "Set a player to track using /track playername");
    			} else if ( (h.getPlayer().getName().equals(p.getName())) && ((h.getWorld().getEnvironment().equals(World.Environment.NORMAL)) || (h.getWorld().getEnvironment().equals(World.Environment.THE_END))) ){
    				CompassMeta compass = (CompassMeta) h.getInventory().getItemInMainHand().getItemMeta();
    				if(compass.hasLodestone()) {
    					h.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "No structure to track.");
    				} else {
    					h.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "No structure to track. (pointing to world spawn)");
    				}
    			} else if( (h.getPlayer().getName().equals(p.getName())) && (h.getWorld().getEnvironment().equals(World.Environment.NETHER)) ) {
    				ItemStack item = h.getInventory().getItemInMainHand();
    				CompassMeta compass = (CompassMeta) h.getInventory().getItemInMainHand().getItemMeta();
    				if(compass.hasLodestone()) {
    					fortress.getBlock().setType(Material.AIR);
    				}
    				fortress = h.getWorld().locateNearestStructure(h.getLocation(), StructureType.NETHER_FORTRESS, 2000, false);
    				fortress.setY(250);
    				fortress.getBlock().setType(Material.LODESTONE);
    				compass.setLodestone(fortress);
    				compass.setLodestoneTracked(true);
    				item.setItemMeta(compass);
    				h.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Now tracking: Nether Fortress");
    			} else if((p.getWorld().getEnvironment().equals(World.Environment.NORMAL) && (h.getWorld().getEnvironment().equals(World.Environment.NORMAL)))) {

    				
    				ItemStack item = h.getInventory().getItemInMainHand();
    				CompassMeta compass = (CompassMeta) h.getInventory().getItemInMainHand().getItemMeta();
    				
    				if(!clone) {
    					compNormal = (CompassMeta) h.getInventory().getItemInMainHand().getItemMeta();
    					clone = true;
    				}
    				
    				if(compass.hasLodestone()) {
    					item.setItemMeta(compNormal);
    				}
    				
    				h.setCompassTarget(p.getLocation());
    				loc = p.getLocation();
    				h.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Now tracking: " + p.getName());
    			} else if((p.getWorld().getEnvironment().equals(World.Environment.NETHER)) && (h.getWorld().getEnvironment().equals(World.Environment.NETHER))) {
    				ItemStack item = h.getInventory().getItemInMainHand();
    				CompassMeta compass = (CompassMeta) h.getInventory().getItemInMainHand().getItemMeta();
    				if(compass.hasLodestone()) {
    					netherLoc.getBlock().setType(Material.AIR);
    				}
    				netherLoc = p.getLocation();
    				netherLoc.setY(250);
    				netherLoc.getBlock().setType(Material.LODESTONE);
    				compass.setLodestone(netherLoc);
    				compass.setLodestoneTracked(true);
    				item.setItemMeta(compass);
    				h.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Now tracking: " + p.getName());
    			} else {
    				h.setCompassTarget(loc);
    				h.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "No players to track.");
    			}
    		}
    	}
    	
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    	Player p = (Player) event.getPlayer();
    	
    	ItemStack item = new ItemStack(Material.COMPASS);
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
    	item.setItemMeta(meta);
    	p.getInventory().addItem(item);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("track")) {
        	trackName = args[0];
        	Player p = (Player) this.getServer().getPlayer(trackName);
        	loc = p.getLocation();
        	sender.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + trackName + " will now be tracked. Right click compass to track.");
        }
        return false;
    }

}
