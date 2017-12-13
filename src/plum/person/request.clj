(ns plum.person.request
  (:require [spec-tools.core :as st]
            [clj-time.format :as f]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [plum.person.core :as core]
            [compojure.api.sweet :as sweet]
            [plum.person.model :as model]))

(s/def ::person
  (st/spec
   {:spec string?
    :name "person"
    ;;NOTE: not showing up in schema like is promised
    :description "A set of person attributes joined by a separator."
    :type :string
    :json-schema/example "smith,john,male,blue,04/07/1950"
    :json-schema/default "smith,john,male,blue,04/07/1950"}))

(defn- date-of-birth->date-time
  [dob]
  (f/parse core/date-of-birth-formatter dob))

(defn ->model
  "returns a person hash map
   --------------
  `person` | `a str of person attributes joined by a separator`"
  [person]
  (update-in
   (apply assoc {} (interleave (map (comp keyword name) model/attributes) (str/split person (re-pattern (str "\\" (core/get-separator person))))))
   [:date-of-birth]
   date-of-birth->date-time))

;; Example of turning person string into person model
;;(->model "smith,john,male,blue,04/07/1950")

;; {:last-name "smith", :first-name "john", :gender "male", :favorite-color "blue", :date-of-birth #object[org.joda.time.DateTime 0x75c38812 "1950-04-07T00:00:00.000Z"]}
