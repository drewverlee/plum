(ns plum.person
  (:require [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [semantic-csv.core :as sc]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.data.csv :as cd-csv]
            [clojure.string :as str]))

(def date-of-birth-formatter (f/formatter "MM/dd/yyyy"))

(defn date-time->date-of-birth
  [dt]
  (f/unparse date-of-birth-formatter  dt))

(defn date-of-birth->date-time
  [dob]
  (f/parse date-of-birth-formatter dob))

(def date-gen
  (gen/fmap  #(f/unparse  (c/from-date %))
             (s/gen (s/inst-in #inst "1800" #inst "2020"))))

(s/def ::date-of-birth
  (s/with-gen string?
    (constantly date-gen)))

(def separators #{"," "|" " "})
(def separator (s/gen separators))

(s/def ::separator
  (s/with-gen string?
    (constantly separator)))

(def base-spec
  (s/with-gen (s/and #(not (separator %))  #(> (count %) 1))
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

(def record
  (gen/fmap #(str/join (first %) (rest %))
            (s/gen (s/cat :separator ::separator :person ::entity))))

(s/def ::record
  (s/with-gen string?
    (constantly record)))

(defn ->vals-as-str
  [separator person]
  (as-> person p
    (cond-> p
      (contains? p :date-of-birth) (update :date-of-birth #(f/unparse date-of-birth-formatter %)))
    (vals p)
    (str/join separator p)))

(def attributes (take-nth 2 (rest (s/form ::entity))))


(defn ->csv
  [separator output-file persons]
  (with-open [writer (io/writer output-file)]
    (as-> persons p
          (sc/cast-with {:date-of-birth #(f/unparse date-of-birth-formatter %)} p)
          (sc/vectorize p)
          (cd-csv/write-csv writer p :separator separator))))

