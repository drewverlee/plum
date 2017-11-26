(ns plum.person
  (:require [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clojure.data.csv :as cd-csv]
            [semantic-csv.core :as sc]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as str]))

(def date-of-birth-formatter (f/formatter "MM/dd/yyyy"))

(defn date-time->date-of-birth
  [dt]
  (f/unparse date-of-birth-formatter  dt))

(def date-gen
  (gen/fmap  #(f/unparse  (c/from-date %))
             (s/gen (s/inst-in #inst "1920" #inst "1985"))))

(s/def ::date-of-birth
  (s/with-gen string?
    (constantly date-gen)))

(def base-spec
  (s/with-gen (s/and #(not (#{"," "|" " "} %))  #(> (count %) 1))
    #(s/gen string?)))

(s/def ::last-name base-spec)
(s/def ::first-name base-spec)
(s/def ::gender base-spec)
(s/def ::favorite-color base-spec)

(s/def ::entity (s/cat :last-name ::last-name
                       :first-name ::first-name
                       :gender ::gender
                       :favorite-color ::favorite-color
                       :date-of-birth ::date-of-birth))

(defn ->vals-as-str
  [delimeter person]
  (as-> person p
    (cond-> p
      (contains? p :date-of-birth) (update :date-of-birth #(f/unparse date-of-birth-formatter %)))
    (vals p)
    (str/join delimeter p)))

(def attributes (take-nth 2 (rest (s/form ::entity))))

(defn ->csv
  [delimiter output-file persons]
  (with-open [writer (io/writer output-file)]
    (->> persons
         (sc/cast-with {:date-of-birth #(f/unparse date-of-birth-formatter %)})
         sc/vectorize
         (cd-csv/write-csv writer))))

