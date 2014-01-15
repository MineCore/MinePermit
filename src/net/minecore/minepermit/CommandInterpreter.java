package net.minecore.minepermit;

import java.util.Map;

import net.minecore.minepermit.miner.PermitHolder;
import net.minecore.minepermit.miner.PermitMiner;
import net.minecore.minepermit.miner.PermitMinerManager;
import net.minecore.minepermit.permits.UniversalPermit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import sun.security.krb5.Config;

public class CommandInterpreter implements CommandExecutor {

	private final MinePermit mp;
	private final PermitMinerManager mm;

	public CommandInterpreter(MinePermit mn, PermitMinerManager m) {
		mm = m;
		mp = mn;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmnd, String arg2, String[] arg3) {

		// Console can't send commands
		if (sender instanceof ConsoleCommandSender) {
			mp.log.info("Sorry, commands cant be sent from console yet.");
			return true;
		}

		Player p = (Player) sender;

		// Make sure there is a secondary parameter
		if (arg3.length == 0) {
			return false;

		} else if (arg3[0].equalsIgnoreCase("time")) {

			PermitMiner pm = mm.getPermitMiner(p);

			Material m;

			if (arg3.length < 2) {

				Map<String, PermitHolder> permits = pm.getPermits();

				for (String paString : permits.keySet()) {

					p.sendMessage("Permits in PermitArea " + paString);

					PermitHolder ph = permits.get(paString);

					UniversalPermit up = ph.getUniversalPermit();

					if (up != null) {
						p.sendMessage(ChatColor.DARK_GREEN + "You have "
								+ pm.getRemainingUniversalTime()
								+ " minutes left on the Universal Permit.");
						return true;
					}

				}

				if (tmp.isEmpty()) {
					p.sendMessage("" + ChatColor.YELLOW + "You don't own any permits!");
					return true;
				}

				for (int y = 0; y < tmp.size(); y++) {

					p.sendMessage(ChatColor.DARK_GREEN + "" + n + ": " + pm.getRemainingTime(n)
							+ " minutes.");
				}

				return true;
			}

			if (!pm.hasPermit(id)) {
				p.sendMessage(ChatColor.DARK_RED + "You do not own a permit for " + id);
			} else {
				p.sendMessage(ChatColor.DARK_GREEN + "You have " + pm.getRemainingTime(id)
						+ " minutes left.");
			}

			return true;

		} else if (arg3[0].equalsIgnoreCase("cost")) {

			int id;

			try {
				id = Integer.parseInt(arg3[1]);
			} catch (ArrayIndexOutOfBoundsException e1) {

				// If there is no number, change to Universal system
				p.sendMessage(ChatColor.AQUA
						+ "The cost for the Universal permit for this world is "
						+ Config.universalCost);
				return true;
			} catch (NumberFormatException e2) {
				return false;
			}

			if (!Config.isPermitRequired(id)) {
				p.sendMessage("" + ChatColor.GRAY + "A permit is not required for this item.");
				return true;
			}

			p.sendMessage(ChatColor.AQUA + "The cost for this item is " + Config.getCost(id)
					+ " dollars");
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
				if (!Config.multiPermit && mm.getPermitMiner(p).hasUniversalPermit()) {
					p.sendMessage("" + ChatColor.YELLOW + "You already have a permit for this!");
					return true;
				}

				// Charge player if possible
				if (!PlayerManager.charge(p, Config.universalCost)) {
					p.sendMessage("" + ChatColor.DARK_RED + "You dont have enough money!");
					return true;
				}

				mm.getPermitMiner(p).addUniversalPermit(Config.permitDuration);

				p.sendMessage("" + ChatColor.DARK_GREEN + "Permit purchased!");
				return true;
			}

			// Check if a permit is required for this block
			if (!Config.isPermitRequired(id)) {
				p.sendMessage("" + ChatColor.GRAY + "A permit is not required for this item.");
				return true;
			}

			// Check if the player already has a permit for this
			if (!Config.multiPermit && mm.getPermitMiner(p).hasPermit(id)) {
				p.sendMessage("" + ChatColor.YELLOW + "You already have a permit for this!");
				return true;
			}

			// Charge player if possible
			if (!PlayerManager.charge(p, Config.getCost(id))) {
				p.sendMessage("" + ChatColor.DARK_RED + "You dont have enough money!");
				return true;
			}

			mm.getPermitMiner(p).addPermit(id, Config.permitDuration);

			p.sendMessage("" + ChatColor.DARK_GREEN + "Permit purchased!");

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

				p.sendMessage(tmp);

				return true;
			} catch (NumberFormatException e2) {
				return false;
			}

			if (Config.isPermitRequired(id)) {
				p.sendMessage(ChatColor.DARK_RED + "A permit is required for " + id);
			} else {
				p.sendMessage(ChatColor.DARK_GREEN + "A permit is not required for " + id);
			}

			return true;

		}

		if (arg3[0].equals("wall")) {
			Wall w = new Wall(p.getLocation());
			return true;
		}

		return false;
	}
}
