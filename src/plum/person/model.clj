(ns plum.person.model
  "Functions and specs for a person map. Also contains a stateful atom for storing people (persons)."
  (:require [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as str]
            [plum.person.core :as core]
            [clojure.set :as set]))

;;date of birth
(s/def ::date-of-birth
  (s/with-gen #(instance? org.joda.time.DateTime %)
    #(gen/fmap  c/from-date
                (s/gen (s/inst-in #inst "1800" #inst "2020")))))

(defn- date-time->date-of-birth
  [dt]
  (f/unparse core/date-of-birth-formatter  dt))

;; person spec
(def base-spec
  (s/with-gen (s/and #(not (core/separators %))  #(pos? (count %)))
    #(s/gen string?)))

(s/def ::last-name base-spec)
(s/def ::first-name base-spec)
(s/def ::gender base-spec)
(s/def ::favorite-color base-spec)

(s/def ::person (s/keys :req-un [::last-name ::first-name ::gender ::favorite-color ::date-of-birth]))
(s/def ::people (s/coll-of ::person))

(def attributes (last (s/form ::person)))

-;; for printing
-(def attr (str/join "," (map name attributes)))

;; conversion
(defn ->response
  [person]
  (update-in person [:date-of-birth] date-time->date-of-birth))

;;State mechanism
(def people (atom [] :validator #(s/valid? ::people %)))
