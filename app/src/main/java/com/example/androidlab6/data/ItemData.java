package com.example.androidlab6.data;

import com.example.androidlab6.myinterf.ChangedListenerData;
import com.example.androidlab6.cart.Cart;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class ItemData {
    int maxId = 0;
    private List<Item> items;
    private List<ChangedListenerData> listeners;
    private static final ItemData ourInstance = new ItemData();

    public static ItemData getInstance() {
        return ourInstance;
    }

    public List<Item> getAvailableItems() {
        List<Item> availableItems = new LinkedList<>();
        for(Item item : items) {
            if(item.getCount() > 0)
                availableItems.add(item);
        }
        return availableItems;
    }

    public void addDataChangedListener(ChangedListenerData listener) {
        listeners.add(listener);
    }

    public void addItem(Item newItem) {
        newItem.setId(maxId + 1);
        maxId +=1;
        items.add(newItem);
        listeners.forEach(new Consumer<ChangedListenerData>() {
            @Override
            public void accept(ChangedListenerData e) {
                e.notifyDataChanged();
            }
        });
    }

    public void deleteItem(int id) {
        for(Item item : items) {
            if(item.getId() == id)
                items.remove(item);
        }
        listeners.forEach(new Consumer<ChangedListenerData>() {
            @Override
            public void accept(ChangedListenerData e) {
                e.notifyDataChanged();
            }
        });
    }

    public void updateItem(Item updatedItem) {
        for(Item item :items) {
            if(item.getId() == updatedItem.getId()) {
                    items.set(items.indexOf(item), updatedItem);
                    Cart.getInstance().updateItem(updatedItem);
            }
        }
        listeners.forEach(new Consumer<ChangedListenerData>() {
            @Override
            public void accept(ChangedListenerData e) {
                e.notifyDataChanged();
            }
        });
    }

    public void removeListener(ChangedListenerData listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public List<Item> getItems() {
        return items;
    }

    public void performPurchase(Cart cart) {
        for(Item item : cart.getItemsArray()) {
            item.setCount(item.getCount() - cart.getCount(item));
        }
        listeners.forEach(new Consumer<ChangedListenerData>() {
            @Override
            public void accept(ChangedListenerData e) {
                e.notifyDataChanged();
            }
        });
    }
    private ItemData() {
        items = new LinkedList<>();
        listeners = new LinkedList<>();
        addItem(new Item(1,"Мерседес", 900000, 2, "Автомобиль премиального класса."));
        addItem(new Item(2,"Ferrari", 12000000, 3, "Спортивный автомобиль."));
        addItem(new Item(3,"Лада", 520000, 28, "Наша любимица."));
        addItem(new Item(4,"BMW", 2000000, 0, "Автомобиль премиального класса."));
    }
}
