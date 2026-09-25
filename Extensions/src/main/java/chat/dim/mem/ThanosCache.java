/* license: https://mit-license.org
 *
 *  DIMP : Decentralized Instant Messaging Protocol
 *
 *                                Written in 2025 by Moky <albert.moky@gmail.com>
 *
 * ==============================================================================
 * The MIT License (MIT)
 *
 * Copyright (c) 2025 Albert Moky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * ==============================================================================
 */
package chat.dim.mem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Implementation of {@link MemoryCache} with "Thanos-style" memory reduction.
 *
 * Core feature: The reduceMemory method removes exactly half of the cache entries
 * (inspired by Thanos snapping his fingers to kill half the universe), making it
 * a deterministic eviction policy for memory optimization.
 *
 * Note: uses a standard {@link java.util.HashMap} as the underlying storage,
 * with O(1) get/put operations.
 */
public class ThanosCache <K, V> implements MemoryCache<K, V> {

    private final Map<K, V> caches = new HashMap<>();

    @Override
    public V get(K key) {
        return caches.get(key);
    }

    @Override
    public V put(K key, V value) {
        if (value == null) {
            // null value = remove key from cache
            return caches.remove(key);
        }
        return caches.put(key, value);
    }

    @Override
    public int size() {
        return caches.size();
    }

    @Override
    public int reduceMemory() {
        int finger = 0;
        // Execute Thanos-style eviction (kill half the entries)
        finger = thanos(caches, finger);
        // Return number of remaining entries (half of original count)
        return finger >> 1;
    }

    /**
     * Thanos-style cache eviction function - removes half of the map entries.
     *
     * "Thanos can kill half lives of a world with a snap of the finger"
     *
     * Eviction logic: iterates through map entries in insertion order;
     * removes entries where the incremented finger counter is odd (keeps even
     * entries); guarantees exactly 50% of entries are removed (deterministic eviction).
     *
     * @param planet - the map (cache) to "snap" (modify in-place)
     * @param finger - the starting counter value (typically 0 for fresh snap)
     * @return the final value of the finger counter (total number of entries processed)
     *
     * Note: modifies the input map directly (in-place operation).
     */
    public static <K, V> int thanos(Map<K, V> planet, int finger) {
        Iterator<Map.Entry<K, V>> people = planet.entrySet().iterator();
        while (people.hasNext()) {
            people.next();
            if ((++finger & 1) == 1) {
                // kill it
                people.remove();
            }
            // let it go
        }
        return finger;
    }

}
