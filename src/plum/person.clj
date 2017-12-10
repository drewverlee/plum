(ns plum.person
  (:require [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as str]
            [plum.person :as person]))

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

(s/def ::date-of-birth-obj
  (s/and
   (s/conformer date-of-birth->date-time date-time->date-of-birth)))

(def separators #{"," "|" " "})

(def separator (gen/elements separators))

(s/def ::separator
  (s/with-gen string?
    (constantly separator)))

(def base-spec
  (s/with-gen (s/and #(not (separators %))  #(> (count %) 0))
    #(s/gen string?)))

(s/def ::last-name base-spec)
(s/def ::first-name base-spec)
(s/def ::gender base-spec)
(s/def ::favorite-color base-spec)

(s/def ::record (s/keys :req [::last-name ::first-name ::gender ::favorite-color ::date-of-birth]))

(defn get-separator
  [s]
  (.charAt
   (re-find (re-pattern (str "[/" (str/join "|/" separators) "]")) s)
   0))

(def attributes (last (s/form ::record)))

(s/def ::row
  (s/and
   (s/conformer #(str/split % (re-pattern (str "\\" (get-separator %)))))
   (s/cat :last-name ::last-name :first-name ::first-name :gender ::gender :favorite-color ::favorite-color :date-of-birth ::date-of-birth-obj)))

;; for display purposes
(def sep (str/join " or " (replace {" " "blankspace"} separators)))
(def attr (str/join "," (map name attributes)))


(-> (s/conform ::row "a,b,c,d,4/7/1980")
    (update-in [:date-of-birth] date-time->date-of-birth))

(def people (atom []))

(defn add! [person]
  (swap! people (fn [people] (conj people person)))
  person)









