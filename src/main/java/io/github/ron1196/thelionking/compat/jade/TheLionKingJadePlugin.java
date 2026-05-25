package io.github.ron1196.thelionking.compat.jade;

import io.github.ron1196.thelionking.block.BugTrapBlock;
import io.github.ron1196.thelionking.block.GrindingBowlBlock;
import io.github.ron1196.thelionking.block.MountedShooterBlock;
import io.github.ron1196.thelionking.block.SpawnerBlock;
import io.github.ron1196.thelionking.block.StarAltarBlock;
import io.github.ron1196.thelionking.block.entity.BugTrapBlockEntity;
import io.github.ron1196.thelionking.block.entity.SpawnerBlockEntity;
import io.github.ron1196.thelionking.entity.animal.LionKingAnimal;
import io.github.ron1196.thelionking.entity.hostile.TermiteQueenEntity;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class TheLionKingJadePlugin implements IWailaPlugin {

    @Override
    public void register(@NotNull IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(TermiteQueenProvider.INSTANCE, TermiteQueenEntity.class);
        registration.registerEntityDataProvider(AnimalFavorProvider.INSTANCE, LionKingAnimal.class);
        registration.registerBlockDataProvider(SpawnerProvider.INSTANCE, SpawnerBlockEntity.class);
        registration.registerBlockDataProvider(BugTrapProvider.INSTANCE, BugTrapBlockEntity.class);
    }

    @Override
    public void registerClient(@NotNull IWailaClientRegistration registration) {
        registration.registerBlockComponent(GrindingBowlProvider.INSTANCE, GrindingBowlBlock.class);
        registration.registerBlockComponent(StarAltarProvider.INSTANCE, StarAltarBlock.class);
        registration.registerBlockComponent(BugTrapProvider.INSTANCE, BugTrapBlock.class);
        registration.registerBlockComponent(MountedShooterProvider.INSTANCE, MountedShooterBlock.class);
        registration.registerBlockComponent(SpawnerProvider.INSTANCE, SpawnerBlock.class);
        registration.registerEntityComponent(TermiteQueenProvider.INSTANCE, TermiteQueenEntity.class);
        registration.registerEntityComponent(AnimalFavorProvider.INSTANCE, LionKingAnimal.class);
    }
}
