package io.github.ron1196.thelionking.world.structure;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructureTypes {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, TheLionKingMod.MOD_ID);

    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, TheLionKingMod.MOD_ID);

    public static final RegistryObject<StructureType<LionKingStructure>> LK_CODE_STRUCTURE =
            STRUCTURE_TYPES.register("lk_code_structure", () -> () -> LionKingStructure.CODEC);

    public static final RegistryObject<StructurePieceType> LK_PIECE_TYPE =
            STRUCTURE_PIECE_TYPES.register("lk_code_piece", () -> LionKingStructurePiece::new);
}
