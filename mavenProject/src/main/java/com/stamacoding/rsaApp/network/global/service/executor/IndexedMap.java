package com.stamacoding.rsaApp.network.global.service.executor;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class IndexedMap<K, V>{
	private final ArrayList<K> keys = new ArrayList<>();
	private final ArrayList<V> values = new ArrayList<>();
	private int size = 0;
	
	private ArrayList<K> getKeys() {
		return keys;
	}
	
	private ArrayList<V> getValues() {
		return values;
	}
	
	public void put(K key, V value) {
		int index = getKeys().indexOf(key);
		if(index != -1) {
			getValues().set(index, value);
		}else {
			getValues().add(value);
			getKeys().add(key);
			size++;
		}
	}

	public void put(int i, V value) {
		if(i >= 0 && i < size()) {
			getValues().set(i, value);
		}else {
			throw new IndexOutOfBoundsException(i + " is greater than the map's max index (" + (size()-1) + ") or smaller than 0!");
		}
	}
	
	public void remove(K key) {
		int index = getKeys().indexOf(key);
		if(index != -1) {
			size--;
			getKeys().remove(index);
			getValues().remove(index);
		}else {
			throw new NoSuchElementException("Didn't find key: " + key.toString());
		}
	}
	
	public void remove(int i) {
		if(i >= 0 && i < size()) {
			size--;
			getKeys().remove(i);
			getValues().remove(i);
		}else {
			throw new IndexOutOfBoundsException(i + " is greater than the map's max index (" + (size()-1) + ") or smaller than 0!");
		}
	}
	
	public int size() {
		return size;
	}
	
	public K getKey(int i) {
		return getKeys().get(i);
	}
	
	public ArrayList<K> getKeys(V value) {
		ArrayList<K> keys = new ArrayList<>();
		for(int i=0; i < getValues().size(); i++) {
			if(getValues().get(i) != null && getValues().get(i).equals(value)) {
				keys.add(getKey(i));
			}
		}
		return keys;
	}
	
	public V getValue(int i) {
		if(i < 0 || i >= size()) throw new IndexOutOfBoundsException(i + " is greater than the map's max index (" + (size()-1) + ") or smaller than 0!");
		return getValues().get(i);
	}
	
	public V getValue(K key) {
		int index = getKeys().indexOf(key);
		if(index != -1) {
			return getValues().get(index);
		}else {
			throw new NoSuchElementException("Didn't find key: " + key.toString());
		}
	}
	
	public int indexOf(K key) {
		return getKeys().indexOf(key);
	}
	
	public boolean contains(K key) {
		return getKeys().contains(key);
	}
	
	public boolean containsValue(V value) {
		return getValues().contains(value);
	}
	
}
