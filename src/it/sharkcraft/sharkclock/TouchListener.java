package it.sharkcraft.sharkclock;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TouchListener implements Listener {
	
	public static final int SIGNAL_BLOCK = 0x0;
	public static final int SIGNAL_POSITION = 0x1;
	
	public static boolean active = false;
	public static int signal = SIGNAL_BLOCK;
	public static int digit = 0;
	public static int index = 0;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		if (!active)
			return;
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			Block block = event.getClickedBlock();
			Player player = event.getPlayer();
			
			switch (signal) {
			
			case SIGNAL_BLOCK:
			
				Config.setBlock(block, digit, index);
			
				player.sendMessage("§8[§c§l!§8] §9SharkClock> block set " + block.getLocation().getX() + " "
					+ block.getLocation().getY() + " " + block.getLocation().getZ() + " to digit " + digit + " at index " + index);
				
				break;
				
			case SIGNAL_POSITION:
				
				break;
				
			default:
				break;
			}
			
			active = false;
		}
	}
}