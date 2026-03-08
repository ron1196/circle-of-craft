package lionking.client;

import lionking.common.*;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraftforge.client.event.sound.*;
import net.minecraftforge.event.ForgeSubscribe;
import java.util.*;

public class LKSound
{
	private Random rand = new Random();
	private SoundPool music;
	
	@ForgeSubscribe
	public void playBackgroundMusic(PlayBackgroundMusicEvent event)
	{
		World world = Minecraft.getMinecraft().theWorld;
		if (world != null && LKIngame.isLKWorld(world.provider.dimensionId))
		{
			int randInt = rand.nextInt(100);
			if (randInt < mod_LionKing.lkMusicChance)
			{
				event.result = music.getRandomSound();
			}
		}
	}
	
	@ForgeSubscribe
	public void loadSounds(SoundLoadEvent event)
	{
		SoundManager soundmanager = event.manager;
		
		music = new SoundPool(Minecraft.getMinecraft().getResourceManager(), "music", true);
		
		registerMusic("The Circle of Life");
		registerMusic("I Just Can't Wait to Be King");
		registerMusic("Be Prepared");
		registerMusic("Hakuna Matata");
		registerMusic("Can You Feel the Love Tonight");
		
		registerSound(soundmanager, "lion");
		registerSound(soundmanager, "lionangry");
		registerSound(soundmanager, "lionroar");
		registerSound(soundmanager, "liondeath");
		
		registerSound(soundmanager, "zebra");
		registerSound(soundmanager, "zebrahurt");
		registerSound(soundmanager, "zebradeath");
	
		registerSound(soundmanager, "rhino");
		registerSound(soundmanager, "rhinohurt");
		registerSound(soundmanager, "rhinodeath");
		
		registerSound(soundmanager, "zazulive");
		registerSound(soundmanager, "zazuhurt");
		
		registerSound(soundmanager, "crocodile");
		registerSound(soundmanager, "crocodilesnap");
		registerSound(soundmanager, "crocodiledeath");
		
		registerSound(soundmanager, "vulture");
		registerSound(soundmanager, "vulturehurt");
		
		registerSound(soundmanager, "rafiki");
		registerSound(soundmanager, "rafiki1");
		registerSound(soundmanager, "rafiki2");
		registerSound(soundmanager, "rafiki3");
		
		registerSound(soundmanager, "flamingo");
		registerSound(soundmanager, "flamingo1");
		registerSound(soundmanager, "flamingohurt");
		registerSound(soundmanager, "flamingohurt1");
		registerSound(soundmanager, "flamingodeath");
		
		registerSound(soundmanager, "flatulence");
		registerSound(soundmanager, "bongo");
		registerSound(soundmanager, "pop");
	}
	
	private void registerSound(SoundManager soundmanager, String soundName)
	{
		soundmanager.soundPoolSounds.addSound("lionking:" + soundName + ".wav");
	}
	
	private void registerMusic(String soundName)
	{
		music.addSound("lionking:" + soundName + ".ogg");
	}
}
