package com.jozufozu.symphony.api

import net.minecraft.inventory.EquipmentSlotType

enum class EquippedSlot {
    MAINHAS
}

enum class EnumEquipmentType {
    ARMOR,
    TOOLS,
    WEAPONS,
    BAUBLES;

    companion object {
        fun from(equipmentSlotType: EquipmentSlotType): EnumEquipmentType = when (equipmentSlotType) {
            EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND -> TOOLS
            EquipmentSlotType.FEET, EquipmentSlotType.LEGS, EquipmentSlotType.CHEST, EquipmentSlotType.HEAD -> ARMOR
        }
    }

    
}
