package strictpvp.virtualStorage

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import strictpvp.virtualStorage.command.OpenCommand
import strictpvp.virtualStorage.listener.InventoryActionListener
import strictpvp.virtualStorage.listener.InventoryCloseListener

class VirtualStorage : JavaPlugin() {
    companion object {
        lateinit var plugin: JavaPlugin
    }

    override fun onLoad() {
        logger.info("Thank you for using VirtualStorage Plugin")
        logger.info("VirtualStorage v${description.version} - by ${description.authors}")
    }

    override fun onEnable() {
        logger.info("Starting VirtualStorage...")

        // config
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        // set plugin
        plugin = this

        // register events
        Bukkit.getPluginManager().registerEvents(InventoryCloseListener(), this)
        Bukkit.getPluginManager().registerEvents(InventoryActionListener(), this)

        // register command and tab completer
        getCommand("창고")!!.setExecutor(OpenCommand())
        getCommand("창고")!!.tabCompleter = OpenCommand()
    }

    override fun onDisable() {
        logger.info("Stopping VirtualStorage...")
    }
}
