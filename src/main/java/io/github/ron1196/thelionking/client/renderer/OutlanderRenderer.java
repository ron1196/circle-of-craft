package io.github.ron1196.thelionking.client.renderer;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.client.model.OutlanderModel;
import io.github.ron1196.thelionking.entity.hostile.OutlanderEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class OutlanderRenderer
        extends net.minecraft.client.renderer.entity.MobRenderer<OutlanderEntity, OutlanderModel<OutlanderEntity>> {

    private static final ResourceLocation MALE_TEXTURE =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/outlander_male.png");
    private static final ResourceLocation FEMALE_TEXTURE =
            new ResourceLocation(TheLionKingMod.MOD_ID, "textures/entity/outlander_female.png");

    private static final float MALE_SHADOW = 0.7F;
    private static final float FEMALE_SHADOW = 0.6F;

    public OutlanderRenderer(EntityRendererProvider.Context context, OutlanderModel<OutlanderEntity> model) {
        super(context, model, MALE_SHADOW);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull OutlanderEntity entity) {
        this.shadowRadius = entity.isFemale() ? FEMALE_SHADOW : MALE_SHADOW;
        return entity.isFemale() ? FEMALE_TEXTURE : MALE_TEXTURE;
    }
}
