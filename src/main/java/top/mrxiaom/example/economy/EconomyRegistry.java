package top.mrxiaom.example.economy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.example.economy.api.ICurrency;
import top.mrxiaom.example.economy.api.ICurrencyName;
import top.mrxiaom.example.economy.api.ICurrencyProvider;
import top.mrxiaom.example.economy.currency.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EconomyRegistry {
    private final Logger logger;
    private final List<ICurrencyProvider> currencyRegistry = new ArrayList<>();
    private final List<ICurrencyName> nameRegistry = new ArrayList<>();
    public EconomyRegistry(Logger logger) {
        this.logger = logger;
    }

    /**
     * 注册内置的货币提供器
     */
    public void registerBuiltInCurrencies() {
        if (isPresent("net.milkbowl.vault.economy.Economy")) {
            VaultCurrency.register(this);
        }
        if (isPresent("org.black_ixx.playerpoints.PlayerPointsAPI")) {
            PlayerPointsCurrency.register(this);
        }
        if (isPresent("me.yic.mpoints.MPointsAPI")) {
            MPointsCurrency.register(this);
        }
        if (isPresent("su.nightexpress.coinsengine.api.CoinsEngineAPI")) {
            CoinsEngineCurrency.register(this);
        }
        if (isPresent("com.mc9y.nyeconomy.api.NyEconomyAPI")) {
            NyEconomyCurrency.register(this);
        }
    }

    /**
     * 注册自定义货币插件
     */
    public void registerCurrency(@NotNull ICurrencyProvider provider) {
        currencyRegistry.add(provider);
        currencyRegistry.sort(Comparator.comparingInt(ICurrencyProvider::priority));
    }

    /**
     * 注销自定义货币插件
     */
    public void unregisterCurrency(@NotNull ICurrencyProvider provider) {
        currencyRegistry.remove(provider);
        currencyRegistry.sort(Comparator.comparingInt(ICurrencyProvider::priority));
    }

    /**
     * 注销所有自定义货币插件
     */
    public void unregisterAllCurrency() {
        currencyRegistry.clear();
    }

    /**
     * 获取所有自定义货币插件提供器
     */
    @NotNull
    public List<ICurrencyProvider> getCurrencyRegistry() {
        return Collections.unmodifiableList(currencyRegistry);
    }

    /**
     * 注册货币名称提供器
     */
    public void registerName(@NotNull ICurrencyName provider) {
        nameRegistry.add(provider);
        nameRegistry.sort(Comparator.comparingInt(ICurrencyName::priority));
    }

    /**
     * 注销货币名称提供器
     */
    public void unregisterName(@NotNull ICurrencyName provider) {
        nameRegistry.remove(provider);
        nameRegistry.sort(Comparator.comparingInt(ICurrencyName::priority));
    }

    /**
     * 注销所有货币名称提供器
     */
    public void unregisterAllName() {
        nameRegistry.clear();
    }

    /**
     * 获取所有货币名称提供器
     */
    @NotNull
    public List<ICurrencyName> getNameRegistry() {
        return Collections.unmodifiableList(nameRegistry);
    }

    /**
     * 通过字符串解析一个货币实现
     * @param str 输入的字符串
     * @return 如果找不到货币，返回 <code>null</code>
     */
    @Nullable
    public ICurrency parse(@Nullable String str) {
        if (str == null) return null;
        for (ICurrencyProvider provider : currencyRegistry) {
            ICurrency currency = provider.parse(str);
            if (currency != null) {
                return currency;
            }
        }
        return null;
    }

    /**
     * 通过货币实现，获取自定义货币名称
     * @param currency 货币实现
     * @return 自定义货币名称，如果未进行配置，则返回货币 ID 作为缺省值
     */
    @NotNull
    public String getCurrencyName(@NotNull ICurrency currency) {
        for (ICurrencyName provider : nameRegistry) {
            String name = provider.getName(currency);
            if (name != null) {
                return name;
            }
        }
        return currency.getCurrencyId();
    }

    @NotNull
    public Logger getLogger() {
        return logger;
    }

    public void info(String message) {
        logger.log(Level.INFO, message);
    }

    public void warn(String message) {
        logger.log(Level.WARNING, message);
    }

    public void warn(String message, Throwable throwable) {
        logger.log(Level.WARNING, message, throwable);
    }

    public void error(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }

    /**
     * 检查类是否存在
     */
    public static boolean isPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}
