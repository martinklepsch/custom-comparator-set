(ns org.martinklepsch.cc-set.impl)

;; First arg is for matching the CLJS API
(deftype CustomComparatorSet [_ data comparator]
  clojure.lang.IPersistentCollection
  (cons [this v] (if (contains? this v)
                   this
                   (CustomComparatorSet. nil (assoc data (comparator v) v) comparator)))
  (empty [this] (CustomComparatorSet. nil {} comparator))
  (equiv [this o] (= (set (seq this)) (set (seq o))))
  (seq [this] (vals data))

  clojure.lang.Counted
  (count [this] (count data))

  clojure.lang.IPersistentSet
  (disjoin [this v] (CustomComparatorSet. nil (dissoc data (comparator v)) comparator))
  (contains [this v] (boolean ((-> data keys set) (comparator v))))
  (get [this k] (get data k))

  Object
  (toString [this] (pr-str (seq data))))
