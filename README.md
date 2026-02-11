# EconomyExample-Spigot

Minecraft 经济插件对接示例

## 开始使用

[![](https://jitpack.io/v/MrXiaoM/EconomyExample-Spigot.svg)](https://jitpack.io/#MrXiaoM/EconomyExample-Spigot)
```kotlin
// build.gradle.kts
repositories { 
    maven("https://jitpack.io/")
}
dependencies {
    // 强烈建议 shadow 打包到你的插件 jar 包中，并进行 relocate
    implementation("com.github.MrXiaoM:EconomyExample-Spigot:$VERSION")
}
```

将货币管理器添加到插件主类示例如下：
```java
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import top.mrxiaom.example.economy.EconomyRegistry;
import top.mrxiaom.example.economy.name.MapName;

public class PluginMain extends JavaPlugin {
    private final EconomyRegistry economy = new EconomyRegistry(getLogger());
    private final MapName economyNames = new MapName();
    
    public EconomyRegistry economy() {
        return economy;
    }

    @Override
    public void onEnable() {
        // 注册自带的经济实现
        economy.registerBuiltInCurrencies();
        economy.registerName(economyNames);
        // ...
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        FileConfiguration config = getConfig();
        // 从配置文件的 currency-names 加载自定义货币名称
        economyNames.reload(config.getConfigurationSection("currency-names"));
    }
}
```

还需要添加默认配置文件：
```yaml
# config.yml
currency-names:
  Vault: 金币
  PlayerPoints: 点券
  MPoints:
    sign: 点数
  NyEconomy:
    type: 点数
```

最后，将需要支持的经济插件添加到 `plugin.yml` 中的 `softdepend` 声明：
```yaml
softdepend:
  - Vault
  - PlayerPoints
  - MPoints
  - CoinsEngine
  - NyEconomy
```

## 使用示例

```java
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import top.mrxiaom.example.economy.api.ICurrency;

void foo(OfflinePlayer player) {
    // 获取一个货币
    String currencyStr = "Vault";
    ICurrency currency = economy().parse(currencyStr);
    if (currency == null) {
        throw new IllegalArgumentException("未知的货币类型 " + currencyStr);
    }
    // 获取玩家货币数量
    double money = currency.get(player);
    // 给予玩家货币
    boolean giveSuccess = currency.giveMoney(player, 100);
    // 拿走玩家货币
    boolean takeSuccess = currency.takeMoney(player, 100);
    
    // 扣除货币给予钻石示例
    if (currency.takeMoney(player, 50)) {
        player.getInventory().addItem(new ItemStack(Material.DIAMOND));
        player.sendMessage("你购买了一个钻石");
    } else {
        player.sendMessage("你没有足够的金钱购买这个商品");
    }
}
```

## 货币支持

目前内置以下货币插件支持：
+ `"Vault"` - Vault 金币
+ `"PlayerPoints"` - PlayerPoints 点券
+ `"MPoints:点数类型"` - MPoints 点数
+ `"CoinsEngine:货币类型"` - CoinsEngine 货币
+ `"NyEconomy:货币类型"` - NyEconomy 货币
