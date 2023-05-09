package test.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.hotrod.torcs.Item;

public class Cache<T extends Item> {

  private List<T> list;
  private Map<String, T> map;
  private T last;

  private int maxSize;
  private Sorter<T> sorter;

  public Cache(final int maxSize, Sorter<T> sorter) {
    this.list = new ArrayList<>();
    this.map = new HashMap<>();
    this.last = null;
    this.maxSize = maxSize;
    this.sorter = sorter;
  }

  public synchronized void record(T item) {

    T hit = this.map.get(item.getKey());

    if (hit == null) { // 1. It's a new item

      if (this.list.size() < this.maxSize) { // cache not full
        ListIterator<T> it = this.list.listIterator();
        while (it.hasNext()) {
          T e = it.next();
          if (this.sorter.isBefore(item, e)) {
            it.add(item);
            return;
          }
        }

      } else {
        // If worse than last item, ignore
        if (this.last != null && !this.sorter.isBefore(item, this.last)) {
          return;
        }

        // ...

      }

    } else { // 2. It's an existing item
      
    }

  }

}
