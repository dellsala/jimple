package org.jimple;
/*
 * This file is part of Jimple.
 *
 * Copyright (c) 2013 Kuzmin Leonid
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.util.HashMap;

/**
 * Jimple main class.
 *
 * @author Leonid Kuzmin
 */
public class Jimple extends HashMap<String, Object> {
    /**
     * Jimple Item
     */
    public interface Item<T> {
        public T create(Jimple c);
    }

    private class SimpleItem {
        protected final Item item;

        private SimpleItem(Item item) {
            this.item = item;
        }

        protected <T> T getItem(Jimple c) {
            //noinspection unchecked
            return (T) item.create(c);
        }

        private Item raw() {
            return item;
        }
    }

    /**
     * Shared Jimple Item
     */
    private class SharedItem extends SimpleItem {
        private Object instance;

        private SharedItem(Item item) {
            super(item);
        }

        protected <T> T getItem(Jimple c) {
            if (instance == null) {
                instance = item.create(c);
            }
            //noinspection unchecked
            return (T) instance;
        }
    }

    /**
     * Jimple Item extender
     */
    public interface Extender<T> {
        public <E extends T> E extend(T item);
    }

    /**
     * Extended Jimple item
     */
    private class ExtendedItem extends SimpleItem {
        private final Extender extender;

        private ExtendedItem(SimpleItem item, Extender extender) {
            super(item.raw());
            this.extender = extender;
        }

        protected <T> T getItem(Jimple c) {
            //noinspection unchecked
            return (T) extender.extend(super.getItem(c));
        }
    }

    /**
     * Returns a SharedItem that stores the result of the given Item for
     * uniqueness in the scope of this instance of Jimple.
     *
     * @param item A Item to wrap for uniqueness
     * @return The wrapped Item
     */
    public SharedItem share(Item item) {
        return new SharedItem(item);
    }

    /**
     * Extends and Item definition
     * Useful when you want to extend an existing Item definition,
     * without necessarily loading that object.
     *
     * @param key      The unique identifier for the Item
     * @param extender A Extender for the original
     * @return The wrapped Item
     * @throws IllegalArgumentException if the identifier is not defined
     */
    public ExtendedItem extend(String key, Extender extender) {
        if (!super.containsKey(key)) {
            throw new IllegalArgumentException("Identifier " + key + " is not defined.");
        }
        Object item = super.get(key);
        if (item instanceof SimpleItem) {
            return new ExtendedItem((SimpleItem) item, extender);
        }
        throw new IllegalArgumentException("Identifier " + key + " does not contain an object definition.");
    }

    public <T> T get(String key) {
        Object item = super.get(key);
        if (item instanceof SimpleItem) {
            return ((SimpleItem) item).getItem(this);
        }
        //noinspection unchecked
        return (T) item;
    }

    @Override
    public Object put(String key, Object item) {
        if (item instanceof Item) {
            item = new SimpleItem((Item) item);
        }
        return super.put(key, item);
    }

    /**
     * Gets a parameter or the Item defining an object.
     *
     * @param key The unique identifier for the parameter or object
     * @return The value of the parameter or the Item defining an object
     */
    public Item raw(String key) {
        return ((SimpleItem) super.get(key)).raw();
    }
}
