package strictpvp.virtualStorage.util

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import strictpvp.virtualStorage.VirtualStorage.Companion.plugin
import java.io.File

class Save {
    companion object {
        fun save(uuid: String, storage: Int, items: MutableMap<Int, ItemStack>) {
            val dataFile = File("${plugin.dataFolder.path}/data", "${uuid}.yml")
            dataFile.parentFile.mkdirs()
            val dataYaml = YamlConfiguration.loadConfiguration(dataFile)

            for (slot in 0..53) {
                dataYaml.set("${storage}.${slot}", null)
            }

            for (item in items) {
                val slot = item.key
                val item = item.value

                dataYaml.set("${storage}.${slot}", item)
            }

            dataYaml.save(dataFile)
        }

        fun load(uuid: String, storage: Int) : MutableMap<Int, ItemStack> {
            val dataFile = File("${plugin.dataFolder.path}/data", "${uuid}.yml")
            val dataYaml = YamlConfiguration.loadConfiguration(dataFile)

            val list: MutableMap<Int, ItemStack> = mutableMapOf()

            for (slot in 0..53) {
                val item = dataYaml.getItemStack("${storage}.${slot}")
                if (item == null)
                    continue
                list.put(slot, item)
            }

            return list
        }
    }
}