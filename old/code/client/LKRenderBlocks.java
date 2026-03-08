package lionking.client;
import lionking.common.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.*;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.*;
import net.minecraft.server.*;
import net.minecraft.server.management.*;
import net.minecraft.src.*;
import net.minecraft.stats.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.*;
import net.minecraft.client.*;
import net.minecraft.client.audio.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.model.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.tileentity.*;


import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.client.ForgeHooksClient;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class LKRenderBlocks implements ISimpleBlockRenderingHandler
{
	private boolean renderInvIn3D;
	
	public LKRenderBlocks(boolean flag)
	{
		renderInvIn3D = flag;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int i, int j, int k, Block block, int id, RenderBlocks renderblocks)
	{
		if (id == mod_LionKing.proxy.getGrindingBowlRenderID())
		{
			renderGrindingBowl(renderblocks, block, i, j, k);
			return true;
		}
		if (id == mod_LionKing.proxy.getPillarRenderID())
		{
			renderPillar(renderblocks, world, block, i, j, k);
			return true;
		}
		if (id == mod_LionKing.proxy.getStarAltarRenderID())
		{
			renderStarAltar(renderblocks, block, i, j, k);
			return true;
		}
		if (id == mod_LionKing.proxy.getVaseRenderID())
		{
			renderVase(renderblocks, world, block, i, j, k);
			return true;
		}
		if (id == mod_LionKing.proxy.getBugTrapRenderID())
		{
			renderBugTrap(renderblocks, world, block, i, j, k);
			return true;
		}
		if (id == mod_LionKing.proxy.getAridGrassRenderID())
		{
			renderAridGrass(renderblocks, world, block, i, j, k);
			return true;
		}
		if (id == mod_LionKing.proxy.getKiwanoBlockRenderID())
		{
			renderKiwano(renderblocks, block, i, j, k);
			return true;
		}
		if (id == mod_LionKing.proxy.getKiwanoStemRenderID())
		{
			renderKiwanoStem(renderblocks, world, block, i, j, k);
			return true;
		}
		if (id == mod_LionKing.proxy.getLeverRenderID())
		{
			renderLever(renderblocks, block, i, j, k);
			return true;
		}
		if (id == mod_LionKing.proxy.getLilyRenderID())
		{
			renderLily(renderblocks, world, block, i, j, k);
			return true;
		}
		return false;
	}
	
	@Override
    public void renderInventoryBlock(Block block, int metadata, int id, RenderBlocks renderblocks)
	{
		if (id == mod_LionKing.proxy.getPillarRenderID())
		{
			renderInvPillar(renderblocks, block, metadata);
		}
		if (id == mod_LionKing.proxy.getStarAltarRenderID())
		{
			renderInvStarAltar(renderblocks, block);
		}
		if (id == mod_LionKing.proxy.getBugTrapRenderID())
		{
			renderInvBugTrap(renderblocks, block);
		}
		if (id == mod_LionKing.proxy.getKiwanoBlockRenderID())
		{
			renderInvKiwano(renderblocks, block);
		}
	}
	
	@Override
	public boolean shouldRender3DInInventory()
	{
		return renderInvIn3D;
	}
	
	@Override
	public int getRenderId()
	{
		return 0;
	}
	
	private void renderGrindingBowl(RenderBlocks renderblocks, Block block, int i, int j, int k)
	{
		renderblocks.setRenderBounds(0.0F, 0.25F, 0.0F, 1.0F, 0.625F, 0.1875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		
		renderblocks.setRenderBounds(0.0F, 0.25F, 0.8125F, 1.0F, 0.625F, 1.0F);
		renderblocks.renderStandardBlock(block, i, j, k);
		
		renderblocks.setRenderBounds(0.0F, 0.25F, 0.1875F, 0.1875F, 0.625F, 0.8125F);
		renderblocks.renderStandardBlock(block, i, j, k);
		
		renderblocks.setRenderBounds(0.8125F, 0.25F, 0.1875F, 1.0F, 0.625F, 0.8125F);
		renderblocks.renderStandardBlock(block, i, j, k);
		
		renderblocks.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
		renderblocks.renderStandardBlock(block, i, j, k);

		renderblocks.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
	}
	
	private void renderPillar(RenderBlocks renderblocks, IBlockAccess world, Block block, int i, int j, int k)
	{
		int l = world.getBlockMetadata(i, j, k);
		if (l > 3 && l < 8)
		{
			l -= 4;
		}
		float f = 0.125F * (float)l;
		renderblocks.setRenderBounds(0.0F + f, 0.0F, 0.0F + f, 1.0F - f, 1.0F, 1.0F - f);
		renderblocks.renderStandardBlock(block, i, j, k);
	}
	
	private void renderInvPillar(RenderBlocks renderblocks, Block block, int i)
	{
		int j = i;
		if (i > 3 && i < 8)
		{
			j -= 4;
		}
		float f = 0.125F * (float)j;
		renderStandardInvBlock(renderblocks, block, 0.0F + f, 0.0F, 0.0F + f, 1.0F - f, 1.0F, 1.0F - f, i);
	}
	
	private void renderStarAltar(RenderBlocks renderblocks, Block block, int i, int j, int k)
	{
		renderblocks.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		renderblocks.renderStandardBlock(block, i, j, k);
		
		renderblocks.setRenderBounds(0.125F, 0.625F, 0.125F, 0.875F, 0.75F, 0.875F);
		renderblocks.renderStandardBlock(block, i, j, k);

		renderblocks.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
	}
	
	private void renderInvStarAltar(RenderBlocks renderblocks, Block block)
	{
		renderStandardInvBlock(renderblocks, block, 0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		renderStandardInvBlock(renderblocks, block, 0.125F, 0.625F, 0.125F, 0.875F, 0.75F, 0.875F);
		renderblocks.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
	}

	private void renderVase(RenderBlocks renderblocks, IBlockAccess world, Block block, int i, int j, int k)
	{
		int l = world.getBlockMetadata(i, j, k);
		float f = 0.1875F;
		renderblocks.setRenderBounds(0.0F + f, 0.0F, 0.0F + f, 1.0F - f, 0.75F, 1.0F - f);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.clearOverrideBlockTexture();
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, i, j, k));
        float f1 = (float)(0xffffff >> 16 & 0xff) / 255F;
        float f2 = (float)(0xffffff >> 8 & 0xff) / 255F;
        float f3 = (float)(0xffffff & 0xff) / 255F;
        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f6 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }
        tessellator.setColorOpaque_F(1.0F * f1, 1.0F * f2, 1.0F * f3);
        double d = i;
        double d1 = j + 0.71875D;
        double d2 = k;
        Icon icon = LKBlockVase.getPlantTextureFromMetadata(l);
        renderblocks.overrideBlockTexture = icon;
        renderblocks.drawCrossedSquares(block, 0, d, d1, d2, 1F);
		renderblocks.clearOverrideBlockTexture();
    }
	
	private void renderBugTrap(RenderBlocks renderblocks, IBlockAccess world, Block block, int i, int j, int k)
	{
		renderblocks.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.0F, 0.9375F, 0.0F, 1.0F, 1.0F, 1.0F);
		renderblocks.renderStandardBlock(block, i, j, k);
		
		for (int i1 = 0; i1 < 2; i1++)
		{
			for (int i2 = 0; i2 < 2; i2++)
			{
				boolean flag1 = i1 == 0;
				boolean flag2 = i2 == 0;
				renderblocks.setRenderBounds(flag1 ? 0.0F : 0.875F, 0.0625F, flag2 ? 0.0F : 0.875F, flag1 ? 0.125F : 1.0F, 0.9375F, flag2 ? 0.125F : 1.0F);
				renderblocks.renderStandardBlock(block, i, j, k);
			}
		}
		
		renderblocks.overrideBlockTexture = mod_LionKing.planks.getIcon(2, 0);
		renderblocks.setRenderBounds(0.46875F, 0.4375F, 0.46875F, 0.53125F, 0.9375F, 0.53125F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.clearOverrideBlockTexture();
		
		renderblocks.overrideBlockTexture = mod_LionKing.blockSilver.getIcon(2, 0);
		float f = 0.3125F * (((float)world.getBlockMetadata(i, j, k)) / 8);
		
		renderblocks.setRenderBounds(0.125F, 0.0625F, 0.06F, 0.1875F + f, 0.9375F, 0.07F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.8125F - f, 0.0625F, 0.06F, 0.875F, 0.9375F, 0.07F);
		renderblocks.renderStandardBlock(block, i, j, k);

		renderblocks.setRenderBounds(0.125F, 0.0625F, 0.94F, 0.1875F + f, 0.9375F, 0.93F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.8125F - f, 0.0625F, 0.94F, 0.875F, 0.9375F, 0.93F);
		renderblocks.renderStandardBlock(block, i, j, k);
		
		renderblocks.setRenderBounds(0.06F, 0.0625F, 0.125F, 0.07F, 0.9375F, 0.1875F + f);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.06F, 0.0625F, 0.8125F - f, 0.07F, 0.9375F, 0.875F);
		renderblocks.renderStandardBlock(block, i, j, k);

		renderblocks.setRenderBounds(0.94F, 0.0625F, 0.125F, 0.93F, 0.9375F, 0.1875F + f);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.setRenderBounds(0.94F, 0.0625F, 0.8125F - f, 0.93F, 0.9375F, 0.875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		
		renderblocks.clearOverrideBlockTexture();
		
		renderblocks.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private void renderInvBugTrap(RenderBlocks renderblocks, Block block)
	{
		renderStandardInvBlock(renderblocks, block, 0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
		renderStandardInvBlock(renderblocks, block, 0.0F, 0.9375F, 0.0F, 1.0F, 1.0F, 1.0F);
		
		for (int i1 = 0; i1 < 2; i1++)
		{
			for (int i2 = 0; i2 < 2; i2++)
			{
				boolean flag1 = i1 == 0;
				boolean flag2 = i2 == 0;
				renderStandardInvBlock(renderblocks, block, flag1 ? 0.0F : 0.875F, 0.0625F, flag2 ? 0.0F : 0.875F, flag1 ? 0.125F : 1.0F, 0.9375F, flag2 ? 0.125F : 1.0F);
			}
		}
		
		renderblocks.overrideBlockTexture = mod_LionKing.planks.getIcon(2, 0);
		renderStandardInvBlock(renderblocks, block, 0.46875F, 0.4375F, 0.46875F, 0.53125F, 0.9375F, 0.53125F);
		renderblocks.clearOverrideBlockTexture();
		
		renderblocks.overrideBlockTexture = mod_LionKing.blockSilver.getIcon(2, 0);
		renderStandardInvBlock(renderblocks, block, 0.125F, 0.0625F, 0.06F, 0.1875F, 0.9375F, 0.07F);
		renderStandardInvBlock(renderblocks, block, 0.8125F, 0.0625F, 0.06F, 0.875F, 0.9375F, 0.07F);
		renderStandardInvBlock(renderblocks, block, 0.125F, 0.0625F, 0.94F, 0.1875F, 0.9375F, 0.93F);
		renderStandardInvBlock(renderblocks, block, 0.8125F, 0.0625F, 0.94F, 0.875F, 0.9375F, 0.93F);
		renderStandardInvBlock(renderblocks, block, 0.06F, 0.0625F, 0.125F, 0.07F, 0.9375F, 0.1875F);
		renderStandardInvBlock(renderblocks, block, 0.06F, 0.0625F, 0.8125F, 0.07F, 0.9375F, 0.875F);
		renderStandardInvBlock(renderblocks, block, 0.94F, 0.0625F, 0.125F, 0.93F, 0.9375F, 0.1875F);
		renderStandardInvBlock(renderblocks, block, 0.94F, 0.0625F, 0.8125F, 0.93F, 0.9375F, 0.875F);
		renderblocks.clearOverrideBlockTexture();
		
		renderblocks.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private void renderAridGrass(RenderBlocks renderblocks, IBlockAccess world, Block block, int i, int j, int k)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, i, j, k));
        float f = 1.0F;
        int i1 = block.colorMultiplier(world, i, j, k);
        float f1 = (float)(i1 >> 16 & 255) / 255.0F;
        float f2 = (float)(i1 >> 8 & 255) / 255.0F;
        float f3 = (float)(i1 & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float var11 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float var12 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float var13 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = var11;
            f2 = var12;
            f3 = var13;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        double d = (double)i;
        double d1 = (double)j;
        double d2 = (double)k;

		long l = (long)(i * 3129871) ^ (long)k * 116129781L ^ (long)j;
		l = l * l * 42317861L + l * 11L;
		d += ((double)((float)(l >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
		d1 += ((double)((float)(l >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
		d2 += ((double)((float)(l >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;

        renderblocks.drawCrossedSquares(block, world.getBlockMetadata(i, j, k), d, d1, d2, 1F);
    }
	
	private void renderKiwano(RenderBlocks renderblocks, Block block, int i, int j, int k)
	{
		renderblocks.setRenderBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.8125F, 0.875F);
		renderblocks.renderStandardBlock(block, i, j, k);
		renderblocks.overrideBlockTexture = block.getIcon(0, -1);
		
		int[][] i1 = new int[][]
		{
			new int[] { 9, 4, 12, 7, 4, 11 },
			new int[] { 6, 10, 3, 8, 12, 4 },
			new int[] { 4, 12, 6, 10, 4, 11 },
			new int[] { 5, 3, 7, 12, 10, 3 },
			new int[] { 4, 11, 6, 9, 5, 10 }
		};
		int[][] i2 = new int[][]
		{
			new int[] { 11, 9, 6, 5, 2, 1 },
			new int[] { 10, 8, 6, 4, 2, 1 },
			new int[] { 11, 10, 7, 6, 2, 1 },
			new int[] { 10, 9, 7, 6, 2, 1 },
			new int[] { 11, 10, 8, 6, 4, 3 }
		};
		
		float f;
		float f1;
		for (int l = 0; l < 6; l++)
		{
			f = ((float)i1[0][l]) / 16.0F;
			f1 = ((float)i2[0][l]) / 16.0F;
			
			renderblocks.setRenderBounds(f, f1, 0.875F, f + 0.0625F, f1 + 0.0625F, 0.9375F);
			renderblocks.renderStandardBlock(block, i, j, k);
			
			f = ((float)i1[1][l]) / 16.0F;
			f1 = ((float)i2[1][l]) / 16.0F;
			
			renderblocks.setRenderBounds(f, f1, 0.0625F, f + 0.0625F, f1 + 0.0625F, 0.125F);
			renderblocks.renderStandardBlock(block, i, j, k);
			
			f = ((float)i1[2][l]) / 16.0F;
			f1 = ((float)i2[2][l]) / 16.0F;
			
			renderblocks.setRenderBounds(0.875F, f1, f, 0.9375F, f1 + 0.0625F, f + 0.0625F);
			renderblocks.renderStandardBlock(block, i, j, k);
			
			f = ((float)i1[3][l]) / 16.0F;
			f1 = ((float)i2[3][l]) / 16.0F;
			
			renderblocks.setRenderBounds(0.0625F, f1, f, 0.125F, f1 + 0.0625F, f + 0.0625F);
			renderblocks.renderStandardBlock(block, i, j, k);
			
			f = ((float)i1[4][l]) / 16.0F;
			f1 = ((float)i2[4][l]) / 16.0F;
			
			renderblocks.setRenderBounds(f, 0.8125F, f1, f + 0.0625F, 0.875F, f1 + 0.0625F);
			renderblocks.renderStandardBlock(block, i, j, k);
		}
			
		renderblocks.clearOverrideBlockTexture();
		renderblocks.setRenderBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.8125F, 0.875F);
	}
	
	private void renderInvKiwano(RenderBlocks renderblocks, Block block)
	{
		renderStandardInvBlock(renderblocks, block, 0.125F, 0.0F, 0.125F, 0.875F, 0.8125F, 0.875F);
		renderblocks.overrideBlockTexture = block.getIcon(0, -1);
		
		int[][] i1 = new int[][]
		{
			new int[] { 9, 4, 12, 7, 4, 11 },
			new int[] { 6, 10, 3, 8, 12, 4 },
			new int[] { 4, 12, 6, 10, 4, 11 },
			new int[] { 5, 3, 7, 12, 10, 3 },
			new int[] { 4, 11, 6, 9, 5, 10 }
		};
		int[][] i2 = new int[][]
		{
			new int[] { 11, 9, 6, 5, 2, 1 },
			new int[] { 10, 8, 6, 4, 2, 1 },
			new int[] { 11, 10, 7, 6, 2, 1 },
			new int[] { 10, 9, 7, 6, 2, 1 },
			new int[] { 11, 10, 8, 6, 4, 3 }
		};
		
		float f;
		float f1;
		for (int l = 0; l < 6; l++)
		{
			f = ((float)i1[0][l]) / 16.0F;
			f1 = ((float)i2[0][l]) / 16.0F;
			
			renderStandardInvBlock(renderblocks, block, f, f1, 0.875F, f + 0.0625F, f1 + 0.0625F, 0.9375F);
			
			f = ((float)i1[1][l]) / 16.0F;
			f1 = ((float)i2[1][l]) / 16.0F;
			
			renderStandardInvBlock(renderblocks, block, f, f1, 0.0625F, f + 0.0625F, f1 + 0.0625F, 0.125F);
			
			f = ((float)i1[2][l]) / 16.0F;
			f1 = ((float)i2[2][l]) / 16.0F;
			
			renderStandardInvBlock(renderblocks, block, 0.875F, f1, f, 0.9375F, f1 + 0.0625F, f + 0.0625F);
			
			f = ((float)i1[3][l]) / 16.0F;
			f1 = ((float)i2[3][l]) / 16.0F;
			
			renderStandardInvBlock(renderblocks, block, 0.0625F, f1, f, 0.125F, f1 + 0.0625F, f + 0.0625F);
			
			f = ((float)i1[4][l]) / 16.0F;
			f1 = ((float)i2[4][l]) / 16.0F;
			
			renderStandardInvBlock(renderblocks, block, f, 0.8125F, f1, f + 0.0625F, 0.875F, f1 + 0.0625F);
		}
			
		renderblocks.clearOverrideBlockTexture();
		renderblocks.setRenderBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.8125F, 0.875F);
	}
	
    private void renderKiwanoStem(RenderBlocks renderblocks, IBlockAccess world, Block block, int i, int j, int k)
    {
        LKBlockKiwanoStem stem = (LKBlockKiwanoStem)block;
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(stem.getMixedBrightnessForBlock(world, i, j, k));
        float f = 1.0F;
        int l = stem.colorMultiplier(world, i, j, k);
        float f1 = (float)(l >> 16 & 255) / 255.0F;
        float f2 = (float)(l >> 8 & 255) / 255.0F;
        float f3 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        stem.setBlockBoundsBasedOnState(world, i, j, k);
        int i1 = stem.getState(world, i, j, k);

        if (i1 < 0)
        {
            renderblocks.renderBlockStemSmall((BlockStem)Block.pumpkinStem, world.getBlockMetadata(i, j, k), renderblocks.renderMaxY, (double)i, (double)j, (double)k);
        }
        else
        {
            renderblocks.renderBlockStemSmall((BlockStem)Block.pumpkinStem, world.getBlockMetadata(i, j, k), 0.5D, (double)i, (double)j, (double)k);
            renderblocks.renderBlockStemBig((BlockStem)Block.pumpkinStem, world.getBlockMetadata(i, j, k), i1, renderblocks.renderMaxY, (double)i, (double)j, (double)k);
        }
    }
	
    private void renderLever(RenderBlocks renderblocks, Block block, int i, int j, int k)
    {
        int var5 = renderblocks.blockAccess.getBlockMetadata(i, j, k);
        int i1 = var5 & 7;
        boolean flag = (var5 & 8) > 0;
        Tessellator tessellator = Tessellator.instance;
        boolean var9 = renderblocks.hasOverrideBlockTexture();

        if (!var9)
        {
            renderblocks.overrideBlockTexture = mod_LionKing.pridestone.getIcon(2, 0);
        }

        float var10 = 0.25F;
        float var11 = 0.1875F;
        float var12 = 0.1875F;

        if (i1 == 5)
        {
            renderblocks.setRenderBounds(0.5F - var11, 0.0F, 0.5F - var10, 0.5F + var11, var12, 0.5F + var10);
        }
        else if (i1 == 6)
        {
            renderblocks.setRenderBounds(0.5F - var10, 0.0F, 0.5F - var11, 0.5F + var10, var12, 0.5F + var11);
        }
        else if (i1 == 4)
        {
            renderblocks.setRenderBounds(0.5F - var11, 0.5F - var10, 1.0F - var12, 0.5F + var11, 0.5F + var10, 1.0F);
        }
        else if (i1 == 3)
        {
            renderblocks.setRenderBounds(0.5F - var11, 0.5F - var10, 0.0F, 0.5F + var11, 0.5F + var10, var12);
        }
        else if (i1 == 2)
        {
            renderblocks.setRenderBounds(1.0F - var12, 0.5F - var10, 0.5F - var11, 1.0F, 0.5F + var10, 0.5F + var11);
        }
        else if (i1 == 1)
        {
            renderblocks.setRenderBounds(0.0F, 0.5F - var10, 0.5F - var11, var12, 0.5F + var10, 0.5F + var11);
        }
        else if (i1 == 0)
        {
            renderblocks.setRenderBounds(0.5F - var10, 1.0F - var12, 0.5F - var11, 0.5F + var10, 1.0F, 0.5F + var11);
        }
        else if (i1 == 7)
        {
            renderblocks.setRenderBounds(0.5F - var11, 1.0F - var12, 0.5F - var10, 0.5F + var11, 1.0F, 0.5F + var10);
        }

        renderblocks.renderStandardBlock(block, i, j, k);

        if (!var9)
        {
            renderblocks.clearOverrideBlockTexture();
        }

        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderblocks.blockAccess, i, j, k));
        float var13 = 1.0F;

        if (Block.lightValue[block.blockID] > 0)
        {
            var13 = 1.0F;
        }

        tessellator.setColorOpaque_F(var13, var13, var13);
        Icon icon = block.getBlockTextureFromSide(0);

        if (renderblocks.hasOverrideBlockTexture())
        {
            icon = renderblocks.overrideBlockTexture;
        }

        double d0 = (double)icon.getMinU();
        double d1 = (double)icon.getMaxU();
        double d2 = (double)icon.getMinV();
        double d3 = (double)icon.getMaxV();
        Vec3[] avec3 = new Vec3[8];
        float f4 = 0.0625F;
        float f5 = 0.0625F;
        float f6 = 0.625F;
        avec3[0] = renderblocks.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-f4), 0.0D, (double)(-f5));
        avec3[1] = renderblocks.blockAccess.getWorldVec3Pool().getVecFromPool((double)f4, 0.0D, (double)(-f5));
        avec3[2] = renderblocks.blockAccess.getWorldVec3Pool().getVecFromPool((double)f4, 0.0D, (double)f5);
        avec3[3] = renderblocks.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-f4), 0.0D, (double)f5);
        avec3[4] = renderblocks.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-f4), (double)f6, (double)(-f5));
        avec3[5] = renderblocks.blockAccess.getWorldVec3Pool().getVecFromPool((double)f4, (double)f6, (double)(-f5));
        avec3[6] = renderblocks.blockAccess.getWorldVec3Pool().getVecFromPool((double)f4, (double)f6, (double)f5);
        avec3[7] = renderblocks.blockAccess.getWorldVec3Pool().getVecFromPool((double)(-f4), (double)f6, (double)f5);

        for (int j1 = 0; j1 < 8; ++j1)
        {
            if (flag)
            {
                avec3[j1].zCoord -= 0.0625D;
                avec3[j1].rotateAroundX(((float)Math.PI * 2F / 9F));
            }
            else
            {
                avec3[j1].zCoord += 0.0625D;
                avec3[j1].rotateAroundX(-((float)Math.PI * 2F / 9F));
            }

            if (i1 == 0 || i1 == 7)
            {
                avec3[j1].rotateAroundZ((float)Math.PI);
            }

            if (i1 == 6 || i1 == 0)
            {
                avec3[j1].rotateAroundY(((float)Math.PI / 2F));
            }

            if (i1 > 0 && i1 < 5)
            {
                avec3[j1].yCoord -= 0.375D;
                avec3[j1].rotateAroundX(((float)Math.PI / 2F));

                if (i1 == 4)
                {
                    avec3[j1].rotateAroundY(0.0F);
                }

                if (i1 == 3)
                {
                    avec3[j1].rotateAroundY((float)Math.PI);
                }

                if (i1 == 2)
                {
                    avec3[j1].rotateAroundY(((float)Math.PI / 2F));
                }

                if (i1 == 1)
                {
                    avec3[j1].rotateAroundY(-((float)Math.PI / 2F));
                }

                avec3[j1].xCoord += (double)i + 0.5D;
                avec3[j1].yCoord += (double)((float)j + 0.5F);
                avec3[j1].zCoord += (double)k + 0.5D;
            }
            else if (i1 != 0 && i1 != 7)
            {
                avec3[j1].xCoord += (double)i + 0.5D;
                avec3[j1].yCoord += (double)((float)j + 0.125F);
                avec3[j1].zCoord += (double)k + 0.5D;
            }
            else
            {
                avec3[j1].xCoord += (double)i + 0.5D;
                avec3[j1].yCoord += (double)((float)j + 0.875F);
                avec3[j1].zCoord += (double)k + 0.5D;
            }
        }

        Vec3 vec3 = null;
        Vec3 vec31 = null;
        Vec3 vec32 = null;
        Vec3 vec33 = null;

        for (int k1 = 0; k1 < 6; ++k1)
        {
            if (k1 == 0)
            {
                d0 = (double)icon.getInterpolatedU(7.0D);
                d1 = (double)icon.getInterpolatedV(6.0D);
                d2 = (double)icon.getInterpolatedU(9.0D);
                d3 = (double)icon.getInterpolatedV(8.0D);
            }
            else if (k1 == 2)
            {
                d0 = (double)icon.getInterpolatedU(7.0D);
                d1 = (double)icon.getInterpolatedV(6.0D);
                d2 = (double)icon.getInterpolatedU(9.0D);
                d3 = (double)icon.getMaxV();
            }

            if (k1 == 0)
            {
                vec3 = avec3[0];
                vec31 = avec3[1];
                vec32 = avec3[2];
                vec33 = avec3[3];
            }
            else if (k1 == 1)
            {
                vec3 = avec3[7];
                vec31 = avec3[6];
                vec32 = avec3[5];
                vec33 = avec3[4];
            }
            else if (k1 == 2)
            {
                vec3 = avec3[1];
                vec31 = avec3[0];
                vec32 = avec3[4];
                vec33 = avec3[5];
            }
            else if (k1 == 3)
            {
                vec3 = avec3[2];
                vec31 = avec3[1];
                vec32 = avec3[5];
                vec33 = avec3[6];
            }
            else if (k1 == 4)
            {
                vec3 = avec3[3];
                vec31 = avec3[2];
                vec32 = avec3[6];
                vec33 = avec3[7];
            }
            else if (k1 == 5)
            {
                vec3 = avec3[0];
                vec31 = avec3[3];
                vec32 = avec3[7];
                vec33 = avec3[4];
            }

            tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, d0, d3);
            tessellator.addVertexWithUV(vec31.xCoord, vec31.yCoord, vec31.zCoord, d2, d3);
            tessellator.addVertexWithUV(vec32.xCoord, vec32.yCoord, vec32.zCoord, d2, d1);
            tessellator.addVertexWithUV(vec33.xCoord, vec33.yCoord, vec33.zCoord, d0, d1);
        }
    }
	
	private void renderLily(RenderBlocks renderblocks, IBlockAccess world, Block block, int i, int j, int k)
	{
		renderblocks.renderBlockLilyPad(Block.waterlily, i, j, k);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, i, j, k));

        float f1 = 1.0F;
        float f2 = 1.0F;
        float f3 = 1.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float var11 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float var12 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float var13 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = var11;
            f2 = var12;
            f3 = var13;
        }

        tessellator.setColorOpaque_F(f1, f2, f3);
		renderblocks.drawCrossedSquares(mod_LionKing.lily, world.getBlockMetadata(i, j, k), (double)i, (double)j + 0.005D, (double)k, 0.75F);
	}

	private void renderStandardInvBlock(RenderBlocks renderblocks, Block block, float f, float f1, float f2, float f3, float f4, float f5)
	{
		renderStandardInvBlock(renderblocks, block, f, f1, f2, f3, f4, f5, 0);
	}
	
	private void renderStandardInvBlock(RenderBlocks renderblocks, Block block, float f, float f1, float f2, float f3, float f4, float f5, int damage)
	{
        Tessellator tessellator = Tessellator.instance;
        renderblocks.setRenderBounds(f, f1, f2, f3, f4, f5);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderblocks.getBlockIconFromSideAndMetadata(block, 0, damage));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderblocks.getBlockIconFromSideAndMetadata(block, 1, damage));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderblocks.getBlockIconFromSideAndMetadata(block, 2, damage));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderblocks.getBlockIconFromSideAndMetadata(block, 3, damage));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderblocks.getBlockIconFromSideAndMetadata(block, 4, damage));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderblocks.getBlockIconFromSideAndMetadata(block, 5, damage));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
}
