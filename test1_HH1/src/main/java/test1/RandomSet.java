package test1;

import java.util.ArrayList;
import java.util.HashMap;

public class RandomSet {

	HashMap<Object, Integer> set = new HashMap<Object, Integer>();
	ArrayList<Object> arr = new ArrayList<Object>();

	public void add(Object o) {
		arr.add(o);
		set.put(o, set.size());
	}

	public void remove(Object o) {
		int x = set.get(o);
		Object or = arr.get(arr.size() - 1);
		arr.set(x, or);
		arr.remove(arr.size() - 1);
		set.replace(or, x);
		set.remove(o);
	}

	public Object getRandom() {
		Object o = null;
		o = arr.get((int) (Math.random() * set.size()));
		return o;
	}

	public void print() {
		System.out.println(set);
		System.out.println(arr);
	}
}
