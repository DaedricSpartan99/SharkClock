package it.sharkcraft.sharkclock;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TouchListener implements Listener {
	
	public static final int SIGNAL_BLOCK = 0x0;
	public static final int SIGNAL_POSITION = 0x1;
	public static final int SIGNAL_VOLUME = 0x2;
	
	public static final int VOLUME_FIRST = 0x0;
	public static final int VOLUME_SECOND = 0x1;
	
	public static boolean active = false;
	public static int signal = SIGNAL_BLOCK;
	public static int volume_prompt = VOLUME_FIRST;
	public static Location volume_first = null;
	public static int digit = 0;
	public static int index = 0;
	public static String pos_digit;
	public static String pos_arg;
	
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
				
				Config.setPosition(block.getLocation(), pos_arg, pos_digit);
				
				player.sendMessage("§8[§c§l!§8] §9SharkClock> block set " + block.getLocation().getX() + " "
						+ block.getLocation().getY() + " " + block.getLocation().getZ() + " as position for " + pos_arg + " argument "
						+ pos_digit + " digit");
				
				break;
				
			case SIGNAL_VOLUME:
				
				player.sendMessage("§8[§c§l!§8] §9SharkClock> Volume set " + block.getLocation().getX() + " "
						+ block.getLocation().getY() + " " + block.getLocation().getZ() + " as point for the volume");
				
				if (volume_prompt == VOLUME_FIRST) {
					
					volume_first = block.getLocation().clone();
					volume_prompt = VOLUME_SECOND;
					return;
				}
				
				System.out.println(volume_first.toString());
				System.out.println(block.getLocation().toString());
				
				Config.setDigitByVolume(volume_first.getBlock(), block, digit);
				
				player.sendMessage("§8[§c§l!§8] §9SharkClock> Generated block sequence");
				
				break;
				
			default:
				break;
			}
			
			active = false;
		}
	}
}
