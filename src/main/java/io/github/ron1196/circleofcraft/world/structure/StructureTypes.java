package io.github.ron1196.circleofcraft.world.structure;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructureTypes {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, CircleOfCraftMod.MOD_ID);

    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, CircleOfCraftMod.MOD_ID);

    public static final RegistryObject<StructureType<ModStructure>> LK_CODE_STRUCTURE =
            STRUCTURE_TYPES.register("lk_code_structure", () -> () -> ModStructure.CODEC);

    public static final RegistryObject<StructurePieceType> LK_PIECE_TYPE =
            STRUCTURE_PIECE_TYPES.register("lk_code_piece", () -> ModStructurePiece::new);
}
