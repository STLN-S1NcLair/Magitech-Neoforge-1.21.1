package net.stln.magitech.data;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.magic.charge.ChargeData;
import net.stln.magitech.feature.magic.cooldown.CooldownData;

import java.util.function.Supplier;

public class DataAttachmentInit {
    public static final DeferredRegister<AttachmentType<?>> DATA_ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Magitech.MOD_ID);

    public static final Supplier<AttachmentType<Long>> ENTITY_MANA = DATA_ATTACHMENT_TYPES.register("mana", () -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG).sync(ByteBufCodecs.VAR_LONG).copyOnDeath().build());

    public static final Supplier<AttachmentType<CooldownData>> SPELL_COOLDOWNS = DATA_ATTACHMENT_TYPES.register("spell_cooldowns", () -> AttachmentType.builder(CooldownData::empty).serialize(CooldownData.CODEC).sync(CooldownData.STREAM_CODEC).copyOnDeath().build());

    public static final Supplier<AttachmentType<ChargeData>> SPELL_CHARGE = DATA_ATTACHMENT_TYPES.register("spell_charge", () -> AttachmentType.builder(ChargeData::empty).serialize(ChargeData.CODEC).sync(ChargeData.STREAM_CODEC).build());

    public static void registerDataAttachmentTypes(IEventBus bus) {
        Magitech.LOGGER.info("Registering Data Attachment Types for" + Magitech.MOD_ID);
        DATA_ATTACHMENT_TYPES.register(bus);
    }
}
