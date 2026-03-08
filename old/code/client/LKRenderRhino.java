package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class LKRenderRhino extends LKRenderLiving
{
	private ResourceLocation texture = new ResourceLocation("lionking:mob/rhino.png");
	
    public LKRenderRhino()
    {
        super(new LKModelRhino(), 1.2F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
