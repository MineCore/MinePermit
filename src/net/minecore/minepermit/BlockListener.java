package net.minecore.minepermit;

import net.minecore.minepermit.miner.PermitMiner;
import net.minecore.minepermit.miner.PermitMinerManager;
import net.minecore.minepermit.permits.Permit;
import net.minecore.minepermit.world.PermitArea;
import net.minecore.minepermit.world.WorldPermitAreaManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {
	
	private PermitMinerManager mm;
	private WorldPermitAreaManager am;
	
	public BlockListener(PermitMinerManager m, WorldPermitAreaManager am){
		mm = m;
		this.am = am;
	}
	
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		
		
		
		Player p = e.getPlayer();
		
		if(p.hasPermission("MinePermit.exempt"))
			return;
		
		PermitArea pa = am.getRelevantPermitArea(e.getBlock().getLocation());
		if(pa.getEffectiveDepth() < e.getBlock().getY() || !pa.requiresPermit(e.getBlock().getTypeId()))
			return;
		
		PermitMiner pm = mm.getPermitMiner(p);
		Permit permit = pm.getPermit(pa, e.getBlock().getTypeId());
		
		if(permit == null){
			e.setCancelled(true);
			p.sendMessage(ChatColor.DARK_RED + "You may not mine these blocks! Use /permit buy <id> to buy a permit." + "");
		} else if(!permit.canStillBreakBlocks()){
			e.setCancelled(true);
			p.sendMessage(ChatColor.DARK_RED + "Your permit has run out! Use /permit buy <id> to buy a permit");
		}
		
	}

}
