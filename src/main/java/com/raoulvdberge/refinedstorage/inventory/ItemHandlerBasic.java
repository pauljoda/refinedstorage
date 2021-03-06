package com.raoulvdberge.refinedstorage.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemHandlerBasic extends ItemStackHandler {
    private IItemHandlerListener listener;

    protected IItemValidator[] validators;

    public ItemHandlerBasic(int size, @Nullable IItemHandlerListener listener, IItemValidator... validators) {
        super(size);

        this.listener = listener;
        this.validators = validators;
    }

    public ItemHandlerBasic(int size, IItemValidator... validators) {
        this(size, null, validators);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        validateSlotIndex(slot);

        stacks.set(slot, stack);

        onContentsChanged(slot);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (validators.length > 0) {
            for (IItemValidator validator : validators) {
                if (validator.isValid(stack)) {
                    return super.insertItem(slot, stack, simulate);
                }
            }

            return stack;
        }

        return super.insertItem(slot, stack, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        if (listener != null) {
            listener.onChanged(slot);
        }
    }

    @Nonnull
    public ItemStack extractItemInternal(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }
}
