package lionking.common;

import net.minecraft.block.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.*;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class LKBlockMobSpawner extends BlockMobSpawner
{
	public LKBlockMobSpawner(int i)
	{
		super(i);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j)
	{
		return Block.mobSpawner.getIcon(i, j);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconregister) {}
	
	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new LKTileEntityMobSpawner();
    }
}
