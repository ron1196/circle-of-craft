package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class LKRenderPumbaa extends LKRenderLiving
{
	private ResourceLocation texture = new ResourceLocation("lionking:mob/pumbaa.png");
	
    public LKRenderPumbaa()
    {
        super(new LKModelPumbaa(), "Pumbaa");
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
