package com.example.service;

import java.util.*;

public class CollectionsUtil {
    public static <T> Set<T> intersect(Collection<? extends Collection<T>> collections) {
        if(collections.isEmpty()) return Collections.emptySet();
        Collection<T> smallest
                = Collections.min(collections, Comparator.comparingInt(Collection::size));
        HashSet<T> result=new HashSet<>(smallest);
        result.removeIf(t -> collections.stream().anyMatch(c -> c!=smallest&& !c.contains(t)));
        return result;
    }
}
