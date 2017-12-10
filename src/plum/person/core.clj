(ns plum.person.core
  (:require [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [spec-tools.spec :as spec]
            [clojure.string :as str]))

;; date of birth
(def date-of-birth-formatter (f/formatter "MM/dd/yyyy"))

(s/def ::date-of-birth
  (s/with-gen string?
    #(gen/fmap  (fn [d] (f/unparse date-of-birth-formatter (c/from-date d)))
                (s/gen (s/inst-in #inst "1800" #inst "2020")))))

;; separators
(def separators #{"," "|" " "})
(def separator (gen/elements separators))

(s/def ::separator
  (s/with-gen string?
    (constantly separator)))

(defn get-separator
  [s]
  (.charAt
   (re-find (re-pattern (str "[/" (str/join "|/" separators) "]")) s)
   0))

;; printing
(def sep (str/join " or " (replace {" " "blankspace"} separators)))

