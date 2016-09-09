package it.sharkcraft.sharkclock;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SharkClock extends JavaPlugin {
	
	public static SharkClock plugin;

	@Override
	public void onEnable() {
		
		SharkClock.plugin = this;
		Config.config = getConfig();
		
		Config.init();
		Config.loadblocks();
		
		@SuppressWarnings("unused")
		Update up = new Update();
	}
	
	@Override
	public void onDisable() {
		
		Config.save();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		Player p = (Player) sender;
        
		if (cmd.getName().equalsIgnoreCase("shtime")){
        	
        	Commands.shtime(p);
        	
        } else if (cmd.getName().equalsIgnoreCase("shdate")){
        	
        	Commands.shdate(p);
        	
        } else if (cmd.getName().equalsIgnoreCase("timeupdate")){
        	
        	Commands.clockupdate();
        }
        
		return false;
	}
}
