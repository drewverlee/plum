(ns plum.commandLine
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [phrase.alpha :refer :all]
            [com.gfredericks.like-format-but-with-named-args :refer [named-format]]))


(def cli-fn-msg "The base function")
(def input-csv-msg "The existing csv location ")
(def sort-csv-msg "The sort function to apply to the input csv")
(def output-csv-msg "The existing csv output location")

(def usage (->> ["Plum helps you sort csv's of people records!" ""
                 "Usage: sort sort-fn input-csv output-csv"
                  (str "sort        : " cli-fn-msg)
                  (str "sort-fn   : " sort-csv-msg)
                  (str "input-csv : " input-csv-msg)
                  (str "output-csv: " output-csv-msg)
                  ""]
                (str/join \newline)))

(def cli-fn-names #{"sort"})
(def sort-fn-names #{"last-name" "birth-date" "gender-and-lastname"})

(s/def ::sort-fn-name #(= "sort" %))
(s/def ::sort-fn-arg #(get sort-fn-names %))
(s/def ::input-csv-arg #(.canRead (io/as-file %)))
(s/def ::output-csv-arg #(.canWrite (io/as-file %)))

(s/def ::sort (s/cat :sort-fn-name ::sort-fn-name
                     :sort-fn-arg ::sort-fn-arg
                     :input-csv-arg ::input-csv-arg
                     :output-csv-arg ::output-csv-arg))

(s/def ::foo (s/cat :one #(= "a" %) :two #(= "b" %)))
(s/def ::cli-fns #(get cli-fn-names (first %)))
(s/def ::cli-input (s/and ::cli-fns (s/or :sort ::sort)))

(defn get-problem-map-leaf
  [spec x]
  (let [problem (first (:clojure.spec.alpha/problems (s/explain-data spec x)))]
    {:spec (last (:via problem))
     :val  (:val problem)
     :pos  (first (:in problem))
     :reason (:reason problem)
     :path (:path problem)}))

(defmulti user-friendly-msg (fn [ctx spec x m] [ctx spec]))

(defmethod user-friendly-msg [:cli ::sort-fn-arg]
  [ctx spec x m]
  (named-format "%msg~s {%val~s} in position {%pos~s} isn't in the list of accepted functions: %sort-fns~s"
                (merge (get-problem-map-leaf spec x) {:msg sort-csv-msg :sort-fns (str/join "," sort-fn-names)} m)))

(defmethod user-friendly-msg [:cli ::input-csv-arg]
  [ctx spec x m]
  (named-format "%msg~s {%val~s} in position {%pos~s} can't be read!"
                (merge (get-problem-map-leaf spec x) {:msg input-csv-msg} m)))

(defmethod user-friendly-msg [:cli ::output-csv-arg]
  [ctx spec x m]
  (named-format "%msg~s {%val~s} in position {%pos~s} can't be written to!"
                (merge (get-problem-map-leaf spec x) {:msg output-csv-msg} m)))

(defmethod user-friendly-msg [:cli ::cli-fns]
  [ctx spec x m]
  (let [{val :val pos :pos} (get-problem-map-leaf spec x)]
    (named-format "%msg~s {%val~s} in position {%pos~s} isn't in the list of accepted functions: %cli-fns~s" {:pos pos
                                                                                                              :msg cli-fn-msg
                                                                                                              :val (first val)
                                                                                                              :cli-fns (str/join "," cli-fn-names)})))
(defmethod user-friendly-msg [:cli nil]
  [ctx spec x m]
  "success")

(defmethod user-friendly-msg [:cli ::cli-input]
  [ctx spec x m]
  (let [{reason :reason path :path val :val leaf-spec :spec pos :pos} (get-problem-map-leaf spec x)]
    (if reason
      (named-format "%arg~s failed because %reason~s" {:arg (name (last path)) :reason reason}) 
      (user-friendly-msg ctx leaf-spec val {:pos pos}))))

(defn -main [& args]
  (if (not (s/valid? ::cli-input args))
    (do
      (user-friendly-msg :cli ::cli-input args {}))))

