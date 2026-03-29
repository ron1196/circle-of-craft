package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.client.gui.QuestBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuestBookClientHelper {
    public static void openScreen() {
        Minecraft.getInstance().setScreen(new QuestBookScreen());
    }
}
