package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;

public class LKRenderZebra extends LKRenderLiving
{
	private static final ResourceLocation texture = new ResourceLocation("lionking:mob/zebra.png");
	
    public LKRenderZebra()
    {
        super(new LKModelZebra());
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return texture;
    }
}
