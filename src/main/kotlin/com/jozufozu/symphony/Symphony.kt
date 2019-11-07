package com.jozufozu.symphony;

import com.jozufozu.symphony.api.SymphonyAPI;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Symphony.MODID)
public class Symphony
{
    public static final String MODID = "symphony";
    public static Logger LOG = SymphonyAPI.log = LogManager.getLogger(MODID);;
}
