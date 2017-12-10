(ns plum.person.request
  (:require [spec-tools.spec :as spec]
            [clj-time.format :as f]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [plum.person.core :as core]
            [plum.person.model :as model]))

(s/def ::person spec/string?)

(defn date-of-birth->date-time
  [dob]
  (f/parse core/date-of-birth-formatter dob))

(defn ->model
  "returns a person hash map
   --------------
  `person-line` | `a str of person attributes joined by a separator`"
  [person-line]
  (update-in
   (apply assoc {} (interleave (map (comp keyword name) model/attributes) (str/split person-line (re-pattern (str "\\" (core/get-separator person-line))))))
   [:date-of-birth]
   date-of-birth->date-time))
