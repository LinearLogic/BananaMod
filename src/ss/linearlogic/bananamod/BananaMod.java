package ss.linearlogic.bananamod;

import ss.linearlogic.bananamod.BMLogger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BananaMod extends JavaPlugin
{
	public void onEnable()
	{
//		BMLogger.logInfo("Enabling command handler...");
		
		BMLogger.logInfo("Activating player listener...");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new BMListener(this), this);
		
		BMLogger.logInfo("Plugin was successfully enabled!");
	}
	
	public void onDisable()
	{
		BMLogger.logInfo("Disabling " + getDescription().getName() + " version " + getDescription().getVersion());
		BMLogger.logInfo("Plugin was successfully disabled!");
	}
}