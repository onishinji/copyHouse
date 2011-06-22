package onishinji.copyHouse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import onishinji.models.BlockManager;
import onishinji.models.MyLocation;
import onishinji.models.SLAPI;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;
import org.bukkit.material.Wool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A utility class of which at most one instance can exist per VM.
 * 
 * Use Singleton.instance() to access this instance.
 */
public class CopyHouseManager { 
	
	public static final  int NORD  = 0;
	public static final  int EST   = 1;
	public static final  int SUD   = 2;
	public static final  int OUEST = 3;
	
	public HashMap<String, ArrayList<BlockManager>> motifs; 
	public Location playerLocation;
	
	public HashMap<Player, ArrayList<BlockManager>> undosBlock;

	/**
	 * The constructor could be made private to prevent others from
	 * instantiating this class. But this would also make it impossible to
	 * create instances of Singleton subclasses.
	 */
	protected CopyHouseManager() {
		// ...
	}

	/**
	 * A handle to the unique Singleton instance.
	 */
	static private CopyHouseManager _instance = null;

	/**
	 * @return The unique instance of this class.
	 */
	static public CopyHouseManager getInstance() {
		if (null == _instance) {
			_instance = new CopyHouseManager(); 
			_instance.motifs = new HashMap<String, ArrayList<BlockManager>>();
			_instance.undosBlock = new HashMap<Player, ArrayList<BlockManager>>();
			try {
				File[] files = SLAPI.listFiles("plugins","copyHouse");
				for(File currentFile: files)
				{
					if(currentFile.getName().contains(".bin"))
					{
						System.out.println("Chargement de " + currentFile.getPath());
						HashMap<String, ArrayList<BlockManager>> test = (HashMap<String, ArrayList<BlockManager>>) SLAPI.load(currentFile.getPath());
						if(test.size()>0)
						{
							_instance.motifs.putAll(test);
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return _instance;
	} 
	 
	public Location getLocationForCopiedBlock(Block block, Location diff)
	{
		Location blockLocation = block.getLocation(); 
		return this.getLocationForCopiedBlock(blockLocation, diff); 
	}
	

	public Location getLocationForCopiedBlock(Location block, Location diff)
	{
		Location blockLocation = block;
		
		blockLocation.setX(blockLocation.getBlockX() + diff.getBlockX());
		blockLocation.setZ(blockLocation.getBlockZ()  + diff.getBlockZ());
		blockLocation.setY(blockLocation.getBlockY()  + diff.getBlockY());
		
		return blockLocation;
	}
	


	public Location computeVector(Block block) {
 
		Location blockLocation = block.getLocation();
		
		Location diff = new Location(block.getWorld(), 0, 0, 0);
		
		diff.setX(playerLocation.getBlockX()+1 - blockLocation.getBlockX());
		diff.setZ(playerLocation.getBlockZ()+1 - blockLocation.getBlockZ());
		diff.setY(playerLocation.getBlockY() - blockLocation.getBlockY());
		
		return diff;
		
	}
 
	
	public Location computeVector(Block block, Location ref) {
		
		Location blockLocation = block.getLocation();
		
		Location diff = new Location(block.getWorld(), 0, 0, 0);
		
		diff.setX(ref.getBlockX()+1 - blockLocation.getBlockX());
		diff.setZ(ref.getBlockZ()+1 - blockLocation.getBlockZ());
		diff.setY(ref.getBlockY() - blockLocation.getBlockY());
		
		return diff;
		
	}

	public void copyHouse(Sign signb, String name, Player player, Block blockSign) {
		
		if(!player.isOp())
		{
			player.sendMessage("Seul un OP peut copier un batiment");
			return;
		}
		
		if(this.motifs.get(name) != null)
		{
			player.sendMessage("Ce nom est deja pris");
			 
		}
		else
		{ 
			World world = signb.getBlock().getWorld();
			int blockX = signb.getBlock().getLocation().getBlockX(); 
			int blockZ = signb.getBlock().getLocation().getBlockZ();
			int blockY = signb.getBlock().getLocation().getBlockY();
			
			int surface = 40;
			ArrayList<Block> findBlock = new ArrayList<Block>();
			
			for(int x = blockX-surface; x < blockX+surface;x++)
			{
				for(int z = blockZ-surface; z < blockZ+surface;z++)
				{ 
					Block currentblock = world.getBlockAt(x, blockY, z);
					if(currentblock.getType() == Material.IRON_BLOCK &&
							findBlock.size() < 2)
					{
						findBlock.add(currentblock);
					}
				}
			}  
			
			if(findBlock.size() == 2)
			{ 
			
				player.sendMessage("Copie de la structure en cours");
				Block first = findBlock.get(0);
				Block second = findBlock.get(1);
				Block tmp;
				if(!(
						first.getLocation().getBlockX() > second.getLocation().getBlockX()
						&& first.getLocation().getBlockZ() > second.getLocation().getBlockZ() 
					)
				)
				{
					tmp = first;
					first = second;
					second = tmp;
				}
				
				ArrayList<BlockManager> blocks = new ArrayList<BlockManager>();
				int maxY = 127 - blockY;
				
				int maxX = second.getLocation().getBlockX() -  first.getLocation().getBlockX();
				int maxZ = second.getLocation().getBlockZ() -  first.getLocation().getBlockZ();
				 
				for(int x = 1; x < Math.abs(maxX); x++ )
				{ 
					for(int z = 1; z < Math.abs(maxZ); z++ )
					{
						int currentX;
						int currentY;
						int currentZ;
						if(maxX > 0)
							currentX = first.getLocation().getBlockX() + x;
						else
							currentX = first.getLocation().getBlockX() - x;
						

						if(maxZ > 0)
							currentZ = first.getLocation().getBlockZ() + z;
						else
							currentZ = first.getLocation().getBlockZ() - z;  
						
						for(int y = 0; y <= maxY;y++)
						{ 
							currentY = first.getLocation().getBlockY() + y;
							
							Block currentblock = world.getBlockAt(currentX, currentY, currentZ);
							if(!currentblock.getLocation().equals(first.getLocation())
							&& !currentblock.getLocation().equals(second.getLocation()))
							{
								BlockManager currentBlockManager = new BlockManager();
								blocks.add(currentBlockManager.convertBlock(currentblock));
							}
							 
						}				
					}
				} 
				int nbBlocks = 0;
				for(BlockManager b : blocks)
				{
					if(b.getType() != Material.AIR) nbBlocks++;
				}
				
				player.sendMessage(nbBlocks + " bloc(s) copié(s)");
				
				if(blocks.size()>0)
				{
					// Pour changer le label de la pancarte
					signb.setLine(0, name); 
					blockSign.getWorld().getBlockAt(blockSign.getLocation()).setData(blockSign.getData());
					
					
					player.sendMessage("Copie de la structure terminé");
					this.motifs.put(name, blocks);

					first.setType(Material.AIR);
					second.setType(Material.AIR);
					
					
					try {
						SLAPI.save(this.motifs, "plugins/copyHouse/"+name+".bin");
						 
				        // export XML
				        String root = "structure";

				        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				        Document document = documentBuilder.newDocument();
				        Element rootElement = document.createElement(root);
				        document.appendChild(rootElement);

				        for(BlockManager block : blocks)
				        {

                            Element blockEl = document.createElement("block");
                            
				            Element locationEl = document.createElement("location");

                            Element locationXEl = document.createElement("x");
                            Element locationYEl = document.createElement("y");
                            Element locationZEl = document.createElement("z");
                            
                            locationXEl.appendChild(document.createTextNode(""+block.getLocation(null).getBlockX()));
                            locationYEl.appendChild(document.createTextNode(""+block.getLocation(null).getBlockY()));
                            locationZEl.appendChild(document.createTextNode(""+block.getLocation(null).getBlockZ()));
                            
                            
                            locationEl.appendChild(locationXEl);
                            locationEl.appendChild(locationYEl);
                            locationEl.appendChild(locationZEl);
                            

                            Element typeEl = document.createElement("type");
                            typeEl.appendChild(document.createTextNode(""+block.getType()));

                            Element dataEl = document.createElement("data");
                            dataEl.appendChild(document.createTextNode(""+block.getData()));

                            blockEl.appendChild(typeEl);
                            blockEl.appendChild(locationEl);
                            blockEl.appendChild(dataEl);
                            
                            rootElement.appendChild(blockEl);
				        } 
 
				        
				        TransformerFactory transformerFactory = TransformerFactory.newInstance();
				        javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
				        DOMSource source = new DOMSource(document);
				        StreamResult result = new StreamResult(new File("plugins/copyHouse/"+name+".xml"));
				        transformer.transform(source, result);
				        
				        
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else
			{ 
				player.sendMessage("Impossible de trouver la structure");
			}
		}
	}

	public void pasteHouse(Sign signb, String name, Player player, Block blockSign, int rotation) {
		if(this.motifs.get(name) != null)
		{
			signb.setLine(0, name); 
			blockSign.getWorld().getBlockAt(blockSign.getLocation()).setData(blockSign.getData());

			
			World world = signb.getWorld();
			ArrayList<BlockManager> blocks = this.motifs.get(name);
			
			int nbBlocks = 0;
			for(BlockManager b : blocks)
			{
				if(b.getType() != Material.AIR) nbBlocks++;
			}
			player.sendMessage(nbBlocks + " bloc(s) à coller");
			
			Block init = world.getBlockAt(blocks.get(0).getLocation(world));
			
			Location vector = CopyHouseManager.getInstance().computeVector(init, signb.getBlock().getLocation());
			
			boolean oui = true;
			ArrayList<BlockManager> oldBlocks = new ArrayList<BlockManager>();
			
			for(BlockManager blockM : blocks)
			{  
				Location currentLocationInit = blockM.getLocation(world); 
				 
				currentLocationInit = this.rotate(rotation, init.getLocation(), currentLocationInit);
				 
				Location newLocation = CopyHouseManager.getInstance().getLocationForCopiedBlock(currentLocationInit, vector);  
				
				oui = false;
				newLocation.setPitch(blockM.getDirection());
				
			
				// Pour le undo
				BlockManager oldBlock = new BlockManager();
				oldBlock.setLocation(new MyLocation(newLocation));
				oldBlock.setType(world.getBlockAt(newLocation).getType());
				oldBlock.setData(world.getBlockAt(newLocation).getData());
				oldBlocks.add(oldBlock);
				
				// Modifie le block
				world.getBlockAt(newLocation).setType(blockM.getType());
				int newDirection = this.computeDataRotation(rotation, this.getDirectionFromData(blockM.getData()));
				
				// Si c'est un block avec une direction
				if(blockM.isDirectionnalable())
				{
					world.getBlockAt(newLocation).setData(this.getDataFromDirection(newDirection));	
				}
				else
				{
					// sinon c'est un block normal
					world.getBlockAt(newLocation).setData(blockM.getData());
				}
				
			}

			undosBlock.put(player, oldBlocks);
			player.sendMessage("Ajout de " + name + " terminé");
			
		}
		else
		{ 
			player.sendMessage("Aucun structure ne porte ce nom"); 
		}
	}
	

	private int computeDataRotation(int initialDirection, int finalDirection) {
		// TODO Auto-generated method stub 
		return ((initialDirection +finalDirection) % 4);
	}

	private byte getDataFromDirection(int direction)
	{ 
		byte res;
		switch (direction) {
		case CopyHouseManager.SUD:
         default:
            res = 0x0;
            break;
         case CopyHouseManager.NORD:
            res = 0x1;
            break;
         case CopyHouseManager.OUEST:
             res = 0x2;
             break;
         case CopyHouseManager.EST:
             res = 0x3;
             break;
         }
		return res;
 
	}
	
	
	private int getDirectionFromData(byte data)
	{ 
		int res;
		switch (data) {
		case 0x0:
         default:
            res = CopyHouseManager.SUD;
            break;
         case 0x1:
            res = CopyHouseManager.NORD;
            break;
         case 0x2:
             res = CopyHouseManager.OUEST;
             break;
         case 0x3:
             res = CopyHouseManager.EST;
             break;
         }
		return res;
	}
	
	public Location rotate(int direction, Location refPoint, Location block){ 
		
		int diffX;
		int diffZ;
		
		diffX = block.getBlockX() - refPoint.getBlockX();
		diffZ = block.getBlockZ() - refPoint.getBlockZ();
		 
		switch(direction){
		case CopyHouseManager.NORD:
			block.setX(refPoint.getX() + diffX);  
			block.setZ(refPoint.getBlockZ() + diffZ);
		break;
		case CopyHouseManager.EST: 
			block.setX(refPoint.getX() - diffZ);  
			block.setZ(refPoint.getBlockZ() + diffX); 
		break;
		case CopyHouseManager.OUEST:
			block.setX(refPoint.getX() + diffZ); 
			block.setZ(refPoint.getBlockZ() - diffX); 
		break;
		case CopyHouseManager.SUD:
			block.setX(refPoint.getX() - diffX); 
			block.setZ(refPoint.getBlockZ() - diffZ);
		break;	  
		}
		return block;
	}

	public void removeHouse(String name, Player player, Block blockSign) {
		if(this.motifs.get(name) != null)
		{
			player.sendMessage("Suppresion de " + name +" effectué"); 
			
			blockSign.getWorld().getBlockAt(blockSign.getLocation()).setType(Material.AIR); 
			this.motifs.remove(name);
			try {
				SLAPI.remove("plugins/copyHouse/"+name+".bin");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{ 
			player.sendMessage("Aucun structure ne porte ce nom"); 
		}
	}

	public void undoLastPaste(Player sender) {
		// TODO Auto-generated method stub

		if(undosBlock.containsKey(sender))
		{
			ArrayList<BlockManager> blocksM = undosBlock.get(sender);
			for(BlockManager block: blocksM)
			{
				sender.getWorld().getBlockAt(block.getLocation(sender.getWorld())).setType(block.getType()); 
				sender.getWorld().getBlockAt(block.getLocation(sender.getWorld())).setData(block.getData());
			}
		}
	}

}