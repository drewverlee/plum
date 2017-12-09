(ns plum.person
  (:require [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as str]))

(def date-of-birth-formatter (f/formatter "MM/dd/yyyy"))

(defn date-time->date-of-birth
  [dt]
  (f/unparse date-of-birth-formatter  dt))

(defn date-of-birth->date-time
  [dob]
  (f/parse date-of-birth-formatter dob))

(s/def ::date-of-birth
  (s/with-gen string?
    #(gen/fmap  (fn [d] (f/unparse date-of-birth-formatter (c/from-date d)))
                (s/gen (s/inst-in #inst "1800" #inst "2020")))))

(def separators #{"," "|" " "})
(def separator (gen/elements separators))

(s/def ::separator
  (s/with-gen string?
    (constantly separator)))

(def base-spec
  (s/with-gen (s/and #(not (separators %))  #(> (count %) 1))
    #(s/gen string?)))

(s/def ::last-name base-spec)
(s/def ::first-name base-spec)
(s/def ::gender base-spec)
(s/def ::favorite-color base-spec)


(s/def ::entity (s/keys :req [::last-name ::first-name ::gender ::favorite-color ::date-of-birth]))

(defn get-separator
  [s]
  (.charAt
   (re-find #"[,|/|| ]" s)
   0))

(defn parse-str [s re]
  (-> s
      (str/split re)))

(s/def ::record
  (s/with-gen string?
    #(gen/fmap (fn [{:keys [separator person]}] (str/join separator (vals person)))
               (gen/hash-map :separator (s/gen ::separator) :person (s/gen ::entity)))))

(def attributes (last (s/form ::entity)))

(s/conform ::record (first (first (s/exercise ::record 1))))
