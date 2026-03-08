package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class LKRenderCrocodile extends LKRenderLiving
{
	private static ResourceLocation texture = new ResourceLocation("lionking:mob/crocodile.png");
	
    public LKRenderCrocodile()
    {
        super(new LKModelCrocodile(), 0.75F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
