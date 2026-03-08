package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.world.item.ItemStack;

public class LKQuestOutlands extends LKQuestBase {

    public LKQuestOutlands(int index) {
        super(index);
    }

    @Override
    public boolean canStart() {
        return LKQuestBase.RAFIKI_QUEST.isComplete();
    }

    @Override
    public String[] getRequirements() {
        return new String[]{"Complete Rafiki's Quest"};
    }

    @Override
    public int getNumStages() {
        return 10;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(LKItems.WAYWARD_FEATHER.get());
    }

    @Override
    public String getObjectiveByStage(int stage) {
        return switch (stage) {
            case 0 -> "Use a Rafiki Stick to open the large mound in the Outlands";
            case 1 -> "Speak with Zira";
            case 2 -> "Bring Zira five kivulite and two silver ingots";
            case 3 -> "Throw the ingots into the Outwater";
            case 4 -> "Bring Zira three Wayward Feathers";
            case 5 -> "Follow the Outlanders back to the Pride Lands";
            case 6 -> "Speak to Timon and Pumbaa";
            case 7 -> "Obtain the ingredients for Pumbaa";
            case 8 -> "Expel the Outlanders from Rafiki's Tree";
            case 9 -> "Defeat Zira";
            case 10 -> "Quest complete!";
            default -> "";
        };
    }
}
