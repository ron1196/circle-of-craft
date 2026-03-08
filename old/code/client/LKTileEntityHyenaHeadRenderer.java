package lionking.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import lionking.common.LKTileEntityHyenaHead;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;

public class LKTileEntityHyenaHeadRenderer extends TileEntitySpecialRenderer
{
	private ModelBase model = new LKModelHyenaHead(false);
	private static HashMap textures = new HashMap();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
	{
		LKTileEntityHyenaHead head = (LKTileEntityHyenaHead)tileentity;
		renderHead((float)d, (float)d1, (float)d2, head.getBlockMetadata(), head.getRotation() * 360 / 16.0F, head.getHyenaType());
	}

	private void renderHead(float f, float f1, float f2, int metadata, float rotation, int hyenaType)
	{
		if (hyenaType == 3)
		{
			bindTexture(LKRenderHyena.textureHyenaSkeleton);
		}
		else
		{
			if (textures.get(Integer.valueOf(hyenaType)) == null)
			{
				textures.put(Integer.valueOf(hyenaType), new ResourceLocation("lionking:mob/hyena_" + hyenaType + ".png"));
			}
			bindTexture((ResourceLocation)textures.get(Integer.valueOf(hyenaType)));
		}

		GL11.glPushMatrix();
		GL11.glDisable(2884);

		if (metadata != 1)
		{
			switch (metadata)
			{
				case 2:
					GL11.glTranslatef(f + 0.5F, f1 + 0.25F, f2 + 0.81F);
					break;
				case 3:
					GL11.glTranslatef(f + 0.5F, f1 + 0.25F, f2 + 0.19F);
					rotation = 180.0F;
					break;
				case 4:
					GL11.glTranslatef(f + 0.81F, f1 + 0.25F, f2 + 0.5F);
					rotation = 270.0F;
					break;
				case 5:
				default:
					GL11.glTranslatef(f + 0.19F, f1 + 0.25F, f2 + 0.5F);
					rotation = 90.0F;
					break;
			}
		}
		else
		{
			GL11.glTranslatef(f + 0.5F, f1, f2 + 0.5F);
		}

		GL11.glEnable(32826);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);

		GL11.glEnable(3008);

		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		GL11.glPopMatrix();
	}
}
