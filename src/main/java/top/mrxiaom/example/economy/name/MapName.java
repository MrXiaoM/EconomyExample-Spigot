package top.mrxiaom.example.economy.name;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.example.economy.api.ICurrency;
import top.mrxiaom.example.economy.api.ICurrencyName;

import java.util.HashMap;
import java.util.Map;

public class MapName implements ICurrencyName {
    public final Map<String, String> names = new HashMap<>();

    /**
     * 从配置文件中重载名称
     */
    public void reload(@Nullable ConfigurationSection root) {
        names.clear();
        if (root != null) for (String key : root.getKeys(false)) {
            if (root.isConfigurationSection(key)) {
                ConfigurationSection section = root.getConfigurationSection(key);
                if (section != null) for (String id : section.getKeys(false)) {
                    String str = (key + ":" + id).toLowerCase();
                    names.put(str, section.getString(id));
                }
            } else {
                String str = key.toLowerCase();
                names.put(str, root.getString(key));
            }
        }
    }

    @Override
    public @Nullable String getName(@NotNull ICurrency currency) {
        return names.getOrDefault(currency.serialize(), currency.getCurrencyId());
    }
}
