package org.Listeners;

import org.Domain.SpellType;

public interface InventoryListener {
    void onInventoryUpdate(SpellType spellType, int newCount);
}