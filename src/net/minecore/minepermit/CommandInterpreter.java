package net.minecore.minepermit;

import java.util.Map;
import java.util.logging.Logger;

import net.minecore.EconomyManager;
import net.minecore.minepermit.miner.PermitHolder;
import net.minecore.minepermit.miner.PermitMiner;
import net.minecore.minepermit.miner.PermitMinerManager;
import net.minecore.minepermit.permits.BlockCountPermit;
import net.minecore.minepermit.permits.Permit;
import net.minecore.minepermit.permits.PermitType;
import net.minecore.minepermit.permits.TimedPermit;
import net.minecore.minepermit.world.PermitArea;
import net.minecore.minepermit.world.WorldPermitAreaManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandInterpreter implements CommandExecutor {

	private final PermitMinerManager mm;
	private final WorldPermitAreaManager areaManager;
	private final Logger log;
	private final EconomyManager econManager;

	public CommandInterpreter(MinePermit mp) {
		areaManager = mp.getWorldPermitAreaManager();
		mm = mp.getPermitMinerManager();
		log = mp.log;
		econManager = mp.getMineCore().getEconomyManager();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmnd, String arg2, String[] arg3) {

		// Console can't send commands
		if (sender instanceof ConsoleCommandSender) {
			log.info("Sorry, commands cant be sent from console yet.");
			return true;
		}

		Player player = (Player) sender;
		PermitMiner pm = mm.getPermitMiner(player);
		PermitArea pa = areaManager.getRelevantPermitArea(player.getLocation());

		// Make sure there is a secondary parameter
		if (arg3.length == 0) {
			return false;

		} else if (arg3[0].equalsIgnoreCase("remaining")) {

			Map<String, PermitHolder> permits = pm.getPermits();

			if (arg3.length < 2) {

				for (String paString : permits.keySet()) {

					player.sendMessage("Permits in PermitArea " + paString);

					PermitHolder ph = permits.get(paString);

					printPermitHolder(ph, player);

				}
			} else {

				String local = arg3[1];

				if (local.equalsIgnoreCase("here")) {

					PermitHolder ph = permits.get(pa.getStringRepresentation());

					printPermitHolder(ph, player);

				} else if (local.equalsIgnoreCase("world")) {

					for (String paString : permits.keySet()) {
						if (paString.substring(0, paString.indexOf('.')).equals(
								player.getLocation().getWorld().getName()))
							printPermitHolder(permits.get(paString), player);
					}

				} else {
					printPermitHolder(permits.get(arg3[1]), player);
				}

			}

			return true;

		} else if (arg3[0].equalsIgnoreCase("cost")) { // /permit cost COUNTED

			if (arg3.length < 2) {

				for (PermitType pt : PermitType.values())
					player.sendMessage(ChatColor.AQUA + "The cost for the Universal " + pt.name()
							+ "permit for this world is " + pa.getUniversalPermitPrice());

				return true;
			}

			Material m = Material.matchMaterial(arg3[1]);

			if (m == null) {
				player.sendMessage(ChatColor.RED + "Invalid Material!");
			} else if (!pa.requiresPermit(m)) {
				player.sendMessage(ChatColor.GRAY + "A permit is not required for this item.");
			} else
				player.sendMessage(ChatColor.AQUA + "The cost for this item is " + pa.getPrice(m));

			return true;

		} else if (arg3[0].equalsIgnoreCase("buy")) {

			if (arg3.length < 3)
				return false;

			PermitType type = PermitType.valueOf(arg3[2].toUpperCase());

			if (type == null) {
				player.sendMessage(ChatColor.DARK_RED + "Invalid Permit Type!");
				return true;
			}

			if (arg3[1].equalsIgnoreCase("universal")) {
				// Check if the player already has the Universal permit
				if (pm.getPermitHolder(pa).hasUniversalPermit()) {
					player.sendMessage("" + ChatColor.YELLOW
							+ "You already have a Universal Permit for here!");
					return true;
				}

				// Charge player if possible
				if (!econManager.charge(player, pa.getUniversalPermitPrice().getCost(type))) {
					player.sendMessage(ChatColor.DARK_RED + "You dont have enough money!");
					return true;
				}

				Permit upermit;

				switch (type) {
				case COUNTED:
					upermit = new BlockCountPermit(null, pa.getUniversalPermitPrice().getAmount(
							type));
					break;
				case TIMED:
					upermit = new TimedPermit(null, pa.getUniversalPermitPrice().getAmount(type));
					break;
				default:
					return false;
				}

				pm.getPermitHolder(pa).addUniversalPermit(upermit);

				player.sendMessage(ChatColor.DARK_GREEN + "Permit purchased!");
				return true;
			}

			Material m = Material.matchMaterial(arg3[1]);

			if (m == null) {
				player.sendMessage(ChatColor.RED + "Invalid Material!");
				return true;
			}

			// Check if a permit is required for this block
			if (!pa.requiresPermit(m)) {
				player.sendMessage("" + ChatColor.GRAY + "A permit is not required for this item.");
				return true;
			}

			// Check if the player already has a permit for this
			if (pm.hasPermit(pa, m)) {
				player.sendMessage("" + ChatColor.YELLOW + "You already have a permit for this!");
				return true;
			}

			// Charge player if possible
			if (!econManager.charge(player, pa.getPrice(m).getCost(type))) {
				player.sendMessage("" + ChatColor.DARK_RED + "You dont have enough money!");
				return true;
			}

			Permit permit = null;

			switch (type) {
			case COUNTED:
				permit = new BlockCountPermit(m, pa.getPrice(m).getAmount(type));
				break;
			case TIMED:
				permit = new TimedPermit(m, pa.getPrice(m).getAmount(type));
				break;
			}

			pm.addPermit(pa, permit);

			player.sendMessage(ChatColor.DARK_GREEN + "Permit purchased!");

			return true;
		} else if (arg3[0].equalsIgnoreCase("view")) {

			if (arg3.length < 2) {

				String tmp = ChatColor.BLUE + "Permits are required for: ";

				for (Material m : pa.getPrices().getPrices().keySet()) {

					tmp += ChatColor.AQUA + m.name() + ChatColor.WHITE + ", ";

				}

				player.sendMessage(tmp);

				return true;
			}

			Material m = Material.matchMaterial(arg3[1]);

			if (pa.requiresPermit(m)) {
				player.sendMessage(ChatColor.RED + "A permit is required for " + m);
			} else {
				player.sendMessage(ChatColor.GREEN + "A permit is not required for " + m);
			}

			return true;
		}

		return false;
	}

	private void printPermitHolder(PermitHolder ph, Player player) {

		if (ph == null) {
			player.sendMessage(ChatColor.RED + "You do not have any Permits for this area!");
			return;
		}

		Permit up = ph.getUniversalPermit();

		if (up != null) {
			player.sendMessage(ChatColor.DARK_GREEN + up.toString());
		}

		for (Permit p : ph.getPermits()) {
			player.sendMessage(ChatColor.AQUA + p.toString());
		}

	}
}
