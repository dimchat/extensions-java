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

/**
 * Generic in-memory cache interface with memory reduction capability.
 *
 * Defines the core contract for key-value cache operations, plus a specialized
 * method to reduce memory usage (critical for mobile/resource-constrained environments).
 *
 * @param <K> key type (must be hashable)
 * @param <V> value type (can be nullable)
 */
public interface MemoryCache <K, V> {

    /**
     * Retrieves a value from the cache by key.
     *
     * @param key - the cache key to look up (non-null)
     * @return the cached value (null if key not found or value is null)
     */
    V get(K key);

    /**
     * Stores a value in the cache.
     *
     * @param key - the cache key to associate with the value (non-null)
     * @param value is the value to cache (null = remove the key from cache)
     * @return the previous value associated with the key (null if none)
     */
    V put(K key, V value);

    /**
     * Returns the current number of entries in the cache.
     *
     * @return a non-negative integer representing the count of cached key-value pairs
     */
    int size();

    /**
     * Reduces cache memory usage by evicting entries (implementation-specific logic).
     *
     * @return the number of entries remaining in the cache after reduction
     */
    int reduceMemory();

}
