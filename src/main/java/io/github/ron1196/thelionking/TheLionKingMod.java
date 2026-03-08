package io.github.ron1196.thelionking;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(TheLionKingMod.MOD_ID)
public class TheLionKingMod {

    public static final String MOD_ID = "thelionking";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TheLionKingMod() {
        LOGGER.info("The Lion King Mod is loading!");
        MinecraftForge.EVENT_BUS.register(this);
    }
}
