(defproject plum "0.1.0-SNAPSHOT"
  :description "Sorts person records"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha19"]
                 [org.clojure/test.check "0.10.0-alpha2"]
                 [clj-time "0.14.0"]]
  :main ^:skip-aot plum.core
  :target-path "target/%s"
  :plugins [[lein-cloverage "1.0.9"]]
  :profiles {:uberjar {:aot :all}})
