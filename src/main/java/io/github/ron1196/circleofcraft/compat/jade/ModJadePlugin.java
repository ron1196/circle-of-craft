package io.github.ron1196.circleofcraft.compat.jade;

import io.github.ron1196.circleofcraft.block.BugTrapBlock;
import io.github.ron1196.circleofcraft.block.GrindingBowlBlock;
import io.github.ron1196.circleofcraft.block.MountedShooterBlock;
import io.github.ron1196.circleofcraft.block.StarAltarBlock;
import io.github.ron1196.circleofcraft.block.entity.BugTrapBlockEntity;
import io.github.ron1196.circleofcraft.entity.animal.ModAnimal;
import io.github.ron1196.circleofcraft.entity.hostile.TermiteQueenEntity;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class ModJadePlugin implements IWailaPlugin {

    @Override
    public void register(@NotNull IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(TermiteQueenProvider.INSTANCE, TermiteQueenEntity.class);
        registration.registerEntityDataProvider(AnimalFavorProvider.INSTANCE, ModAnimal.class);
        registration.registerBlockDataProvider(BugTrapProvider.INSTANCE, BugTrapBlockEntity.class);
    }

    @Override
    public void registerClient(@NotNull IWailaClientRegistration registration) {
        registration.registerBlockComponent(GrindingBowlProvider.INSTANCE, GrindingBowlBlock.class);
        registration.registerBlockComponent(StarAltarProvider.INSTANCE, StarAltarBlock.class);
        registration.registerBlockComponent(BugTrapProvider.INSTANCE, BugTrapBlock.class);
        registration.registerBlockComponent(MountedShooterProvider.INSTANCE, MountedShooterBlock.class);
        registration.registerEntityComponent(TermiteQueenProvider.INSTANCE, TermiteQueenEntity.class);
        registration.registerEntityComponent(AnimalFavorProvider.INSTANCE, ModAnimal.class);
    }
}
