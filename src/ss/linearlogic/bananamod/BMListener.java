package ss.linearlogic.bananamod;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BMListener implements Listener
{
	private ArrayList<Location> peelLocations = new ArrayList<Location>();
	private HashMap<String, Boolean> blockMsgsForPlayer = new HashMap<String, Boolean>();
	
	private BananaMod plugin;
	public BMListener(BananaMod instance) {
		this.plugin = instance;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) //placing the banana peel
	{
		Player player = event.getPlayer();
		if (event.getBlockPlaced().getType().equals(Material.YELLOW_FLOWER)) {
			if (!player.hasPermission("bananamod.place")) {
				player.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Banana" + ChatColor.GOLD + "Mod" + ChatColor.GRAY + "] " + ChatColor.RED + "Error: " + ChatColor.GRAY + "You don't have permission to place banana peels!");
				event.setCancelled(true);
				return;
			}
			peelLocations.add(event.getBlockPlaced().getLocation());
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Banana" + ChatColor.GOLD + "Mod" + ChatColor.GRAY + "] " + ChatColor.GRAY + "You have placed a banana peel!");
			return;
		}
	}
	
	@EventHandler
	public void onBlockDestroy(BlockBreakEvent event) //what to do if a player destroys a banana peel, or the block below one
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Block blockAbove= block.getRelative(BlockFace.UP);
		if (peelLocations.contains(block.getLocation())) {
			if ((!player.hasPermission("bananamod.destroy")) && (!player.hasPermission("bananamod.remove"))) {
				player.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Banana" + ChatColor.GOLD + "Mod" + ChatColor.GRAY + "] " + ChatColor.RED + "Error: " + ChatColor.GRAY + "You don't have permission to pick up banana peels!");
				event.setCancelled(true);
				return;
			}
			peelLocations.remove(block.getLocation());
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Banana" + ChatColor.GOLD + "Mod" + ChatColor.GRAY + "] " + ChatColor.GRAY + "You have picked up a banana peel, use it wisely!");
		}
		else if (peelLocations.contains(blockAbove.getLocation()))
		{
			if ((!player.hasPermission("bananamod.destroy")) && (!player.hasPermission("bananamod.remove"))) {
				player.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Banana" + ChatColor.GOLD + "Mod" + ChatColor.GRAY + "] " + ChatColor.RED + "Error: " + ChatColor.GRAY + "You don't have permission to pick up banana peels!");
				event.setCancelled(true);
				return;
			}
			peelLocations.remove(blockAbove.getLocation());
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Banana" + ChatColor.GOLD + "Mod" + ChatColor.GRAY + "] " + ChatColor.GRAY + "You have picked up a banana peel, use it wisely!");
		}
		return;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) //this helps with preventing immunity message spam
	{
		blockMsgsForPlayer.put(event.getPlayer().getName(), true);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) //slipping on the banana peel
	{
		Player player = event.getPlayer();
		final String name = player.getName();
		Block block = player.getLocation().getBlock();
		if (peelLocations.contains(block.getLocation())) {
			if (player.hasPermission("bananamod.immune")) {
				if (blockMsgsForPlayer.get(name)) {
					player.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Banana" + ChatColor.GOLD + "Mod" + ChatColor.GRAY + "] " + ChatColor.GRAY + "You just stepped on a banana peel, but your agility saved you from a fall.");
					blockMsgsForPlayer.put(name, false);
					
				}
				return;
			}
			player.damage(6);
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Banana" + ChatColor.GOLD + "Mod" + ChatColor.GRAY + "] " + ChatColor.RED + "Ouch! " + ChatColor.GRAY + "You slipped on a banana peel!");
			block.setTypeId(0);
			block.setData((byte) 0);
			peelLocations.remove(block.getLocation());
			return;	
		}
		if (!blockMsgsForPlayer.get(name)) {
			blockMsgsForPlayer.put(name, true);
		}
	}
	
}
