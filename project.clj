(defproject plum "0.1.0-SNAPSHOT"
  :description "Sorts records"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha19"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/test.check "0.10.0-alpha2"]
                 [phrase "0.1-alpha1"]]
  :main ^:skip-aot plum.core
  :target-path "target/%s"
  :plugins [[lein-cloverage "1.0.9"]]
  :profiles {:uberjar {:aot :all}})
