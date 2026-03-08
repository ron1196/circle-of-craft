package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class LKRenderTermite extends LKRenderLiving
{
	private ResourceLocation texture = new ResourceLocation("lionking:mob/termite.png");
	
    public LKRenderTermite()
    {
        super(new LKModelTermite(), 0.3F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
