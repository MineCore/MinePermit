package net.minecore.minepermit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Wall {

	/**
	 * for signs: getPermit() and then count number of ids (Int)
	 * add that many signs and add to length of wall
	 * @param l
	 */
	public Wall(Location l)
	{
		World w = l.getWorld();
		System.out.println("sdf");
		for(int x = l.getBlockX()-5; x < l.getBlockX()+5;x++)
		{
		
			System.out.println("asd");
			for(int y = l.getBlockY(); y < l.getBlockY()+3;y++){

				w.getBlockAt(x,y,l.getBlockZ()).setType(Material.WOOD);
				System.out.println(x+" "+y);
			}
		}
	}
}
