package com.rsgtechlabs.tradestore.store;

import java.util.List;
import java.util.Map;

public interface Store<K,V> {

    void add(final K key, final V value);
    boolean contains(final K key);
    int getLatestVersion(final K key);
    Map<K,V> get();

    //Checks maturity date and sets expires flag
    void inspect();
    List<V> get(K key);
}
