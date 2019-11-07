package com.jozufozu.symphony.api.interactions;

import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;

import java.util.List;

public class AttackInteraction
{
    private LivingEntity attacker;
    private DamageSource damageSource;

    private List<LivingEntity> entities;
    private List<EffectInstance> effects;

    private float damage;
    private float knockback;

    private int fireTime;

    private float reflection;

    public AttackInteraction(LivingEntity attacked, DamageSource damageSource)
    {
        this.damageSource = damageSource;
        this.entities = Lists.newArrayList(attacked);
        this.effects = Lists.newArrayList();

        if (damageSource.getTrueSource() instanceof LivingEntity)
            this.attacker = ((LivingEntity) damageSource.getTrueSource());
    }

    public AttackInteraction(LivingEntity attacked, DamageSource damageSource, float damage)
    {
        this(attacked, damageSource);
        this.damage = damage;
    }

    /**
     * The {@link LivingEntity} that initiated the attack
     */
    public LivingEntity getAttacker()
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
     * A list of {@link LivingEntity}s who will feel this attack.<br>
     * Typically only one
     */
    public List<LivingEntity> getEntities()
    {
        return entities;
    }

    public List<EffectInstance> getEffects()
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
