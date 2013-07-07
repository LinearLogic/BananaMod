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

public class BMListener implements Listener {

	private HashMap<String, Boolean> blockMsgsForPlayers = new HashMap<String, Boolean>();
	private ArrayList<Location> peelLocations = new ArrayList<Location>();
	private final String prefix = ChatColor.GRAY + "[" + ChatColor.YELLOW + "Banana" + ChatColor.GOLD + "Mod" +
			ChatColor.GRAY + "] ";

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) { //placing the banana peel
		Player player = event.getPlayer();
		if (event.getBlockPlaced().getType().equals(Material.YELLOW_FLOWER)) {
			if (!player.hasPermission("bananamod.place")) {
				player.sendMessage(prefix + ChatColor.RED + "Error: " + ChatColor.GRAY + "You don't have permission to place banana peels!");
				event.setCancelled(true);
				return;
			}
			peelLocations.add(event.getBlockPlaced().getLocation());
			player.sendMessage(prefix + "You have placed a banana peel!");
			return;
		}
	}

	@EventHandler
	public void onBlockDestroy(BlockBreakEvent event) { //what to do if a player destroys a banana peel, or the block below one
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Block blockAbove= block.getRelative(BlockFace.UP);
		if (peelLocations.contains(block.getLocation())) {
			if ((!player.hasPermission("bananamod.destroy")) && (!player.hasPermission("bananamod.remove"))) {
				player.sendMessage(prefix + ChatColor.RED + "Error: " + ChatColor.GRAY + "You don't have permission to pick up banana peels!");
				event.setCancelled(true);
				return;
			}
			peelLocations.remove(block.getLocation());
			player.sendMessage(prefix + ChatColor.GRAY + "You have picked up a banana peel, use it wisely!");
		} else if (peelLocations.contains(blockAbove.getLocation())) {
			if ((!player.hasPermission("bananamod.destroy")) && (!player.hasPermission("bananamod.remove"))) {
				player.sendMessage(prefix + ChatColor.RED + "Error: " + ChatColor.GRAY + "You don't have permission to pick up banana peels!");
				event.setCancelled(true);
				return;
			}
			peelLocations.remove(blockAbove.getLocation());
			player.sendMessage(prefix + "You have picked up a banana peel, use it wisely!");
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) { // prevents immunity spam if login location is on a banana peel
		blockMsgsForPlayers.put(event.getPlayer().getName(), true);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) { //slipping on a banana peel
		Player player = event.getPlayer();
		Block block = player.getLocation().getBlock();
		if (peelLocations.contains(block.getLocation())) {
			if (player.hasPermission("bananamod.immune"))
				return;
			Boolean blocked = blockMsgsForPlayers.get(player.getName());
			blockMsgsForPlayers.put(player.getName(), false);
			if (blocked != null && blocked) {
				blockMsgsForPlayers.put(player.getName(), false);
				return;
			}
			player.damage(6.0);
			player.sendMessage(prefix + ChatColor.RED + "Ouch! " + ChatColor.GRAY + "You slipped on a banana peel!");
			block.setTypeId(0);
			block.setData((byte) 0);
			peelLocations.remove(block.getLocation());
		}
	}
}
