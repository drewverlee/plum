(defproject plum "0.1.0-SNAPSHOT"
  :description "Sorts person records"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha19"]
                 [org.clojure/test.check "0.10.0-alpha2"]
                 [clj-time "0.14.0"]
                 [metosin/compojure-api "2.0.0-alpha16"]]
  :ring {:handler plum.handler/app}
  :uberjar-name "plum.jar"
  :uberware-name "plum.war"
  :plugins [[lein-cloverage "1.0.9"]]
  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                   :plugins [[lein-ring "0.12.0"]]}})
