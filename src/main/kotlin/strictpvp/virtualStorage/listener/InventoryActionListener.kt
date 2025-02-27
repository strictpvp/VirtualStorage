package strictpvp.virtualStorage.listener

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.persistence.PersistentDataType
import strictpvp.virtualStorage.VirtualStorage.Companion.plugin


class InventoryActionListener : Listener {
    companion object {
        val openPlayers: MutableList<Player> = mutableListOf()
    }

    @EventHandler
    fun clickHandler(event: InventoryClickEvent) {
        if (!openPlayers.contains(event.whoClicked))
            return

        event.isCancelled = true

        when (event.action) {
            InventoryAction.PICKUP_SOME,
            InventoryAction.PICKUP_ONE,
            InventoryAction.PICKUP_HALF,
            InventoryAction.PICKUP_ALL -> {
                if (event.clickedInventory!! == event.view.bottomInventory)
                    return

                val meta = event.currentItem!!.itemMeta
                
                val key = NamespacedKey(plugin, "number")
                if (!meta!!.persistentDataContainer.has(key, PersistentDataType.INTEGER)) {
                    event.whoClicked.sendMessage("죄송합니다 오류가 발생했습니다. /창고 <number> 를 이용해주세요")
                    return
                }
                val number = meta.persistentDataContainer.get(key, PersistentDataType.INTEGER)!!

                openPlayers.remove(event.whoClicked as Player)
                Bukkit.dispatchCommand(event.whoClicked as CommandSender, "창고 $number")
            }

            else -> {}
        }
    }

    @EventHandler
    fun closeHandler(event: InventoryCloseEvent) {
        openPlayers.remove(event.player)
    }
}