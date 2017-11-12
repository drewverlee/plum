(ns plum.specs.tools
  (:require [clojure.spec.alpha :as s]))

(defn get-problem-map-leaf
  [spec x]
  (let [problem (first (:clojure.spec.alpha/problems (s/explain-data spec x)))]
    {:spec (last (:via problem))
     :val  (:val problem)
     :pos  (first (:in problem))
     :reason (:reason problem)
     :path (:path problem)}))
