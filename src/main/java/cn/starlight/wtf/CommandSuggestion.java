package cn.starlight.wtf;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

public class CommandSuggestion implements SuggestionProvider<ServerCommandSource> {

    private final String TYPE;

    CommandSuggestion(String type){
        this.TYPE = type;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {

        switch (this.TYPE) {
            case "enable" -> {
                builder.suggest("true");
                builder.suggest("false");

                return builder.buildFuture();
            }
            case "distance" -> {
                builder.suggest(32);
                builder.suggest(64);
                builder.suggest(128);

                return builder.buildFuture();
            }
            default -> {
                return builder.buildFuture();
            }
        }
    }
}
