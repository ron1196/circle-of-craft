package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class LKRenderBug extends LKRenderLiving
{
	private ResourceLocation texture = new ResourceLocation("lionking:mob/bug.png");
	
    public LKRenderBug()
    {
        super(new LKModelTermite(), 0.3F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
