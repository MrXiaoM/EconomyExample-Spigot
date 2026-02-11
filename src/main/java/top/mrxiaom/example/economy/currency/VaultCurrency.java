package top.mrxiaom.example.economy.currency;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.example.economy.EconomyRegistry;
import top.mrxiaom.example.economy.api.ICurrency;
import top.mrxiaom.example.economy.api.ICurrencyProvider;

public class VaultCurrency implements ICurrency {
    public static class Provider implements ICurrencyProvider {
        private final ICurrency currency;
        public Provider(ICurrency currency) {
            this.currency = currency;
        }

        @Override
        public @Nullable ICurrency parse(@NotNull String str) {
            if (str.equalsIgnoreCase("Vault")) {
                return currency;
            }
            return null;
        }
    }
    private final EconomyRegistry registry;
    private final Economy economy;
    public VaultCurrency(EconomyRegistry registry, Economy economy) {
        this.registry = registry;
        this.economy = economy;
    }

    public static void register(EconomyRegistry registry) {
        RegisteredServiceProvider<Economy> service = Bukkit.getServicesManager().getRegistration(Economy.class);
        Economy economy = service == null ? null : service.getProvider();
        if (economy != null) {
            ICurrency currency = new VaultCurrency(registry, economy);
            registry.registerCurrency(new Provider(currency));
            registry.info("已挂钩 Vault 经济 (" + economy.getName() + ")");
        } else {
            registry.warn("已发现 Vault 经济接口，但没有可用的经济服务，你可能未安装经济插件");
        }
    }

    @Override
    public String getCurrencyId() {
        return "Vault";
    }

    @Override
    public String getName() {
        return registry.getCurrencyName(this);
    }

    @Override
    public String serialize() {
        return "Vault";
    }

    @Override
    public double get(OfflinePlayer player) {
        return economy.getBalance(player);
    }

    @Override
    public boolean has(OfflinePlayer player, double money) {
        return economy.has(player, money);
    }

    @Override
    public boolean giveMoney(OfflinePlayer player, double money) {
        return economy.depositPlayer(player, money).transactionSuccess();
    }

    @Override
    public boolean takeMoney(OfflinePlayer player, double money) {
        if (has(player, money)) {
            return economy.withdrawPlayer(player, money).transactionSuccess();
        }
        return false;
    }
}
