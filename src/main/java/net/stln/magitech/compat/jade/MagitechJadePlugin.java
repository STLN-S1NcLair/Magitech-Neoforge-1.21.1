package net.stln.magitech.compat.jade;

import net.stln.magitech.block.ManaVesselBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class MagitechJadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new ManaContainerJadeProvider(), ManaVesselBlock.class);
    }
}
