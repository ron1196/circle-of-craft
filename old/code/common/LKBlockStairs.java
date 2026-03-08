package lionking.common;

import net.minecraft.block.*;
import net.minecraft.world.*;

public class LKBlockStairs extends BlockStairs
{
	private Block theBlock;
	private int theBlockMetadata;

    public LKBlockStairs(int i, Block block, int j)
    {
        super(i, block, j);
		theBlock = block;
		theBlockMetadata = j;
		setCreativeTab(LKCreativeTabs.tabBlock);
    }
	
	@Override
    public float getBlockHardness(World world, int i, int j, int k)
    {
        return theBlock.getBlockHardness(world, i, j, k);
    }
}