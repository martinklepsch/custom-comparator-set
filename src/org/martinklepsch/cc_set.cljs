(ns org.martinklepsch.cc-set
  (:require [clojure.string :as string]))

(deftype CustomComparatorSet [meta data-map comparator]
  IWithMeta
  (-with-meta [coll meta] (CustomComparatorSet. meta data-map comparator))

  IMeta
  (-meta [coll] meta)

  ICloneable
  (-clone [_] (CustomComparatorSet. meta hash-map comparator))

  IIterable
  (-iterator [_] (HashSetIter. (-iterator data-map)))
  
  ICollection
  (-conj [coll v] (if (contains? coll v)
                   coll
                   (CustomComparatorSet. meta (assoc data-map (comparator v) v) comparator)))

  IEmptyableCollection
  (-empty [coll] (CustomComparatorSet. meta {} comparator))

  IEquiv
  (-equiv [coll o] (= (set (seq coll)) (set (seq o))))

  ISeqable
  (-seq [_] (vals data-map))

  ICounted
  (-count [_] (count data-map))

  ISet
  (-disjoin [_ v] (CustomComparatorSet. meta (dissoc data-map (comparator v)) comparator))

  ILookup
  (-lookup [coll v]
    (-lookup coll v nil))
  (-lookup [coll v not-found]
    (-lookup data-map v not-found))
  
  Object
  (toString [_] (pr-str (seq data-map))))

(defn set-by
  "High level constructor for custom comparator sets.
   Throws if comparator returns nil."
  [comparator & keys]
  (let [cmp (fn [i] (if (nil? (comparator i))
                      (throw (ex-info "Custom set comparator returned nil" {:item i}))
                      (comparator i)))]
    (reduce conj (CustomComparatorSet. nil {} cmp) keys)))

;; (defmethod print-method CustomComparatorSet [v ^java.io.Writer w]
;;   (.write w (str "#CustomComparatorSet{" (string/join " " (map pr-str (seq v))) "}")))

(comment
  (def x (CustomComparatorSet. {} :id))
  (conj x {:id 1} {:id 2})

  (set (seq x))

  (contains? (CustomComparatorSet. {"a" {:thing "a"}} :thing) {:thing "a"})
  (disj (CustomComparatorSet. {"a" {:thing "a"}} :thing) {:thing "a"})

  (set-by :thing {:thing "a"} {:thing "x"} {:thing "b"})

  )
