package com.jozufozu.quench.api.interactions;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import java.util.List;

public class AttackInteraction
{
    private EntityLivingBase attacker;
    private DamageSource damageSource;

    private List<EntityLivingBase> entities;
    private List<PotionEffect> effects;

    private float damage;
    private float knockback;

    private int fireTime;

    private float reflection;

    public AttackInteraction(EntityLivingBase attacked, DamageSource damageSource)
    {
        this.damageSource = damageSource;
        this.entities = Lists.newArrayList(attacked);
        this.effects = Lists.newArrayList();

        if (damageSource.getTrueSource() instanceof EntityLivingBase)
            this.attacker = ((EntityLivingBase) damageSource.getTrueSource());
    }

    public AttackInteraction(EntityLivingBase attacked, DamageSource damageSource, float damage)
    {
        this(attacked, damageSource);
        this.damage = damage;
    }

    /**
     * The {@link EntityLivingBase} that initiated the attack
     */
    public EntityLivingBase getAttacker()
    {
        return attacker;
    }

    public DamageSource getDamageSource()
    {
        return damageSource;
    }

    public void setDamageSource(DamageSource damageSource)
    {
        this.damageSource = damageSource;
    }

    /**
     * A list of {@link EntityLivingBase}s who will feel this attack.<br>
     * Typically only one
     */
    public List<EntityLivingBase> getEntities()
    {
        return entities;
    }

    public List<PotionEffect> getEffects()
    {
        return effects;
    }

    public float getDamage()
    {
        return damage;
    }

    public void setDamage(float damage)
    {
        this.damage = damage;
    }

    public float getKnockback()
    {
        return knockback;
    }

    public void setKnockback(float knockback)
    {
        this.knockback = knockback;
    }

    public int getFireTime()
    {
        return fireTime;
    }

    public void setFireTime(int fireTime)
    {
        this.fireTime = fireTime;
    }

    /**
     * A number greater than 0.0 that represents who the damage and effects go to.<br>
     * 0.0 means 100 percent of the effects are felt by the {@link AttackInteraction#entities}<br>
     * 1.0 means 100 percent of the effects are reflected back at the {@link AttackInteraction#attacker}<br>
     * 2.0 means the damage and other effects are doubled and reflected back at the {@link AttackInteraction#attacker}
     */
    public float getReflection()
    {
        return reflection;
    }

    public void setReflection(float reflection)
    {
        this.reflection = reflection;
    }
}
