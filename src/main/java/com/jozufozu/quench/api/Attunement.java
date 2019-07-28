package com.jozufozu.quench.api;

import com.jozufozu.quench.api.interactions.AttackInteraction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class Attunement<NBT extends NBTBase> implements INBTSerializable<NBT>
{
    public Attunement() {}

    protected ItemStack appliedOn;

    public abstract ITextComponent getDisplay(boolean advanced);

    public abstract int getColorRGB();

    public Attunement amplify()
    {
        return this;
    }

    public void onUserUpdate(EntityLivingBase user) { }

    public void onUserAttackedByEntity(AttackInteraction attack) { }

    public void onUserAttackEntity(AttackInteraction attack) { }

    public void onUserDeath() { }
}
