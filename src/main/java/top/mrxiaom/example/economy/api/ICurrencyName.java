package top.mrxiaom.example.economy.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 为经济实现提供自定义名称
 */
public interface ICurrencyName {
    default int priority() {
        return 1000;
    }
    @Nullable String getName(@NotNull ICurrency currency);
}
