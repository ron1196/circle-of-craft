package lionking.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import lionking.common.LKIngame;

public class LKRenderTicketLion extends LKRenderLiving
{
	private static final ResourceLocation texture = new ResourceLocation("lionking:mob/ticketlion.png");
	private static final ResourceLocation textureChristmas = new ResourceLocation("lionking:mob/ticketlion_christmas.png");
	
    public LKRenderTicketLion()
    {
        super(new LKModelLion(), "Ticket Lion");
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return LKIngame.isChristmas() ? textureChristmas : texture;
    }
}
