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
package org.spongepowered.common.world.generation;

import net.minecraft.world.level.levelgen.WorldOptions;
import org.spongepowered.api.world.generation.config.WorldGenerationConfig;
import org.spongepowered.common.SpongeCommon;
import org.spongepowered.common.util.SeedUtil;

import java.util.Objects;

public final class SpongeWorldGenerationConfigBuilder implements WorldGenerationConfig.Builder {

    private long seed;
    private boolean generateFeatures, generateBonusChest;

    @Override
    public WorldGenerationConfig.Builder seed(final long seed) {
        this.seed = seed;
        return this;
    }

    @Override
    public void seed(final String seed) {
        this.seed = SeedUtil.compute(seed);
    }

    @Override
    public WorldGenerationConfig.Builder generateStructures(final boolean generateStructures) {
        this.generateFeatures = generateStructures;
        return this;
    }

    @Override
    public WorldGenerationConfig.Builder generateBonusChest(final boolean generateBonusChest) {
        this.generateBonusChest = generateBonusChest;
        return this;
    }

    @Override
    public WorldGenerationConfig.Builder reset() {
        final WorldOptions defaultSettings = SpongeCommon.server().getWorldData().worldGenOptions();
        this.seed = defaultSettings.seed();
        this.generateFeatures = defaultSettings.generateStructures();
        this.generateBonusChest = defaultSettings.generateBonusChest();
        return this;
    }

    @Override
    public WorldGenerationConfig.Builder from(final WorldGenerationConfig value) {
        this.seed = Objects.requireNonNull(value, "value").seed();
        this.generateFeatures = value.generateStructures();
        this.generateBonusChest = value.generateBonusChest();
        return this;
    }

    @Override
    public WorldGenerationConfig build() {
        return (WorldGenerationConfig) new WorldOptions(this.seed, this.generateFeatures, this.generateBonusChest);
    }
}
