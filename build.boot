(set-env! :source-paths #{"src" "test"}
          :dependencies '[[adzerk/boot-test "1.0.4" :scope "test"]
                          [adzerk/bootlaces "0.1.13" :scope "test"]
                          ;; TODO: use [org.clojure/test.check "0.8.2" :scope "test"]
                          ])

(require '[adzerk.boot-test :refer [test]])
(require '[adzerk.bootlaces :as b :refer [build-jar push-release push-snapshot]])

(def +version+ "0.1.0-SNAPSHOT")
(b/bootlaces! +version+)

(task-options! pom {:project 'org.martinklepsch/cc-set
                    :version +version+
                    :description "A Clojure/Script library for custom comparator sets"
                    :url "https://github.com/martinklepsch/custom-comparator-set"
                    :scm {:url "https://github.com/martinklepsch/custom-comparator-set"}
                    :license {"MPL v2" "https://www.mozilla.org/en-US/MPL/2.0/"}})
