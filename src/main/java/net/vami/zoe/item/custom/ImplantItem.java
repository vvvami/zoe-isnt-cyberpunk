package net.vami.zoe.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.vami.zoe.ZoeIsntCyberpunk;
import net.vami.zoe.event.custom.ImplantOnEquipEvent;
import net.vami.zoe.event.custom.ImplantOnUnequipEvent;
import net.vami.zoe.util.implant.ImplantData;
import net.vami.zoe.util.implant.ImplantUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class ImplantItem extends Item {
    public ArrayList<AttributeModifier> attributeList = new ArrayList<>();

    public ImplantItem(Properties pProperties) {
        super(pProperties);
    }

    public void onTick(LivingEntity entity, ItemStack item) {}

    public void onEquip(ImplantOnEquipEvent event) {}
    public void onUnequip(ImplantOnUnequipEvent event) {}

    public void onHit(LivingHurtEvent event) {}
    public void onHurt(LivingHurtEvent event) {}
    public void onDeath(Entity source, LivingEntity target, ItemStack item) {}
    public void onKill(LivingEntity source, LivingEntity target, ItemStack item) {}

    public abstract ImplantData data();

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide) return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));

        ArrayList<ItemStack> implants = ImplantUtil.implants(pPlayer);

        for (int i = 0; i < implants.size(); i++) {
            if (implants.get(i).getItem() == Items.AIR) {
                ImplantUtil.setSlot(pPlayer, pPlayer.getItemInHand(pUsedHand), i);
                pPlayer.setItemInHand(pUsedHand, Items.AIR.getDefaultInstance());
                ZoeIsntCyberpunk.LOGGER.debug(implants.get(i).toString());
                break;
            }
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        setDefaultTags(stack);
        return stack;
    }

    public static void setDefaultTags(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        if (!tag.contains(ImplantUtil.QUALITY_TAG)) {
            tag.putDouble(ImplantUtil.QUALITY_TAG, 100f);
        }
    }
}
