package strictpvp.virtualStorage.listener

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import strictpvp.virtualStorage.util.Save

class InventoryCloseListener : Listener {
    companion object {
        val openPlayerList: MutableList<Player> = mutableListOf()
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        if (!openPlayerList.contains(event.player))
            return

        (event.player as Player).playSound(event.player.location, Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.0f)

        val list: MutableMap<Int, ItemStack> = mutableMapOf()

        for (slot in 0..53) {
            val item = event.view.topInventory.getItem(slot)
            if (item == null)
                continue
            list.put(slot, item)
        }

        val title = event.view.title

        if (!title.contains("번 창고"))
            return

        val regex = Regex("(\\d+)번 창고")
        val matchResult = regex.find(title)
        val number = matchResult?.groups?.get(1)?.value?.toIntOrNull() ?: -1

        openPlayerList.remove(event.player)
        Save.save(event.player.uniqueId.toString(), number, list)
    }
}