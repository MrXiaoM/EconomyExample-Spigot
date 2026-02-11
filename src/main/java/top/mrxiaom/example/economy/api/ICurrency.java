package top.mrxiaom.example.economy.api;

import org.bukkit.OfflinePlayer;

/**
 * 经济插件抽象接口
 */
public interface ICurrency {
    /**
     * 获取货币 ID，用于在货币插件不支持获取展示名称时，为 {@link ICurrencyName} 提供器获取自定义名称提供货币 ID
     */
    String getCurrencyId();

    /**
     * 获取经济接口展示名，通过经济插件自带的显示名称，或者通过 {@link ICurrencyName} 提供器获取
     */
    String getName();

    /**
     * 将经济接口实现序列化为字符串，这个字符串能够通过 {@link ICurrencyProvider} 提供器重新获取相同的 ICurrency 实例
     */
    String serialize();

    /**
     * 获取玩家货币数量
     * @param player 玩家
     */
    double get(OfflinePlayer player);

    /**
     * 获取玩家是否有足够的货币
     * @param player 玩家
     * @param money 货币数量
     */
    boolean has(OfflinePlayer player, double money);

    /**
     * 给予玩家货币
     * @param player 玩家
     * @param money 货币数量
     * @return 是否操作成功
     */
    boolean giveMoney(OfflinePlayer player, double money);

    /**
     * 从玩家那里拿走货币
     * @param player 玩家
     * @param money 货币数量
     * @return 是否操作成功
     */
    boolean takeMoney(OfflinePlayer player, double money);
}
