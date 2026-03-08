package lionking.client;
import lionking.common.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.*;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.*;
import net.minecraft.server.*;
import net.minecraft.server.management.*;
import net.minecraft.src.*;
import net.minecraft.stats.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.*;
import net.minecraft.client.*;
import net.minecraft.client.audio.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.model.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.tileentity.*;
import java.util.HashMap;

import net.minecraft.src.*;

public class LKRenderGiraffe extends RenderLiving
{
	private static ResourceLocation texture = new ResourceLocation("lionking:mob/giraffe/giraffe.png");
	
	private static ResourceLocation saddleTexture = new ResourceLocation("lionking:mob/giraffe/saddle.png");
	private static String[] tieNames = {"tie", "tie_white", "tie_blue", "tie_yellow", "tie_red", "tie_purple", "tie_green", "tie_black"};
	private static HashMap tieTextures = new HashMap();
	
    public LKRenderGiraffe(ModelBase model, ModelBase model1)
    {
        super(model, 0.5F);
        setRenderPassModel(model1);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
	
	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int i, float f)
    {
		if (i == 0 && ((LKEntityGiraffe)entity).getSaddled())
		{
			bindTexture(saddleTexture);
			return 1;
		}
		else if (i == 1 && ((LKEntityGiraffe)entity).getTie() > -1)
		{
			String s = tieNames[((LKEntityGiraffe)entity).getTie()];
			if (tieTextures.get(s) == null)
			{
				tieTextures.put(s, new ResourceLocation("lionking:mob/giraffe/" + s + ".png"));
			}
			ResourceLocation r = (ResourceLocation)tieTextures.get(s);
			bindTexture(r);
			((LKModelGiraffe)renderPassModel).tie.showModel = true;
			return 1;
		}
		return -1;
    }
}
