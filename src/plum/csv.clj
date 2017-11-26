(ns plum.csv
  (:refer-clojure :exclude [peek])
  (:require [clojure.data.csv :as cd-csv]
            [clojure.java.io :as io]
            [semantic-csv.core :as sc]
            [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [plum.person :as person]
            [clojure.string :as str]))

(def delimiters #{"," "|" " "})
(def delimiter (s/gen delimiters))

(s/def ::delimiter
  (s/with-gen #(string? %)
    (constantly delimiter)))

(def record
  (gen/fmap #(str/join (first %) (rest %))
            (s/gen (s/cat :delimiter ::delimiter :person ::person/entity))))

(s/def ::record
  (s/with-gen #(string? %)
    (constantly record)))

(defn get-delimiter
  [s]
  (.charAt
   (re-find #"[,|/|| ]" s)
   0))

(defn peek
  "Returns first line of a file"
  [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (first (line-seq rdr))))

(defn ->record
  "Returns a vector of records
  :--
  |`csv`       | str                | path to csv file
  | `header`   | vector of keywords | describes csv columns
  | `delimiter`| char               | designates column separator's
  | `cast`     | map->fn            | fns transform columns
  "
  [csv header delimiter cast]
  (with-open [reader (io/reader csv)]
    (->>
     (cd-csv/read-csv reader :separator delimiter)
     (sc/mappify {:header header})
     (sc/cast-with cast)
     doall)))

(defn ->person
  "Returns a vector of people"
  [delimiter csv]
  (let [header person/attributes
        cast {:date-of-birth #(f/parse person/date-of-birth-formatter %)}]
    (->record csv header delimiter cast)))
