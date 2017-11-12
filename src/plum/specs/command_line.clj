(ns plum.specs.command-line
  (:require [clojure.spec.alpha :as s]
            [plum.sort.functions :as sort-fns]
            [clojure.java.io :as io]))

(s/def ::sort-fn-name #(= "sort" %))
(s/def ::sort-fn #(get sort-fns/names %))
(s/def ::input-csv #(.canRead (io/as-file %)))
(s/def ::output-csv #(.canWrite (io/as-file %)))

(s/def ::sort (s/cat :sort-fn-name ::sort-fn-name
                     :sort-fn ::sort-fn
                     :input-csv ::input-csv
                     :output-csv ::output-csv))

(s/def ::cli-fns #(get #{"sort"} (first %)))
(s/def ::cli-input (s/and ::cli-fns (s/or :sort ::sort)))

(defn invalid-args?
  [args]
  (not (s/valid? ::cli-input args)))
