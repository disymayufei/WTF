package cn.starlight.wtf;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {

    public static Config CONF_INSTANCE = new Config();

    @Override
    public void onInitialize() {
        Command.registerAll();
        Logger.info("蜜蜂防寻巢卡顿加载成功，Author by Disy!");
    }
}
