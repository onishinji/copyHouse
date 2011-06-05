package onishinji.copyHouse.Listener;

import onishinji.copyHouse.CopyHouse;
import onishinji.copyHouse.CopyHouseManager;

import org.bukkit.Material;
import org.bukkit.block.Block; 
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent; 

public class HelloBlockListener extends BlockListener {

	private CopyHouse plugin;

	public HelloBlockListener(CopyHouse helloPlugin) {
		// TODO Auto-generated constructor stub
		plugin = helloPlugin;
	}
}
