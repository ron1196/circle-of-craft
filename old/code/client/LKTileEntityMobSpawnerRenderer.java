package lionking.client;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import lionking.common.LKTileEntityMobSpawner;
import org.lwjgl.opengl.GL11;

public class LKTileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer
{
    public void renderTileEntityMobSpawner(LKTileEntityMobSpawner tileentity, double d, double d1, double d2, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d + 0.5F, (float)d1, (float)d2 + 0.5F);
        Entity entity = tileentity.getMobEntity();

        if (entity != null)
        {
            entity.setWorld(tileentity.getWorldObj());
            float f1 = 0.4375F;
            GL11.glTranslatef(0.0F, 0.4F, 0.0F);
            GL11.glRotatef((float)(tileentity.yaw2 + (tileentity.yaw - tileentity.yaw2) * (double)f) * 10.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.4F, 0.0F);
            GL11.glScalef(f1, f1, f1);
            entity.setLocationAndAngles(d, d1, d2, 0.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, f);
        }

        GL11.glPopMatrix();
    }

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderTileEntityMobSpawner((LKTileEntityMobSpawner)tileentity, d, d1, d2, f);
    }
}
