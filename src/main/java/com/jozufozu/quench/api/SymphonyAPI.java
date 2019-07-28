package com.jozufozu.quench.api;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class SymphonyAPI
{
    public static Logger log;

    public static final String NBT_ROOT = "symphony:attunements";

    public static IForgeRegistry<NoteType> REGISTRY;

    public static boolean baubles;

    public static List<Attunement> getAllAttunements(EntityLivingBase entity)
    {
        List<Attunement> enchantments = new ArrayList<>();

        Arrays.stream(EntityEquipmentSlot.values())
              .map(entity::getItemStackFromSlot)
              .flatMap(stack -> getStackAttunements(stack).stream())
              .forEach(enchantments::add);

        if (baubles && entity instanceof EntityPlayer)
        {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(((EntityPlayer) entity));

            for (int i = 0; i < handler.getSlots(); i++)
            {
                ItemStack stack = handler.getStackInSlot(i);

                if (!stack.isEmpty())
                    enchantments.addAll(getStackAttunements(stack));
            }
        }

        return enchantments;
    }

    public static boolean isTargetableItem(ItemStack stack)
    {

    }

    public static boolean attuneStack(ItemStack stack, Attunement attunement)
    {

    }

    public static List<Attunement> getStackAttunements(ItemStack stack)
    {
        NBTTagCompound compound = stack.getTagCompound();

        if (compound == null || compound.hasKey(NBT_ROOT, Constants.NBT.TAG_COMPOUND)) return Collections.emptyList();

        NBTTagList quench = compound.getTagList(NBT_ROOT, Constants.NBT.TAG_COMPOUND);

        List<Attunement> attunements = new ArrayList<>();

        int numSockets = quench.tagCount();

        for (int i = 0; i < numSockets; i++)
        {
            NBTTagCompound socket = quench.getCompoundTagAt(i);

            for (String name : socket.getKeySet())
            {
                ResourceLocation location = new ResourceLocation(name);
                NoteType enchantment = REGISTRY.getValue(location);

                if (enchantment == null) continue;

                try
                {
                    Attunement attunement = enchantment.instantiate();

                    attunement.deserializeNBT(socket.getTag(name));

                    attunements.add(attunement);
                }
                catch (Exception e)
                {
                    log.error("Tried to initialize a note of type '" + location.toString() + "', but it has no default constructor! This is a programmer error!");
                }
            }
        }

        return attunements;
    }
}
