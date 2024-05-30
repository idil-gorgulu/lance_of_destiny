package org.Domain;

import org.Listeners.InventoryListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Inventory{

    // Hex
    // Felix Felicis
    // Magical Staff Expansion
    // Overwhelming Fireball
    private ArrayList<Integer> spellCountsList;
    private ArrayList<InventoryListener> listeners;

    public Inventory() {
        this.spellCountsList = new ArrayList<>(Arrays.asList(0, 0, 0, 0));
        this.listeners = new ArrayList<>();
    }

    // Method to add a listener
    public void addInventoryListener(InventoryListener listener) {
        listeners.add(listener);
    }

    // Method to notify listeners
    private void notifyListeners(SpellType spellType, int newCount) {
        for (InventoryListener listener : listeners) {
            listener.onInventoryUpdate(spellType, newCount);
        }
    }

    // Method to update the inventory
    public void updateInventory(SpellType spellType, int countChange){
        int newCount = 0;
        if (spellType == SpellType.HEX){
            spellCountsList.set(0, (spellCountsList.get(0)+countChange));
            newCount = spellCountsList.get(0);
        }
        else if (spellType == SpellType.FELIX_FELICIS){
            spellCountsList.set(1, (spellCountsList.get(1)+countChange));
            newCount = spellCountsList.get(1);
        }
        else if (spellType == SpellType.STAFF_EXPANSION){
            spellCountsList.set(2, (spellCountsList.get(2)+countChange));
            newCount = spellCountsList.get(2);
        }
        else if (spellType == SpellType.OVERWHELMING_FIREBALL){
            spellCountsList.set(3, (spellCountsList.get(3)+countChange));
            newCount = spellCountsList.get(3);
        }
        notifyListeners(spellType, newCount);
    }

    public boolean checkSpellCount(SpellType spellType){
        boolean hasSpell = false;
        if (spellType == SpellType.HEX && spellCountsList.get(0) > 0){
            hasSpell = true;
        }
        else if (spellType == SpellType.FELIX_FELICIS && spellCountsList.get(1) > 0){
            hasSpell = true;
        }
        else if (spellType == SpellType.STAFF_EXPANSION && spellCountsList.get(2) > 0){
            hasSpell = true;
        }
        else if (spellType == SpellType.OVERWHELMING_FIREBALL && spellCountsList.get(3) > 0){
            hasSpell = true;
        }
        return hasSpell;
    }
    public ArrayList<Integer> getSpellCountsList() {
        return spellCountsList;
    }

    public int getSpellCount(SpellType spellType){
        if (spellType == SpellType.HEX){
            return spellCountsList.get(0);
        }
        else if (spellType == SpellType.FELIX_FELICIS){
            return spellCountsList.get(1);
        }
        else if (spellType == SpellType.STAFF_EXPANSION){
            return spellCountsList.get(2);
        }
        else if (spellType == SpellType.OVERWHELMING_FIREBALL){
            return spellCountsList.get(3);
        }
        return 0;
    }

    public void setSpellCount(SpellType spellType, int count){
        if (spellType == SpellType.HEX){
            spellCountsList.set(0, count);
        }
        else if (spellType == SpellType.FELIX_FELICIS){
            spellCountsList.set(1, count);
        }
        else if (spellType == SpellType.STAFF_EXPANSION){
            spellCountsList.set(2, count);
        }
        else if (spellType == SpellType.OVERWHELMING_FIREBALL){
            spellCountsList.set(2, count);
        }
    }
}