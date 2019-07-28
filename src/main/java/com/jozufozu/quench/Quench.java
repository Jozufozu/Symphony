package com.jozufozu.quench;

import com.jozufozu.quench.api.SymphonyAPI;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Quench.MODID, name = Quench.MOD_NAME, version = Quench.VERSION)
public class Quench
{
    @Mod.Instance(MODID)
    public static Quench INSTANCE;

    public static final String MODID = "symphony";
    public static final String MOD_NAME = "Symphonic Attunements";
    public static final String VERSION = "0.0.1-1";

    public static Logger LOG;

    static {
        SymphonyAPI.log = LOG = LogManager.getLogger(MODID);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
