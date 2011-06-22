package onishinji.copyHouse.Listener;

import onishinji.copyHouse.CopyHouse;
import onishinji.copyHouse.CopyHouseManager;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

public class HelloPlayerListener extends PlayerListener {

	private CopyHouse plugin;

	public HelloPlayerListener(CopyHouse helloPlugin) {
		// TODO Auto-generated constructor stub
		plugin = helloPlugin;
	}
	
	public void onPlayerJoin(PlayerJoinEvent event)
	{
	//	event.getPlayer().sendMessage("Salut " + event.getPlayer().getName());
		if(event.getPlayer().getWorld().getPlayers().size() > 0)
		{
			String connectedUsers = "";
			for(Player p: event.getPlayer().getWorld().getPlayers())
			{
				connectedUsers = connectedUsers + p.getName() +"  ";
			} 
	//		event.getPlayer().sendMessage("Liste des personnes connectés : " + connectedUsers);
		}
		else
		{
		//	event.getPlayer().sendMessage("Tu es tout seul mon pote");
		}
	}

	public void onPlayerMove(PlayerMoveEvent event) {
		CopyHouseManager.getInstance().playerLocation = event.getPlayer().getLocation();
	}

	public void onPlayerInteract(PlayerInteractEvent event) {

		if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			Player player = event.getPlayer();

			// On regarde la valeur dans le wall sign
			if (block.getType() == Material.WALL_SIGN) {
				Sign signb = (Sign) block.getState();
				String[] lines = signb.getLines();

				String ligne = lines[0];
				if (ligne.indexOf("copy") >= 0) {
					String[] res = ligne.split(" "); 
					// On va chercher les deux blocs les plus proches
					CopyHouseManager.getInstance().copyHouse(signb, res[1], player, block);

				}
			}

			// On regarde la valeur dans le wall sign
			if(block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
			{
				Sign signb = (Sign) block.getState();
				String[] lines = signb.getLines();
				 
				String ligne = lines[0];
				
				if (ligne.indexOf("add") >= 0) {
					String[] res = ligne.split(" ");
					ligne = lines[1];
					int rotation = 0;
					if(ligne.indexOf("N") >= 0) rotation = CopyHouseManager.NORD;
					if(ligne.indexOf("E") >= 0) rotation = CopyHouseManager.EST;
					if(ligne.indexOf("O") >= 0) rotation = CopyHouseManager.OUEST;
					if(ligne.indexOf("S") >= 0) rotation = CopyHouseManager.SUD;
					
					CopyHouseManager.getInstance().pasteHouse(signb, res[1], player, block,rotation);
				}

				if (ligne.indexOf("remove") >= 0) {
					String[] res = ligne.split(" ");
					signb.setLine(0, res[1]);

					CopyHouseManager.getInstance().removeHouse(res[1], player, block);
				}
			}
		}
	}
}
