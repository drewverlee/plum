(ns plum.commandLine
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [phrase.alpha :refer :all]
            [clojure.tools.cli :as cli]
            [com.gfredericks.like-format-but-with-named-args :refer [named-format]]))


(def cli-fn-msg "The base function")
(def input-csv-msg "The existing csv location ")
(def sort-csv-msg "The sort function to apply to the input csv")
(def output-csv-msg "The existing csv output location")

(defn usage
  [summery]
  (->> ["Sorts csvs"
        "Usage: sort sort-fn input-csv output-csv"
                  (str "arg       | pos | name ")
                  (str "sort      |  0  | " cli-fn-msg)
                  (str "sort-fn   |  1  | " sort-csv-msg)
                  (str "input-csv |  2  | " input-csv-msg)
                  (str "output-csv|  3  | " output-csv-msg)
                  ""]
       (str/join \newline)))


(def cli-fn-names #{"sort"})
(def sort-fn-names #{"last-name" "birth-date" "gender-and-lastname"})

(s/def ::sort-fn-name #(= "sort" %))
(s/def ::sort-fn #(get sort-fn-names %))
(s/def ::input-csv #(.canRead (io/as-file %)))
(s/def ::output-csv #(.canWrite (io/as-file %)))

(s/def ::sort (s/cat :sort-fn-name ::sort-fn-name
                     :sort-fn ::sort-fn
                     :input-csv ::input-csv
                     :output-csv ::output-csv))

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

(defmethod user-friendly-msg [:cli ::cli-fns]
  [ctx spec x m]
  (let [{val :val} (get-problem-map-leaf spec x)]
    (named-format "%msg~s {%val~s} in position 0 isn't in the list of accepted functions: %cli-fns~s" {:msg cli-fn-msg
                                                                                                       :val (first val)
                                                                                                       :cli-fns (str/join "," cli-fn-names)})))


(defmethod user-friendly-msg [:cli ::sort-fn]
  [ctx spec x m]
  (named-format "%msg~s {%val~s} in position {%pos~s} isn't in the list of accepted functions: %sort-fns~s"
                (merge (get-problem-map-leaf spec x) {:msg sort-csv-msg :sort-fns (str/join "," sort-fn-names)} m)))

(defmethod user-friendly-msg [:cli ::input-csv]
  [ctx spec x m]
  (named-format "%msg~s {%val~s} in position {%pos~s} can't be read!"
                (merge (get-problem-map-leaf spec x) {:msg input-csv-msg} m)))

(defmethod user-friendly-msg [:cli ::output-csv]
  [ctx spec x m]
  (named-format "%msg~s {%val~s} in position {%pos~s} can't be written to!"
                (merge (get-problem-map-leaf spec x) {:msg output-csv-msg} m)))


(defmethod user-friendly-msg [:cli nil]
  [ctx spec x m]
  "success")


(defmethod user-friendly-msg [:cli ::cli-input]
  [ctx spec x m]
  (let [{reason :reason path :path val :val leaf-spec :spec pos :pos} (get-problem-map-leaf spec x)]
    (def foo pos)
    (if reason
      (named-format "%arg~s failed because %reason~s" {:arg (name (last path)) :reason reason}) 
      (user-friendly-msg ctx leaf-spec val {:pos pos}))))



(def cli-options [["-h" "--help"]])

(defn exit [status msg]
  (println msg)
  (System/exit status))


(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))



(defn validate-args
  [args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options)                           {:action :exit :exit-message (usage summary) :status-code :fine}
      errors                                    {:action :exit :exit-message (error-msg errors) :status-code :errors}
      (not (s/valid? ::cli-input args))         {:action :exit :exit-message (error-msg [(user-friendly-msg :cli ::cli-input arguments {})]) :status-code :errors}
      :process                                  {:action (first arguments) :arguments arguments :options options})))


(defn -main
  [& args]
  (let [{:keys [action options exit-message ok?]} (validate-args args)
        status-fine 0
        status-error 1]
    (case action
      "exit" (exit (if ok? status-fine status-error) exit-message) 
      "sort" "TODO SORT")))





(-main "sort" "last-name" "README.md" "README.md")

