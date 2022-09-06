package cn.starlight.wtf;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.argument;

public class Command {
    public static void registerAll(){
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("wtf")
                    .requires((source) -> source.hasPermissionLevel(4))
                    .then(CommandManager.literal("enable")
                            .then(argument("enable", BoolArgumentType.bool())
                                    .suggests(new CommandSuggestion("enable"))
                                    .executes((commandContext) ->
                                            executeCmd(commandContext.getSource(),  BoolArgumentType.getBool(commandContext, "enable"))
                                    )
                            )
                    )
                    .then(CommandManager.literal("distance")
                            .then(argument("distance", DoubleArgumentType.doubleArg(1.0))
                                    .suggests(new CommandSuggestion("distance"))
                                    .executes((commandContext) ->
                                            executeCmd(commandContext.getSource(), DoubleArgumentType.getDouble(commandContext, "distance"))
                                    )
                            )
                    )
                    .then(CommandManager.literal("reload")
                            .executes((commandContext) -> {
                                Main.CONF_INSTANCE.reloadConfig();
                                commandContext.getSource().sendFeedback(new LiteralText("[WTF] §a配置文件已重新加载!"), true);
                                return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                            })
                    )
                    .then(CommandManager.literal("help")
                            .executes((commandContext) -> sendHelpMessage(commandContext.getSource()))
                    )
                    .executes((commandContext) -> sendHelpMessage(commandContext.getSource()))
            );
        });
    }

    public static int sendHelpMessage(ServerCommandSource source){
        source.sendFeedback(new LiteralText("§b ======= §l[WTF | 帮助菜单]§r§b ======= \n§6- enable <true|false>: 开启/关闭本Mod功能\n§6- distance <最大距离>: 设定允许蜜蜂寻路回巢的最大水平距离（默认为256）\n§6- reload: 重载本Mod的配置文件"), true);
        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
    }

    public static int executeCmd(ServerCommandSource source, boolean enable){
        Main.CONF_INSTANCE.setEnable(enable);
        source.sendFeedback(new LiteralText(String.format("[WTF] §a功能开关已设定为：%s", enable)), true);

        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
    }

    public static int executeCmd(ServerCommandSource source, double distance){
        Main.CONF_INSTANCE.setMaxDistance(distance);
        source.sendFeedback(new LiteralText(String.format("[WTF] §a蜜蜂最大寻巢距离已设定为：%f", distance)), true);

        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
    }
}
