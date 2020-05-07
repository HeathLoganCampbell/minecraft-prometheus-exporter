package de.sldk.mc.metrics;

import com.google.common.collect.Maps;
import io.prometheus.client.Gauge;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class PlayersOnlineTotal extends Metric {

    private static final Gauge PLAYERS_ONLINE = Gauge.build()
            .name(prefix("players_online_total"))
            .help("Players currently online per world")
            .labelNames("world")
            .create();

    public PlayersOnlineTotal(Plugin plugin) {
        super(plugin, PLAYERS_ONLINE);
    }


    @Override
    public final void doCollect() {
        collect();
    }

    public void collect()
    {
        //Scan all online players, so we don't count ncp's as players
        Map<String, Integer> playerCountPerWorld = Maps.newHashMap();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            String worldName = onlinePlayer.getWorld().getName();
            Integer playerCount = playerCountPerWorld.getOrDefault(worldName, 0);

            playerCount++;

            playerCountPerWorld.put(worldName, playerCount);
        }

        playerCountPerWorld.forEach((worldName, playerCount) -> {
            PLAYERS_ONLINE.labels(worldName).set(playerCount);
        });
    }
}
