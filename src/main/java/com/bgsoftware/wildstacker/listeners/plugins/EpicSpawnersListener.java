package com.bgsoftware.wildstacker.listeners.plugins;

import com.bgsoftware.wildstacker.WildStackerPlugin;
import com.bgsoftware.wildstacker.api.objects.StackedEntity;
import com.bgsoftware.wildstacker.api.objects.StackedSpawner;
import com.bgsoftware.wildstacker.objects.WStackedEntity;
import com.bgsoftware.wildstacker.objects.WStackedSpawner;
import com.bgsoftware.wildstacker.utils.Executor;
import com.songoda.epicspawners.api.events.SpawnerSpawnEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@SuppressWarnings("unused")
public final class EpicSpawnersListener implements Listener {

    private static boolean enabled = false;

    public EpicSpawnersListener(WildStackerPlugin plugin){
        try {
            Class.forName("com.songoda.epicspawners.api.events.SpawnerSpawnEvent");
            enabled = true;
        }catch(Exception ignored){}
    }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent e){
        if(!(e.getEntity() instanceof LivingEntity))
            return;

        StackedEntity stackedEntity = WStackedEntity.of(e.getEntity());

        if(!stackedEntity.isWhitelisted() || stackedEntity.isBlacklisted() || stackedEntity.isWorldDisabled())
            return;

        StackedSpawner stackedSpawner = WStackedSpawner.of(e.getSpawner().getCreatureSpawner());

        //It takes 1 tick for EpicSpawners to set the metadata for the mobs.
        Executor.sync(() -> stackedEntity.trySpawnerStack(stackedSpawner), 2L);
    }

    public static boolean isEnabled(){
        return enabled;
    }

}
