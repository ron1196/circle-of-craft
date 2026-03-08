package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class LKRenderScar extends LKRenderLiving
{
	private static final ResourceLocation texture = new ResourceLocation("lionking:mob/scar.png");
	
    public LKRenderScar()
    {
        super(new LKModelLion(true), "Scar");
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
