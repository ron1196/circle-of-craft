package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import lionking.common.*;

public class LKRenderLion extends LKRenderLiving
{
	private ResourceLocation textureLion = new ResourceLocation("lionking:mob/lion.png");
	private ResourceLocation textureLioness = new ResourceLocation("lionking:mob/lioness.png");
	
    public LKRenderLion()
    {
        super(new LKModelLion());
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return entity instanceof LKEntityLioness ? textureLioness : textureLion;
    }
}
