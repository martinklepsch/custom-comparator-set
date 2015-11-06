(ns org.martinklepsch.cc-set-test
  (:require [org.martinklepsch.cc-set :as ccset]
            ;; [clojure.test.check :as tc]
            ;; [clojure.test.check.generators :as gen]
            ;; [clojure.test.check.properties :as prop]
            #?(:clj [clojure.test :as t :refer [deftest]]
               :cljs [cljs.test :as t :refer-macros [deftest]])))

(deftest equality
  (t/is (= (ccset/set-by :k {:k "a"})
           (ccset/set-by :k {:k "a"}))))

(deftest counting
  (t/is (= 0 (count (ccset/set-by :k))))
  (t/is (= 1 (count (ccset/set-by :k {:k "a"} {:k "a"}))))
  (t/is (= 2 (count (ccset/set-by :k {:k "a"} {:k "b"})))))

(deftest emptying
  (t/testing "empty ccset"
    (t/is (= 0 (count (empty (ccset/set-by :k {:k "a"})))))
    (t/is (= (ccset/set-by :k)
             (empty (ccset/set-by :k {:k "a"}))))))

(deftest adding-values
  (t/testing "conj value to empty ccset"
    (t/is (= (ccset/set-by :k {:k "a"})
             (conj (ccset/set-by :k) {:k "a"}))))
  (t/testing "conj value already in ccset"
    (t/is (= (ccset/set-by :k {:k "a"})
             (conj (ccset/set-by :k {:k "a"}) {:k "a"}))))
  (t/testing "conj value not yet in ccset"
    (t/is (= (ccset/set-by :k {:k "a"} {:k "b"})
             (conj (ccset/set-by :k {:k "a"}) {:k "b"})))))

(deftest subtracting-values
  (t/testing "disj value not in ccset"
    (t/is (= (ccset/set-by :k {:k "a"})
             (disj (ccset/set-by :k {:k "a"}) {:k "b"}))))
  (t/testing "disj value in ccset"
    (t/is (= (ccset/set-by :k)
             (disj (ccset/set-by :k {:k "a"}) {:k "a"})))))

(deftest getting-values
  (t/testing "get key not in ccset"
    (t/is (= nil (get (ccset/set-by :k {:k "a"}) "b"))))
  (t/testing "do not get value by value as in regular sets"
    (t/is (= nil (get (ccset/set-by :k {:k "a"}) {:k "b"}))))
  (t/testing "get value in ccset"
    (t/is (= {:k "b"}
             (-> (ccset/set-by :k {:k "a"} {:k "b"})
                 (get "b"))))))
