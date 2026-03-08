package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import lionking.common.*;
import java.util.HashMap;

public class LKRenderHyena extends LKRenderLiving
{
	private static HashMap textures = new HashMap();
	public static final ResourceLocation textureHyenaSkeleton = new ResourceLocation("lionking:mob/hyena_skeleton.png");
	
    public LKRenderHyena()
    {
        super(new LKModelHyena());
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        if (entity instanceof LKEntitySkeletalHyena)
		{
			return textureHyenaSkeleton;
		}
		else
		{
			int i = ((LKEntityHyena)entity).getHyenaType();
			if (textures.get(Integer.valueOf(i)) == null)
			{
				textures.put(Integer.valueOf(i), new ResourceLocation("lionking:mob/hyena_" + i + ".png"));
			}
			return (ResourceLocation)textures.get(Integer.valueOf(i));
		}
    }
}
