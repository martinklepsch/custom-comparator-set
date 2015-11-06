(ns org.martinklepsch.cc-set
  (:require [clojure.string :as string]))

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
  (disjoin [this v] (-> (dissoc data (comparator v))
                        (CustomComparatorSet. comparator)))
  (contains [this v] (boolean ((-> data keys set) (comparator v))))
  (get [this k] (get data k))
  
  Object
  (toString [this] (pr-str (seq data))))

(defn set-by
  "High level constructor for custom comparator sets.
   Throws if comparator returns nil."
  [comparator & keys]
  (let [cmp (fn [i] (if (nil? (comparator i))
                      (throw (ex-info "Custom set comparator returned nil" {:item i}))
                      (comparator i)))]
    (reduce conj (CustomComparatorSet. {} cmp) keys)))

(defmethod print-method CustomComparatorSet [v ^java.io.Writer w]
  (.write w (str "#CustomComparatorSet{" (string/join " " (map pr-str (seq v))) "}")))

(comment
  (def x (CustomComparatorSet. {} :id))
  (conj x {:id 1} {:id 2})

  (set (seq x))

  (contains? (CustomComparatorSet. {"a" {:thing "a"}} :thing) {:thing "a"})
  (disj (CustomComparatorSet. {"a" {:thing "a"}} :thing) {:thing "a"})

  (set-by :thing {:thing "a"} {:thing "x"} {:thing "b"})

)
