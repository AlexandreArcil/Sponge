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
package org.spongepowered.common.mixin.api.minecraft.world.level.storage;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.api.util.MinecraftDayTime;
import org.spongepowered.api.world.gamerule.GameRule;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.accessor.world.level.GameRulesAccessor;
import org.spongepowered.common.accessor.world.level.GameRules_ValueAccessor;
import org.spongepowered.common.util.SpongeMinecraftDayTime;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(LevelData.class)
public interface LevelDataMixin_API extends WorldProperties {

    // @formatter:off
    @Shadow long shadow$getGameTime();
    @Shadow long shadow$getDayTime();
    @Shadow GameRules shadow$getGameRules();
    // @formatter:on

    @Override
    default MinecraftDayTime gameTime() {
        return new SpongeMinecraftDayTime(this.shadow$getGameTime());
    }

    @Override
    default MinecraftDayTime dayTime() {
        return new SpongeMinecraftDayTime(this.shadow$getDayTime());
    }

    @Override
    default <V> V gameRule(final GameRule<V> gameRule) {
        final GameRules.Value<?> value = this.shadow$getGameRules().getRule((GameRules.Key<?>) (Object) Objects.requireNonNull(gameRule,
                "gameRule"));
        if (value instanceof GameRules.BooleanValue) {
            return (V) Boolean.valueOf(((GameRules.BooleanValue) value).get());
        } else if (value instanceof GameRules.IntegerValue) {
            return (V) Integer.valueOf(((GameRules.IntegerValue) value).get());
        }
        return null;
    }

    @Override
    default <V> void setGameRule(final GameRule<V> gameRule, final V value) {
        Objects.requireNonNull(gameRule, "gameRule");
        Objects.requireNonNull(value, "value");

        final GameRules.Value<?> mValue = this.shadow$getGameRules().getRule((GameRules.Key<?>) (Object) gameRule);
        ((GameRules_ValueAccessor) mValue).invoker$deserialize(value.toString());
    }

    @Override
    default Map<GameRule<?>, ?> gameRules() {
        final Map<GameRules.Key<?>, GameRules.Value<?>> rules =
                ((GameRulesAccessor) this.shadow$getGameRules()).accessor$rules();

        final Map<GameRule<?>, Object> apiRules = new HashMap<>();
        for (final Map.Entry<GameRules.Key<?>, GameRules.Value<?>> rule : rules.entrySet()) {
            final GameRule<?> key = (GameRule<?>) (Object) rule.getKey();
            final GameRules.Value<?> mValue = rule.getValue();
            Object value = null;
            if (mValue instanceof GameRules.BooleanValue) {
                value = ((GameRules.BooleanValue) mValue).get();
            } else if (mValue instanceof GameRules.IntegerValue) {
                value = ((GameRules.IntegerValue) mValue).get();
            }

            if (value != null) {
                apiRules.put(key, value);
            }
        }

        return apiRules;
    }
}
