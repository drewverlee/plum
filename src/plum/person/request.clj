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
    :json-schema/example "john,smith,male,blue,04/07/1950"
    :json-schema/default "john,smith,male,blue,04/07/1950"}))

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

;;(->model "john,smith,male,blue,04/07/1950")

;;=>{:last-name "john", :first-name "smith", :gender "male", :favorite-color "blue", :date-of-birth #object[org.joda.time.DateTime 0x7756506b "1950-04-07T00:00:00.000Z"]}
