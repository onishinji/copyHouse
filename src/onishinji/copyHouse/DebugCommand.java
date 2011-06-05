package onishinji.copyHouse;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand implements CommandExecutor {
 

	public DebugCommand(CopyHouse hello, String string) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] arg3) { 
		
		Player player = (Player)sender;
		 
		
		int playerX = player.getLocation().getBlockX(); 
		int playerZ = player.getLocation().getBlockZ();
		int playerY = player.getLocation().getBlockY();
		
		int surface = 30;
		for(int x = playerX-surface; x < playerX+surface;x++)
		{
			for(int z = playerZ-surface; z < playerZ+surface;z++)
			{
				for(int y = playerY; y < playerY+60;y++)
				{
					player.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
					
				}
				player.getWorld().getBlockAt(x, player.getLocation().getBlockY()-1, z).setType(Material.DIAMOND_ORE);
			}
		}
		return false;
	}

}
