package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class LKRenderGemsbok extends LKRenderLiving
{
	private ResourceLocation texture = new ResourceLocation("lionking:mob/gemsbok.png");
	
    public LKRenderGemsbok()
    {
        super(new LKModelGemsbok());
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
