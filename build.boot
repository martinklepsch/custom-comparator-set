(set-env! :source-paths #{"src"}
          :dependencies '[[adzerk/boot-test "1.1.2" :scope "test"]
                          [crisptrutski/boot-cljs-test "0.3.0" :scope "test"]
                          [adzerk/bootlaces "0.1.13" :scope "test"]
                          [collection-check "0.1.7" :scope "test"]
                          [org.clojure/clojurescript "1.9.293" :scope "provided"]
                          ;; [org.clojure/test.check "0.9.0" :scope "test"]
                          ])

(require '[adzerk.boot-test :refer [test]]
         '[crisptrutski.boot-cljs-test :refer [test-cljs]]
         '[adzerk.bootlaces :as b :refer [build-jar push-release push-snapshot]])

(def +version+ "0.1.0-SNAPSHOT")
(b/bootlaces! +version+)

(task-options! pom {:project 'org.martinklepsch/cc-set
                    :version +version+
                    :description "A Clojure/Script library for custom comparator sets"
                    :url "https://github.com/martinklepsch/custom-comparator-set"
                    :scm {:url "https://github.com/martinklepsch/custom-comparator-set"}
                    :license {"MPL v2" "https://www.mozilla.org/en-US/MPL/2.0/"}}
               push {:repo-map {:creds :gpg}
                     :gpg-sign true})

(deftask pushs
  "Deploy snapshot version to Clojars."
  [f file PATH str "The jar file to deploy."]
  (push :file file :ensure-snapshot true))

(deftask testing []
  (merge-env! :source-paths #{"test"})
  (let [nss #{'org.martinklepsch.cc-set-test}]
    (task-options! test      {:namespaces nss}
                   test-cljs {:namespaces nss}))
  identity)

(deftask test-cljc []
  (comp (test-cljs) (test)))
