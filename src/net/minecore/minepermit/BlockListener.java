package net.minecore.minepermit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {
	
	private MinerManager mm;
	
	public BlockListener(MinerManager m){
		mm = m;
	}
	
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		
		if(!Config.isPermitRequired(e.getBlock().getTypeId()))
			return;
		
		Player p = e.getPlayer();
		
		if(p.hasPermission("MinePermit.exempt"))
			return;
		
		
		
		if(!mm.getMiner(p).hasPermit(e.getBlock().getTypeId())){
			e.setCancelled(true);
			p.sendMessage(ChatColor.DARK_RED + "You may not mine these blocks! Use /permit buy <id> to buy a permit." + "");
		}
		
	}

}
