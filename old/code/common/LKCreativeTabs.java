package lionking.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.*;

public class LKCreativeTabs extends CreativeTabs
{
	public static LKCreativeTabs tabBlock = new LKCreativeTabs("Lion King Blocks");
	public static LKCreativeTabs tabDeco = new LKCreativeTabs("Lion King Decorations");
	public static LKCreativeTabs tabFood = new LKCreativeTabs("Lion King Foodstuffs");
	public static LKCreativeTabs tabMaterials = new LKCreativeTabs("Lion King Materials");
	public static LKCreativeTabs tabMisc = new LKCreativeTabs("Lion King Miscellaneous");
	public static LKCreativeTabs tabTools = new LKCreativeTabs("Lion King Tools");
	public static LKCreativeTabs tabCombat = new LKCreativeTabs("Lion King Combat");
	public static LKCreativeTabs tabQuest = new LKCreativeTabs("Lion King Quest Items");
	
	public ItemStack theIcon;
	
	public LKCreativeTabs(String label)
	{
		super(label);
	}
	
	public static void setupIcons()
	{
		tabBlock.theIcon = new ItemStack(mod_LionKing.prideBrick);
		tabDeco.theIcon = new ItemStack(mod_LionKing.sapling);
		tabFood.theIcon = new ItemStack(mod_LionKing.zebraRaw);
		tabMaterials.theIcon = new ItemStack(mod_LionKing.hyenaBone);
		tabMisc.theIcon = new ItemStack(mod_LionKing.bug);
		tabTools.theIcon = new ItemStack(mod_LionKing.pickaxe);
		tabCombat.theIcon = new ItemStack(mod_LionKing.dartShooter);
		tabQuest.theIcon = new ItemStack(mod_LionKing.questBook, 1, 1);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel()
    {
        return getTabLabel();
    }
	
	@Override
    public ItemStack getIconItemStack()
    {
        return theIcon;
    }
}
