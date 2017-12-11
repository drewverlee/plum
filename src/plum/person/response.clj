(ns plum.person.response
  (:require
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]
            [clj-time.format :as f]
            [clj-time.coerce :as c]
            [plum.person.model :as model]
            [plum.person.core :as core]))

(s/def ::person (s/keys :req-un [::model/last-name ::model/first-name ::model/gender ::model/favorite-color ::core/date-of-birth]))


(s/def ::people (s/coll-of ::person))

;; (s/valid? ::people [{:last-name "a", :first-name "b", :gender "c", :favorite-color "d", :date-of-birth "04/07/1800"}])
;; => true
