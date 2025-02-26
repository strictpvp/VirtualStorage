package strictpvp.virtualStorage.command

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import strictpvp.virtualStorage.VirtualStorage.Companion.plugin
import strictpvp.virtualStorage.listener.InventoryActionListener
import strictpvp.virtualStorage.listener.InventoryCloseListener
import strictpvp.virtualStorage.util.Save
import strictpvp.virtualStorage.util.StorageLimit


class OpenCommand : CommandExecutor, TabCompleter {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String?>
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage("창고는 플레이어만 이용할 수 있습니다.")
            return true
        }

        val player: Player = sender

        if (args.isEmpty()) {
            val items = mutableMapOf<Int, ItemStack>()
            val gui: Inventory = Bukkit.createInventory(null, 36, "가상창고")

            for (slot in 1..36) {
                if (slot > StorageLimit.getLimit(sender)) {
                    break
                }

                val stack = ItemStack(Material.CHEST)
                val meta = stack.itemMeta

                meta!!.setDisplayName("가상창고 $slot")
                meta.lore = listOf<String>(
                    "${ChatColor.WHITE}클릭시 해당 창고로 이동 합니다."
                )
                val key = NamespacedKey(plugin, "number")
                meta.persistentDataContainer.set(key, PersistentDataType.INTEGER, slot)

                stack.itemMeta = meta

                items.put(slot - 1, stack)
            }

            items.forEach(gui::setItem)
            player.openInventory(gui)
            InventoryActionListener.openPlayers.add(player)

            return true
        }

        var number = args[0]?.toInt()

        if (number == null || number < 1) {
            sender.sendMessage("올바른 자연수를 입력해주세요")
            return true
        }
        if (StorageLimit.getLimit(player) < number) {
            sender.sendMessage("입력하신 숫자가 보유중인 창고의 수보다 큽니다")
            return true
        }

        if (InventoryCloseListener.openPlayerList.contains(player)) {
            player.closeInventory()
        }

        val items = Save.load(player.uniqueId.toString(), number)
        val gui: Inventory = Bukkit.createInventory(null, 54, "가상창고 $number")
        items.forEach(gui::setItem)
        player.openInventory(gui)
        InventoryCloseListener.openPlayerList.add(player)

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String?>
    ): List<String?>? {
        if (args.isEmpty()) {
            val limit = StorageLimit.getLimit(sender as Player)
            val list = mutableListOf<String?>()
            for (i in limit downTo 0) {
                list.add(i.toString())
            }
            return list
        }

        return listOf()
    }
}