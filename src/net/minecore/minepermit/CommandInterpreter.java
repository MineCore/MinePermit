package net.minecore.minepermit;

import java.util.Map;
import java.util.logging.Logger;

import net.minecore.minepermit.miner.PermitHolder;
import net.minecore.minepermit.miner.PermitMiner;
import net.minecore.minepermit.miner.PermitMinerManager;
import net.minecore.minepermit.permits.Permit;
import net.minecore.minepermit.permits.PermitType;
import net.minecore.minepermit.permits.UniversalPermit;
import net.minecore.minepermit.world.PermitArea;
import net.minecore.minepermit.world.WorldPermitAreaManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import sun.security.krb5.Config;

public class CommandInterpreter implements CommandExecutor {

	private final PermitMinerManager mm;
	private final WorldPermitAreaManager areaManager;
	private final Logger log;

	public CommandInterpreter(MinePermit mp) {
		areaManager = mp.getWorldPermitAreaManager();
		mm = mp.getPermitMinerManager();
		log = mp.log;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmnd, String arg2, String[] arg3) {

		// Console can't send commands
		if (sender instanceof ConsoleCommandSender) {
			log.info("Sorry, commands cant be sent from console yet.");
			return true;
		}

		Player player = (Player) sender;

		// Make sure there is a secondary parameter
		if (arg3.length == 0) {
			return false;

		} else if (arg3[0].equalsIgnoreCase("remaining")) {

			PermitMiner pm = mm.getPermitMiner(player);

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

					PermitArea pa = areaManager.getRelevantPermitArea(player.getLocation());
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

		} else if (arg3[0].equalsIgnoreCase("cost")) {

			PermitArea pa = areaManager.getRelevantPermitArea(player.getLocation());

			if (arg3.length < 2) {

				for (PermitType pt : PermitType.values())
					player.sendMessage(ChatColor.AQUA + "The cost for the Universal " + pt.name()
							+ "permit for this world is " + pa.getUniversalPermitCost(pt));

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

			int id;

			// Atempt to get an id number for the block
			try {
				id = Integer.parseInt(arg3[1]);
			} catch (ArrayIndexOutOfBoundsException e1) {
				return false;
			} catch (NumberFormatException e) {

				if (!arg3[1].equalsIgnoreCase("universal"))
					return false;

				// Check if the player already has the Universal permit
				if (!Config.multiPermit && mm.getPermitMiner(player).hasUniversalPermit()) {
					player.sendMessage("" + ChatColor.YELLOW
							+ "You already have a permit for this!");
					return true;
				}

				// Charge player if possible
				if (!PlayerManager.charge(player, Config.universalCost)) {
					player.sendMessage("" + ChatColor.DARK_RED + "You dont have enough money!");
					return true;
				}

				mm.getPermitMiner(player).addUniversalPermit(Config.permitDuration);

				player.sendMessage("" + ChatColor.DARK_GREEN + "Permit purchased!");
				return true;
			}

			// Check if a permit is required for this block
			if (!Config.isPermitRequired(id)) {
				player.sendMessage("" + ChatColor.GRAY + "A permit is not required for this item.");
				return true;
			}

			// Check if the player already has a permit for this
			if (!Config.multiPermit && mm.getPermitMiner(player).hasPermit(id)) {
				player.sendMessage("" + ChatColor.YELLOW + "You already have a permit for this!");
				return true;
			}

			// Charge player if possible
			if (!PlayerManager.charge(player, Config.getCost(id))) {
				player.sendMessage("" + ChatColor.DARK_RED + "You dont have enough money!");
				return true;
			}

			mm.getPermitMiner(player).addPermit(id, Config.permitDuration);

			player.sendMessage("" + ChatColor.DARK_GREEN + "Permit purchased!");

			return true;
		} else if (arg3[0].equalsIgnoreCase("view")) {

			int id;

			try {
				id = Integer.parseInt(arg3[1]);
			} catch (IndexOutOfBoundsException e) {

				Map<Integer, Integer> perms = Config.getPermits();
				String tmp = ChatColor.DARK_PURPLE + "Permits are required for: ";

				for (Integer i : perms.keySet()) {
					tmp += i + " ";
				}

				player.sendMessage(tmp);

				return true;
			} catch (NumberFormatException e2) {
				return false;
			}

			if (Config.isPermitRequired(id)) {
				player.sendMessage(ChatColor.DARK_RED + "A permit is required for " + id);
			} else {
				player.sendMessage(ChatColor.DARK_GREEN + "A permit is not required for " + id);
			}

			return true;

		}

		if (arg3[0].equals("wall")) {
			Wall w = new Wall(player.getLocation());
			return true;
		}

		return false;
	}

	private void printPermitHolder(PermitHolder ph, Player player) {

		if (ph == null) {
			player.sendMessage(ChatColor.RED + "You do not have any Permits for this area!");
			return;
		}

		UniversalPermit up = ph.getUniversalPermit();

		if (up != null) {
			player.sendMessage(ChatColor.DARK_GREEN + up.toString());
		}

		for (Permit p : ph.getPermits()) {
			player.sendMessage(ChatColor.AQUA + p.toString());
		}

	}
}
