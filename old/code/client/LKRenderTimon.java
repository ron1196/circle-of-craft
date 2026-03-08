package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class LKRenderTimon extends LKRenderLiving
{
	private ResourceLocation texture = new ResourceLocation("lionking:mob/timon.png");
	
    public LKRenderTimon()
    {
        super(new LKModelTimon(), 0.5F, "Timon");
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
