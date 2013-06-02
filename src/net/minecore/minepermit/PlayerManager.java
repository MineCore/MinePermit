package net.minecore.minepermit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerManager {

	public static boolean charge(Player p, int amount) {
		if (Config.useEconomyPlugin) {
			
/*			EconomyResponse r = MinePermit.econ.withdrawPlayer(p.getName(), amount);
			
			if(r.transactionSuccess())
				return true;
				*/
			return false;

			
		} else {
			
			while (amount > 0) {
				ItemStack x = p.getInventory().getItem(
						p.getInventory().first(Config.currencyBlockID));

				if (x.getAmount() < amount) {
					amount -= x.getAmount();
					x.setAmount(0);
				} else {
					x.setAmount(x.getAmount() - amount);
					amount = 0;
				}
			}
			return true;
			
		}
		
	}
	
}
