package ss.linearlogic.bananamod;

import org.bukkit.plugin.java.JavaPlugin;

public class BananaMod extends JavaPlugin {

	public void onEnable() {
		getLogger().info("Registering listener...");
		getServer().getPluginManager().registerEvents(new BMListener(), this);
		getLogger().info("Enabled!");
	}
	
	public void onDisable()	{
		getLogger().info("Disabled!");
	}
}
