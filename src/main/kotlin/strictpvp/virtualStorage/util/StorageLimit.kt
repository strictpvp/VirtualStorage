package strictpvp.virtualStorage.util

import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.user.User
import org.bukkit.entity.Player
import strictpvp.virtualStorage.VirtualStorage.Companion.plugin

class StorageLimit {
    companion object {
        fun getPrimaryGroup(player: Player): String? {
            val luckPerms = LuckPermsProvider.get()
            val user: User = luckPerms.userManager.getUser(player.uniqueId) ?: return null
            val queryOptions = luckPerms.contextManager.getQueryOptions(user).orElse(null)

            if (queryOptions == null) return null
            val sortedGroups = user.getInheritedGroups(queryOptions)
                .sortedByDescending { it.weight.orElse(0) }

            return sortedGroups.firstOrNull()?.name
        }

        fun getLimit(player: Player): Int {
            var maxLimit = 1

            val group = getPrimaryGroup(player)
            if (group == null)
                return maxLimit
            
            maxLimit = plugin.config.getInt("limit.${group.lowercase()}")

            return maxLimit
        }
    }
}