(ns plum.command-line
  (:require [clojure.spec.alpha :as s]
            [clojure.java.io :as io]))

(def cli-fn-names #{"sort"})
(def sort-fn-names #{"last-name" "birth-date" "gender-and-lastname"})

(s/def ::sort-fn-name #(= "sort" %))
(s/def ::sort-fn #(get sort-fn-names %))
(s/def ::input-csv #(.canRead (io/as-file %)))
(s/def ::output-csv #(.canWrite (io/as-file %)))

(s/def ::sort (s/cat :sort-fn-name ::sort-fn-name
                     :sort-fn ::sort-fn
                     :input-csv ::input-csv
                     :output-csv ::output-csv))

(s/def ::cli-fns #(get cli-fn-names (first %)))
(s/def ::cli-input (s/and ::cli-fns (s/or :sort ::sort)))
