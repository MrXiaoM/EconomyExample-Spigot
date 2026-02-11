package top.mrxiaom.example.economy.currency;

import com.mc9y.nyeconomy.api.NyEconomyAPI;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.example.economy.EconomyRegistry;
import top.mrxiaom.example.economy.api.ICurrency;
import top.mrxiaom.example.economy.api.ICurrencyProvider;

import java.util.HashMap;
import java.util.Map;

public class NyEconomyCurrency implements ICurrency {
    public static class Provider implements ICurrencyProvider {
        private final NyEconomyAPI economy = NyEconomyAPI.getInstance();
        private final Map<String, ICurrency> cache = new HashMap<>();
        private final EconomyRegistry registry;
        public Provider(EconomyRegistry registry) {
            this.registry = registry;
        }

        @Override
        public @Nullable ICurrency parse(@NotNull String str) {
            if (str.startsWith("NyEconomy:")) {
                String type = str.substring(10);
                ICurrency exists = cache.get(type);
                if (exists != null) return exists;
                NyEconomyCurrency currency = new NyEconomyCurrency(registry, economy, type);
                cache.put(type, currency);
                return currency;
            }
            return null;
        }
    }
    private final EconomyRegistry registry;
    private final NyEconomyAPI economy;
    private final String type;
    public NyEconomyCurrency(EconomyRegistry registry, NyEconomyAPI economy, String type) {
        this.registry = registry;
        this.economy = economy;
        this.type = type;
    }

    public static void register(EconomyRegistry registry) {
        registry.registerCurrency(new Provider(registry));
        registry.info("已挂钩 NyEconomy 经济");
    }

    @Override
    public String getCurrencyId() {
        return type;
    }

    @Override
    public String getName() {
        return registry.getCurrencyName(this);
    }

    @Override
    public String serialize() {
        return "NyEconomy:" + type;
    }

    @Override
    public double get(OfflinePlayer player) {
        return economy.getBalance(type, player.getUniqueId());
    }

    @Override
    public boolean has(OfflinePlayer player, double money) {
        return economy.getBalance(type, player.getUniqueId()) >= money;
    }

    @Override
    public boolean giveMoney(OfflinePlayer player, double money) {
        economy.withdraw(type, player.getUniqueId(), (int) money);
        return true;
    }

    @Override
    public boolean takeMoney(OfflinePlayer player, double money) {
        if (has(player, money)) {
            economy.deposit(type, player.getUniqueId(), (int) money);
            return true;
        }
        return false;
    }
}
