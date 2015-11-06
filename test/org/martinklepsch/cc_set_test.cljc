(ns org.martinklepsch.cc-set-test
  (:require [org.martinklepsch.cc-set :as ccset]
            ;; [clojure.test.check :as tc]
            ;; [clojure.test.check.generators :as gen]
            ;; [clojure.test.check.properties :as prop]
            #?(:clj [clojure.test :as t :refer [deftest]]
               :cljs [cljs.test :as t :refer-macros [deftest]])))

(deftest single-argument-arity
  (t/is (empty? (ccset/custom-comparator-set :k))))

(deftest equality
  (t/is (= (ccset/custom-comparator-set :k {:k "a"})
           (ccset/custom-comparator-set :k {:k "a"}))))

(deftest counting
  (t/is (= 0 (count (ccset/custom-comparator-set :k))))
  (t/is (= 1 (count (ccset/custom-comparator-set :k {:k "a"} {:k "a"}))))
  (t/is (= 2 (count (ccset/custom-comparator-set :k {:k "a"} {:k "b"})))))

(deftest emptying
  (t/testing "empty ccset"
    (t/is (= 0 (count (empty (ccset/custom-comparator-set :k {:k "a"})))))
    (t/is (= (ccset/custom-comparator-set :k)
             (empty (ccset/custom-comparator-set :k {:k "a"}))))))

(deftest adding-values
  (t/testing "conj value to empty ccset"
    (t/is (= (ccset/custom-comparator-set :k {:k "a"})
             (conj (ccset/custom-comparator-set :k) {:k "a"}))))
  (t/testing "conj value already in ccset"
    (t/is (= (ccset/custom-comparator-set :k {:k "a"})
             (conj (ccset/custom-comparator-set :k {:k "a"}) {:k "a"}))))
  (t/testing "conj value not yet in ccset"
    (t/is (= (ccset/custom-comparator-set :k {:k "a"} {:k "b"})
             (conj (ccset/custom-comparator-set :k {:k "a"}) {:k "b"})))))

(deftest subtracting-values
  (t/testing "disj value not in ccset"
    (t/is (= (ccset/custom-comparator-set :k {:k "a"})
             (disj (ccset/custom-comparator-set :k {:k "a"}) {:k "b"}))))
  (t/testing "disj value in ccset"
    (t/is (= (ccset/custom-comparator-set :k)
             (disj (ccset/custom-comparator-set :k {:k "a"}) {:k "a"})))))

(deftest getting-values
  (t/testing "get key not in ccset"
    (t/is (= nil (get (ccset/custom-comparator-set :k {:k "a"}) "b"))))
  (t/testing "do not get value by value as in regular sets"
    (t/is (= nil (get (ccset/custom-comparator-set :k {:k "a"}) {:k "b"}))))
  (t/testing "get value in ccset"
    (t/is (= {:k "b"}
             (-> (ccset/custom-comparator-set :k {:k "a"} {:k "b"})
                 (get "b"))))))
