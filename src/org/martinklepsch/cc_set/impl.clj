(ns org.martinklepsch.cc-set.impl)

(deftype CustomComparatorSet [data comparator]
  clojure.lang.IPersistentCollection
  (cons [this v] (if (contains? this v)
                   this
                   (CustomComparatorSet. (assoc data (comparator v) v) comparator)))
  (empty [this] (CustomComparatorSet. {} comparator))
  (equiv [this o] (= (set (seq this)) (set (seq o))))
  (seq [this] (vals data))

  clojure.lang.Counted
  (count [this] (count data))

  clojure.lang.IPersistentSet
  (disjoin [this v] (CustomComparatorSet. (dissoc data (comparator v)) comparator))
  (contains [this v] (boolean (contains? data (comparator v))))
  (get [this k] (get data k))

  java.util.Collection
  (size [this] (count data))

  clojure.lang.IHashEq
  (hasheq [this] (hash-unordered-coll (-> data vals set)))

  Iterable
  (iterator [this]
    (clojure.lang.SeqIterator. (seq this)))

  Object
  (equals [this that]
    (cond
      (identical? this that)    true
      (= (seq this) (seq that)) true
      :else false))
  (hashCode [this] (.hashCode data))
  (toString [this] (pr-str this)))

(comment
  (.equals (CustomComparatorSet. {"a" {:id "a"}} :id) #{})
  ;; (.hashCode (CustomComparatorSet. {} :id))
  (.hashCode #{})
  (hash #{})
  (hash (CustomComparatorSet. {} :id))
  (hash {}))

(defmethod print-method CustomComparatorSet [v ^java.io.Writer w]
  (.write w "#CustomComparatorSet{")
  (if (seq v)
    (loop [[x & xs] (seq v)]
      (print-method x w)
      (when (seq xs) (.write w " ") (recur xs))))
  (.write w "}"))
