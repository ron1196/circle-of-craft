package io.github.ron1196.thelionking.entity.hostile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class OutlandessEntity extends OutlanderEntity {

    public OutlandessEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }
}
