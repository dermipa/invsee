package de.mipa.invsee;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;


public class Invsee extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("InvSee Plugin aktiviert!");
        registerCommand("openinv");
        registerCommand("openec");
    }

    @Override
    public void onDisable() {
        getLogger().info("InvSee Plugin deaktiviert!");
    }

    private void registerCommand(String name) {
        PluginCommand command = getCommand(name);
        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern genutzt werden!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("§cVerwendung: /" + label + " <Spieler>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("§cSpieler nicht gefunden oder offline!");
            return true;
        }

        if (command.getName().equalsIgnoreCase("openinv")) {
            if (!player.hasPermission("invsee.open")) {
                player.sendMessage("§cDazu hast du keine Berechtigung!");
                return true;
            }
            player.openInventory(target.getInventory());
            player.sendMessage("§aDu siehst nun das Inventar von " + target.getName());
            return true;
        }

        if (command.getName().equalsIgnoreCase("openec")) {
            if (!player.hasPermission("invsee.ender")) {
                player.sendMessage("§cDazu hast du keine Berechtigung!");
                return true;
            }
            Inventory enderChest = target.getEnderChest();
            player.openInventory(enderChest);
            player.sendMessage("§aDu siehst nun die Enderchest von " + target.getName());
            return true;
        }

        return false;
    }
}