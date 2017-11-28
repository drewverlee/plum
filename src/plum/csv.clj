(ns plum.csv
  (:require [semantic-csv.core :as sc]
            [plum.person :as person]))

(defn ->person
  "Returns a vector of people"
  [csv]
  (sc/slurp-csv csv
                :header person/attributes
                :cast-fns {:date-of-birth person/date-of-birth->date-time}))
