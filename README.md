# Custom Comparator Sets in Clojure

**Alpha** A custom Clojure/Script type for custom comparator sets.

[](dependency)
```clojure
[org.martinklepsch/cc-set "0.1.2"] ;; latest release
```
[](/dependency)

### Rationale

When dealing with domain data in maps this data often has unique
identifiers. To make merging easy and guarantee that there are no two
items with the same identifier people often store those models in maps
of the following format:

```clojure
{(:id model) model
 ,,,}
```

While this certainly is a solution it complicates the handling of the
data in every place imaginable. Often you just want `(vals my-models)`
instead of the actual map. Adding new items requires code that is
tightly coupled to the underlying map that you only chose to guarantee
uniqueness of a certain property (`(assoc my-models (:id model)
model)`).

### Custom Sets

This library provides a custom type `CustomComparatorSet` which allows
users to manage data in a set-like datastructure while using a custom
function as uniqueness comparator.

```clojure
;; (import [org.martinklepsch.cc_set.impl CustomComparatorSet])            ; Clojure
;; (require '[org.martinklepsch.cc-set :refer [custom-comparator-set]]) ; ClojureScript
(def x (custom-comparator-set :id))
(conj x {:id 1} {:id 2})
;; => #CustomComparatorSet{{:id 1} {:id 2}}
```

> While a keyword `:id` is used as comparator here you can also use
> more complex functions.

A constructor `set-by` is provided which additionally wraps the passed
comparator in a nil check, throwing if `nil` is returned.

```clojure
(require '[org.martinklepsch.cc-set :refer [set-by]])
(set-by :id {:id "a"} {:id "x"} {:id "b"})
```

If you don't want the `nil` guard behavior you can implement your own constructor like this:

```clojure
(defn my-set-by [comparator & keys]
  (reduce conj (CustomComparatorSet. {} comparator) keys)))
```

Simple!

### Low level constructors

Clojure

```
(import [org.martinklepsch.cc_set.impl CustomComparatorSet])
(CustomComparatorSet. {} :id)
```

ClojureScript

```
(require '[org.martinklepsch.cc-set.impl :refer [CustomComparatorSet]])
(CustomComparatorSet. nil {} :id)
```
**Notice the different arity, resulting of the differences in how
  Clojure and ClojureScript handle metadata.**

#### Differences to regular sets

**get**

_Regular set_: `(get regular-set v)` looks for entries where `v == entry`

_Custom-comparator set_: `(get ccset v)` look for entries where `(comparator v) == (comparator entry)`

**conj**

_Regular set_: `(conj regular-set v)` will add the value only if the set does not already contain it, maintaining uniqueness by value equality

_Custom-comparator set_: `(conj ccset v)` will behave like `assoc` with the key `(comparator v)` and the value `v`, maintaining uniqueness by comparator. This means that values will be updated if the comparator result is equal but the value is different

### Building and Testing

```sh
boot build-jar                   # install to ~/.m2
boot testing test-cljc           # run tests once
boot testing watch test-cljc     # run tests after changes
```

### Contributing

Feedback and PRs welcome, especially generative testing seems like a good fit.

--

### License

MPLv2, see `LICENSE`.
