package de.mipa.invsee;


import org.bstats.bukkit.Metrics;
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
        int pluginId = 24915; //
        Metrics metrics = new Metrics(this, pluginId);

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

            // Erstelle ein neues Inventar mit der Größe für 36 Slots und 4 Rüstungsslots
            Inventory inventory = Bukkit.createInventory(null, 45, "Inventar von " + target.getName()); // 45 Slots: 36 für Inventar, 9 für Rüstung

            // Fülle das Inventar mit dem normalen Inventar des Zielspielers
            inventory.setContents(target.getInventory().getContents());

            // Füge die Rüstung des Zielspielers in die ersten 4 Slots (die Rüstungsslots) ein
            inventory.setItem(36, target.getInventory().getItem(39)); // Stiefel
            inventory.setItem(37, target.getInventory().getItem(38)); // Leggings
            inventory.setItem(38, target.getInventory().getItem(37)); // Brustplatte
            inventory.setItem(39, target.getInventory().getItem(36)); // Helm

            // Öffne das benutzerdefinierte Inventar für den Spieler
            player.openInventory(inventory);
            player.sendMessage("§aDu siehst nun das Inventar und die Rüstung von " + target.getName());
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