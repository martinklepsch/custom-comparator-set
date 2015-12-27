(ns org.martinklepsch.cc-set.impl)

(deftype CustomComparatorSet [data comparator]
  clojure.lang.IPersistentCollection
  (cons [this v] (if (contains? this v)
                   this
                   (CustomComparatorSet. (assoc data (comparator v) v) comparator)))
  (empty [this] (CustomComparatorSet. {} comparator))
  (equiv [this o] (= (set (seq this)) o))
  (seq [this] (vals data))

  clojure.lang.Counted
  (count [this] (count data))

  clojure.lang.IPersistentSet
  (disjoin [this v] (CustomComparatorSet. (dissoc data (comparator v)) comparator))
  (contains [this v] (boolean (contains? data (comparator v))))
  (get [this k] (get data k))

  clojure.lang.IHashEq
  (hasheq [this] (hash-unordered-coll (-> data vals set)))

  clojure.lang.IFn
  (invoke [this k] (get data k))

  java.lang.Iterable
  (iterator [this]
    (clojure.lang.SeqIterator. (seq this)))

  java.util.Collection
  (size [this] (count data))

  java.util.Set
  (isEmpty [this] (zero? (count this)))
  (toArray [this array]
    (.toArray ^java.util.Collection (sequence (seq data)) array))
  (toArray [this] (into-array (seq this)))
  (containsAll [this coll]
    (every? #(contains? this %) coll))

  Object
  (equals [this that]
    (cond (identical? this that)     true
          (= (-> this seq set) that) true
          :else false))
  (hashCode [this] (reduce #(+ %1 (clojure.lang.Util/hash %2)) 0 (seq this)))
  (toString [this] (pr-str this)))

(defmethod print-method CustomComparatorSet [v ^java.io.Writer w]
  (.write w "#CustomComparatorSet{")
  (if (seq v)
    (loop [[x & xs] (seq v)]
      (print-method x w)
      (when (seq xs) (.write w " ") (recur xs))))
  (.write w "}"))

(comment
  (.equals (CustomComparatorSet. {"a" {:id "a"}} :id) #{})
  (.hashCode (CustomComparatorSet. {} :id))
  (.hashCode #{})
  (hash #{})
  (hash (CustomComparatorSet. {} :id))
  (hash {})

  (def x (CustomComparatorSet. {} :id))

  (conj x {:id 1} {:id 2})

  (set (seq x))

  (contains? (CustomComparatorSet. {"a" {:thing "a"}} :thing) {:thing "a"})
  (disj (CustomComparatorSet. {"a" {:thing "a"}} :thing) {:thing "a"})

  (set-by :thing {:thing "a"} {:thing "x"} {:thing "b"})

  (require 'collection-check)

  (collection-check/assert-set-like (set-by :x) clojure.test.check.generators/int)

  (.equals #{} (set-by :x))

  (set-by :x)

  )
