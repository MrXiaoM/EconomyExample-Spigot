package top.mrxiaom.example.economy.currency;

import me.yic.mpoints.MPointsAPI;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.example.economy.EconomyRegistry;
import top.mrxiaom.example.economy.api.ICurrency;
import top.mrxiaom.example.economy.api.ICurrencyProvider;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MPointsCurrency implements ICurrency {
    public static class Provider implements ICurrencyProvider {
        private final MPointsAPI economy = new MPointsAPI();
        private final Map<String, ICurrency> cache = new HashMap<>();
        private final EconomyRegistry registry;
        public Provider(EconomyRegistry registry) {
            this.registry = registry;
        }

        @Override
        public @Nullable ICurrency parse(@NotNull String str) {
            if (str.startsWith("MPoints:")) {
                String sign = str.substring(8);
                ICurrency exists = cache.get(sign);
                if (exists != null) return exists;
                MPointsCurrency currency = new MPointsCurrency(registry, economy, sign);
                cache.put(sign, currency);
                return currency;
            }
            return null;
        }
    }
    private final EconomyRegistry registry;
    private final MPointsAPI economy;
    private final String sign;
    public MPointsCurrency(EconomyRegistry registry, MPointsAPI economy, String sign) {
        this.registry = registry;
        this.economy = economy;
        this.sign = sign;
    }

    public static void register(EconomyRegistry registry) {
        registry.registerCurrency(new Provider(registry));
        registry.info("已挂钩 MPoints 经济");
    }

    @Override
    public String getCurrencyId() {
        return sign;
    }

    @Override
    public String getName() {
        return registry.getCurrencyName(this);
    }

    @Override
    public String serialize() {
        return "MPoints:" + sign;
    }

    @Override
    public double get(OfflinePlayer player) {
        return economy.getbalance(sign, player.getUniqueId()).doubleValue();
    }

    @Override
    public boolean has(OfflinePlayer player, double money) {
        return get(player) >= money;
    }

    @Override
    public boolean giveMoney(OfflinePlayer player, double money) {
        return economy.changebalance(sign, player.getUniqueId(), player.getName(), BigDecimal.valueOf(money), true) == 0;
    }

    @Override
    public boolean takeMoney(OfflinePlayer player, double money) {
        if (has(player, money)) {
            return economy.changebalance(sign, player.getUniqueId(), player.getName(), BigDecimal.valueOf(money), false) == 0;
        }
        return false;
    }
}
