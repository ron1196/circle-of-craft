package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import lionking.common.LKEntityDikdik;
import java.util.HashMap;

public class LKRenderDikdik extends LKRenderLiving
{
	private static HashMap textures = new HashMap();
	
    public LKRenderDikdik()
    {
        super(new LKModelDikdik(), 0.8F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        int i = ((LKEntityDikdik)entity).getDikdikType();
		if (textures.get(Integer.valueOf(i)) == null)
		{
			textures.put(Integer.valueOf(i), new ResourceLocation("lionking:mob/dikdik_" + i + ".png"));
		}
		return (ResourceLocation)textures.get(Integer.valueOf(i));
    }
}
