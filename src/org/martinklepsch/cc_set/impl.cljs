(ns org.martinklepsch.cc-set.impl
  (:require [clojure.string :as string]))

(deftype CustomComparatorSet [meta data-map comparator]
  IWithMeta
  (-with-meta [coll meta] (CustomComparatorSet. meta data-map comparator))

  IMeta
  (-meta [coll] meta)

  ICloneable
  (-clone [_] (CustomComparatorSet. meta data-map comparator))

  IIterable
  (-iterator [_] (HashSetIter. (-iterator data-map)))

  ICollection
  (-conj [coll v] (if (contains? coll v)
                    coll
                    (CustomComparatorSet. meta (assoc data-map (comparator v) v) comparator)))

  IEmptyableCollection
  (-empty [coll] (CustomComparatorSet. meta {} comparator))

  IEquiv
  (-equiv [coll o] (= (set (seq coll)) o))

  ISeqable
  (-seq [_] (vals data-map))

  ICounted
  (-count [_] (count data-map))

  ISet
  (-disjoin [_ v] (CustomComparatorSet. meta (dissoc data-map (comparator v)) comparator))

  ILookup
  (-lookup [coll v] (-lookup coll v nil))
  (-lookup [coll v not-found] (-lookup data-map v not-found))

  IHash
  (-hash [coll] (hash-combine (hash comparator) (hash (seq coll))))

  IPrintWithWriter
  (-pr-writer [coll writer _]
    (let [items (string/join " " (map pr-str (seq coll)))]
      (-write writer (str "#CustomComparatorSet{" items "}"))))

  Object
  (toString [_] (pr-str (seq data-map))))
