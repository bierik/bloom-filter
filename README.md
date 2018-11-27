# Bloom fitler

A Bloom filter is a space-efficient probabilistic data structure,
conceived by Burton Howard Bloom in 1970,
that is used to test whether an element is a member of a set.
False positive matches are possible, but false negatives are not
So a query can tell if an element is possibly in the set or definitely not in the set.
Elements can be added to the set, but not removed.
The more elements that are added to the set,
the larger the probability of false positives.