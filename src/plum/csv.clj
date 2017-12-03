(ns plum.csv
  (:refer-clojure :exclude [peek])
  (:require [clojure.data.csv :as cd-csv]
            [clojure.java.io :as io]
            [semantic-csv.core :as sc]
            [clj-time.format :as f]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as str]
            [plum.person :as person]))

(def separators #{"," "|" " "})
(def separator (s/gen separators))

(s/def ::separator
  (s/with-gen string?
    (constantly separator)))

(def record
  (gen/fmap #(str/join (first %) (rest %))
            (s/gen (s/cat :separator ::separator :person ::person/entity))))

(s/def ::record
  (s/with-gen string?
    (constantly record)))

(defn get-separator
  [s]
  (.charAt
   (re-find #"[,|/|| ]" s)
   0))

(defn peek
  "Returns first line of a file"
  [file]
  (with-open [rdr (io/reader file)]
    (first (line-seq rdr))))

(defn ->record
  "Returns a vector of records
  :--
  |`csv`       | str                | path to csv file
  | `header`   | vector of keywords | describes csv columns
  | `separator`| char               | designates column separator's
  | `cast`     | map->fn            | fns transform columns
  "
  [csv header separator cast]
  (with-open [reader (io/reader csv)]
    (->>
     (cd-csv/read-csv reader :separator separator)
     (sc/mappify {:header header})
     (sc/cast-with cast)
     doall)))

(defn ->person
  "Returns a vector of people"
  [separator csv]
  (let [header person/attributes
        cast {:date-of-birth #(f/parse person/date-of-birth-formatter %)}]
    (->record csv header separator cast)))

