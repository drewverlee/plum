(ns plum.person.sort-fns
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [clojure.spec.gen.alpha :as gen]))

(defn ascending
  [x y]
  (compare x y))

(defn descending
  [x y]
  (compare y x))

(defn- gender-order
  "Females > Males."
  [person]
  (->> person
       :gender
       str/lower-case
       {"female" 1 "male" -1}))

(defn- gender-and-lastname
  "Return a sorted collection.
  Sort order: Females > Males then by last name ascending."
  [coll]
  (sort  #(compare [(gender-order %2) (:last-name %1)]
                   [(gender-order %1) (:last-name %2)])
         coll))

(def name->fn
  {"last-name" #(sort-by :last-name descending %)
   "date-of-birth" #(sort-by :date-of-birth ascending %)
   "gender-and-lastname" gender-and-lastname})

(def names (set (keys name->fn)))

(s/def ::names
  (st/spec names
   {:name "sort functions"
    :description "A set of person sort functions."
    :json-schema/example (first names)
    :json-schema/default (first names)}))

(defn sort-with
  [sort-name coll]
  ((name->fn sort-name) coll))
