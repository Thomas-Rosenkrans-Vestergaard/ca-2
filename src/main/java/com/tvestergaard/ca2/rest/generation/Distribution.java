package com.tvestergaard.ca2.rest.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Helper class for retrieving random elements from a distribution.
 * @param <T>
 */
class Distribution<T>
{

    private static Random      random        = new Random();
    private        int         total         = 0;
    private        List<Entry> possibilities = new ArrayList<>();

    public Distribution<T> with(Integer weight, T element)
    {
        total += weight;
        possibilities.add(new Entry(weight, element));

        return this;
    }

    public T getRandom()
    {
        int pick = random.nextInt(total) + 1;
        for (Entry entry : possibilities) {
            if (pick <= entry.weight) {
                return entry.element;
            }

            pick -= entry.weight;
        }

        throw new IllegalStateException();
    }

    private final class Entry
    {
        public final int weight;
        public final T   element;

        public Entry(int weight, T element)
        {
            this.weight = weight;
            this.element = element;
        }
    }
}
