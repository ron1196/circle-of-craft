package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.world.item.ItemStack;

public class LKQuestRafiki extends LKQuestBase {

    public LKQuestRafiki(int index) {
        super(index);
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public String[] getRequirements() {
        return null;
    }

    @Override
    public int getNumStages() {
        return 7;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(LKItems.STAFF.get());
    }

    @Override
    public String getObjectiveByStage(int stage) {
        return switch (stage) {
            case 0 -> "Speak to Rafiki in his tree at the centre of the world";
            case 1 -> "Bring Rafiki a full stack of hyena bones";
            case 2 -> "Find and defeat Scar";
            case 3 -> "Return to Rafiki";
            case 4 -> "Bring Rafiki four ground termites";
            case 5 -> "Bring Rafiki four ground mangoes";
            case 6 -> "Build a Star Altar and use the Rafiki Dust on it";
            case 7 -> "Quest complete!";
            default -> "";
        };
    }
}
