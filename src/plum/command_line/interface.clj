(ns plum.command-line.interface
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]]
            [com.gfredericks.like-format-but-with-named-args :refer [named-format]]
            [plum.specs.command-line :as spec-cli]
            [plum.command-line.tools :as tools]
            [plum.user-friendly :as user-friendly]
            [plum.sort.functions :as sort-fns]
            [plum.command-line.msgs :as msgs]))



(defn usage
  [options-summery]
  (->> ["Sorts csvs"
        "Usage: [options] sort sort-fn input-csv output-csv "
        (str "arg        | pos | name ")
        (str "sort       |  0  | " msgs/cli-fn)
        (str "sort-fn    |  1  | " msgs/sort-csv " examples: " (str/join "," sort-fns/names))
        (str "input-csv  |  2  | " msgs/input-csv)
        (str "output-csv |  3  | " msgs/output-csv)
        ""
        "Options"
        options-summery
        ""]
       (str/join \newline)))


(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(defn args->action
  [args]
  (let [{:keys [options arguments errors summary] :as input} (parse-opts args [["-h" "--help"]])]
    (cond
      (:help options)                           {:action :exit :exit-message (usage summary) :status :fine}
      errors                                    {:action :exit :exit-message (error-msg errors) :status :errors}
      (spec-cli/invalid-args? args)             {:action :exit :exit-message (error-msg (user-friendly/msg :cli ::cli-input arguments {})) :status :errors}
      :process                                  {:action (keyword (first arguments)) :arguments arguments :options options})))

(defmulti process-args (fn [{:keys [action]}] action))

(defmethod process-args :exit
  [{:keys [status exit-message]}]
  (println exit-message)
  (System/exit (status {:fine 0 :errors -1})))


;;TODO implement sort function
(defmethod process-args :sort
  [{:keys [arguments] :as m}]
  m)
