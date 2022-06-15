/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.mixin.api.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.spongepowered.api.util.weighted.WeightedTable;
import org.spongepowered.api.world.generation.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(StructureSet.class)
public abstract class StructureSetMixin_API implements org.spongepowered.api.world.generation.structure.StructureSet {

    // @formatter:off
    @Shadow @Final private StructurePlacement placement;
    @Shadow @Final private List<StructureSet.StructureSelectionEntry> structures;
    // @formatter:on

    @Override
    public WeightedTable<Structure> structures() {
        final WeightedTable<Structure> weightedTable = new WeightedTable<>();
        for (final StructureSet.StructureSelectionEntry entry : structures) {
            weightedTable.add((Structure) entry.structure().value(), entry.weight());
        }
        return weightedTable;
    }

    @Override
    public Placement placement() {
        return (Placement) this.placement;
    }
}
