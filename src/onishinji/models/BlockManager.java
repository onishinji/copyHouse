package onishinji.models;

import java.io.Serializable;
import java.util.ArrayList;

import onishinji.copyHouse.CopyHouseManager;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;

public class BlockManager  implements Serializable{
  
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Class<? extends Block> classname;
	private byte data;
	private Material type;

	private MyLocation location;
	private float direction;

	private DyeColor woolColor;
	
	
	public Material getType() {
		return type;
	}

	public void setType(Material type) {
		this.type = type;
	}

	public Location getLocation(World world) {
		return new Location(world, location.getX(), location.getY(), location.getZ()); 
	}

	public void setLocation(MyLocation location) {
		this.location = location;
	} 
	
	public void setDirection(float direction) {
		this.direction = direction;
	}

	public float getDirection() {
		return direction;
	}   
	
	public void setData(byte data) {
		this.data = data;
	}

	public byte getData() {
		return data;
	}
	
	public void setWoolColor(DyeColor woolColor) {
		this.woolColor = woolColor;
	}

	public DyeColor getWoolColor() {
		return woolColor;
	}

	public BlockManager()
	{
		
	}
	
	public boolean isWool()
	{
		return this.type == Material.WOOL;
	}
	
	public BlockManager convertBlock(Block block)
	{ 
		this.classname = block.getClass();
		this.setData(block.getData());
		this.type = block.getType();				
		this.setDirection(block.getLocation().getPitch());		
		this.location = new MyLocation(block.getLocation());
		
		if(block.getType() == Material.WOOL)
		{
			System.out.println("copie wool: data: " + block.getData());
			if(block.getData() == 0xE)
			{
				System.out.println("block rouge detecte");
				this.setWoolColor(DyeColor.RED);
			}
		//	this.setWoolColor(wool.getColor());
		}
		
		return this;
	}

	public boolean isDirectionnalable() {
		
		ArrayList<Material> directionnables = new ArrayList<Material>();
		directionnables.add(Material.BED);
		directionnables.add(Material.BED_BLOCK);
		directionnables.add(Material.BOOK);	
		directionnables.add(Material.WOOD_STAIRS);
		directionnables.add(Material.COBBLESTONE_STAIRS);
		
		for(Material t: directionnables)
		{
			if(t == this.type)
			{
				return true;
			}
		}
		
		
		return false;
	}

}
