package me.xeify.hub;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.File;
import java.io.IOException;

public class HubCommand extends Plugin {

    private Configuration config;

    @Override
    public void onEnable() {
        loadConfig();
        getProxy().getPluginManager().registerCommand(this, new Command("hub") {
            @Override
            public void execute(CommandSender sender, String[] args) {
                if (sender instanceof ProxiedPlayer) {
                    ProxiedPlayer player = (ProxiedPlayer) sender;
                    String serverName = config.getString("server", "Hub-1");
                    ServerInfo target = getProxy().getServerInfo(serverName);

                    if (target != null) {
                        player.connect(target);
                        String successMessage = ChatColor.translateAlternateColorCodes('&', config.getString("messages.success", "&aYou have been sent to &6" + serverName));
                        player.sendMessage(successMessage);
                    } else {
                        String errorMessage = ChatColor.translateAlternateColorCodes('&', config.getString("messages.error", "&cThe specified server could not be found."));
                        player.sendMessage(errorMessage);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                }
            }
        });
    }

    private void loadConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
                config.set("server", "Hub-1");
                config.set("messages.success", "&aYou have been sent to &6Hub-1");
                config.set("messages.error", "&cThe specified server could not be found.");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
