package top.mrxiaom.example.economy.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 经济实现提供器
 */
@FunctionalInterface
public interface ICurrencyProvider {
    /**
     * 优先级，数据值越小越先进行解析操作
     */
    default int priority() {
        return 1000;
    }

    /**
     * 从字符串解析经济接口实现，如果不符合格式或找不到经济实现，则返回 <code>null</code>
     */
    @Nullable ICurrency parse(@NotNull String str);
}
