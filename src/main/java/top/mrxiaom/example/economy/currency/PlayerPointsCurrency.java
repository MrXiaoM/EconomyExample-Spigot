package top.mrxiaom.example.economy.currency;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.example.economy.EconomyRegistry;
import top.mrxiaom.example.economy.api.ICurrency;
import top.mrxiaom.example.economy.api.ICurrencyProvider;

public class PlayerPointsCurrency implements ICurrency {
    public static class Provider implements ICurrencyProvider {
        private final ICurrency currency;
        public Provider(ICurrency currency) {
            this.currency = currency;
        }

        @Override
        public @Nullable ICurrency parse(@NotNull String str) {
            if (str.equalsIgnoreCase("PlayerPoints")) {
                return currency;
            }
            return null;
        }
    }
    private final EconomyRegistry registry;
    private final PlayerPointsAPI economy;
    public PlayerPointsCurrency(EconomyRegistry registry, PlayerPointsAPI economy) {
        this.registry = registry;
        this.economy = economy;
    }

    public static void register(EconomyRegistry registry) {
        PlayerPointsAPI economy = PlayerPoints.getInstance().getAPI();
        ICurrency currency = new PlayerPointsCurrency(registry, economy);
        registry.registerCurrency(new Provider(currency));
        registry.info("已挂钩 PlayerPoints 经济");
    }

    @Override
    public String getCurrencyId() {
        return "PlayerPoints";
    }

    @Override
    public String getName() {
        return registry.getCurrencyName(this);
    }

    @Override
    public String serialize() {
        return "PlayerPoints";
    }

    @Override
    public double get(OfflinePlayer player) {
        return economy.look(player.getUniqueId());
    }

    @Override
    public boolean has(OfflinePlayer player, double money) {
        return get(player) >= money;
    }

    @Override
    public boolean giveMoney(OfflinePlayer player, double money) {
        return economy.give(player.getUniqueId(), (int) money);
    }

    @Override
    public boolean takeMoney(OfflinePlayer player, double money) {
        if (has(player, money)) {
            return economy.take(player.getUniqueId(), (int) money);
        }
        return false;
    }
}
