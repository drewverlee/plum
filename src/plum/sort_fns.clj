(ns plum.sort-fns
  (:require [clojure.string :as str]))

(defn ascending
  [x y]
  (compare x y))

(defn descending
  [x y]
  (compare y x))

(defn gender-order
  "Females > Males."
  [person]
  (->> person
       :gender
       str/lower-case
       {"female" 1 "male" -1}))

(defn gender-and-lastname
  [coll]
  (sort  #(compare [(gender-order %2) (:last-name %1)]
                   [(gender-order %1) (:last-name %2)])
         coll))

(def user-choosen-fn->fn-implmentation
  {"last-name" #(sort-by :last-name descending %)
   "date-of-birth" #(sort-by :birth-date ascending %)
   "gender-and-lastname" gender-and-lastname})

(def names (set (keys user-choosen-fn->fn-implmentation)))
