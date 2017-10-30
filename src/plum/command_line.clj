(ns plum.command-line
  (:require [clojure.java.io :as io]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [phrase.alpha :as phrase]))



(s/def ::year
  pos-int?)

(defphraser pos-int?
  {:via [::year]}
  [_ _]
  "year whwat")

(s/def ::drinks
  pos-int?)

(defphraser pos-int?
  {:via [::drinks]}
  [_ _]
  "year XXXX")

(def foo (s/cat :year ::year :drinks ::drinks))

(s/def ::foo foo)




(s/exercise ::foo)


(defphraser foo)

(defphraser pos-int?
  {:via [::year]}
  [_ _]
  "The year has to be a positive integer.")



(phrase-first {} ::year "1942")
;;=> "The year has to be a positive integer."


(def input-csv-msg "The csv location")
(def sort-csv-msg "The sort function to apply to the input csv")
(def output-csv-msg "The intended csv location")

(def usage
  (->> ["Plum helps you sort csv's of people records!"
        ""
        "Usage: plum input-csv sort output-csv"
        (str "input-csv : " input-csv-msg)
        (str "sort      : " sort-csv-msg)
        (str "output-csv: " output-csv-msg)
        ""]
       (str/join \newline)))

(defn file-exists?
  [f]
  (.exists (io/as-file f)))

(s/def ::input-csv file-exists?)

(phrase/defphraser file-exists?
  {:via [::input-csv]}
  [_ {:keys [val]}]
  (format "%s %s doesn't exist." input-csv-msg val))


(def sort-functions #{:last-name :birth-date :gender-and-lastname})

(def valid-sort-function-options
  (map name sort-functions))

(defn valid-plum-sort-function?
  [s]
  (valid-sort-function-options s))

(s/def ::plum-sort valid-plum-sort-function?)

(phrase/defphraser valid-plum-sort-function?
  [{:via [::plum-sort]}]
  (format))


(phrase-first {} ::input-csv "README.mx")

(s/def ::args (s/cat :input-csv ::input-csv-arg))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))



(def validations
  {:input-csv {:fn #(.exists (io/as-file %)) :error-fn #(str (:value %) "in pos" (:pos %) "is not a valid input csv location.")}
   :sort {:fn #(valid-sort-function-options %) :error-fn #(str (:value %) "in pos" (:pos %) "is not in the valid sort functions: " valid-sort-function-options)}
   :output-csv {:fn #(or (not (str/includes? % "/")) (.canWrite (io/file %))) :error-fn #(str (:value %) "in pos" (:pos %) "is not a valid output csv location.")}})


(defn validate-arg
  [{:keys [id value pos] :as arg
    (let [validation (id validations)]
      (if ((:fn validation) value)
        {:arg arg :valid? true :error-msg ""}
        {:arg arg :valid? false :error-msg ((:error-fn validation) arg)}))}])


(defn positional->map
  [[input-csv sort output-csv]]
  [{:id :input-csv  :value input-csv  :pos 0}
   {:id :sort       :value sort       :pos 1}
   {:id :output-csv :value output-csv :pos 2}])


(defn -main [& args]
  (let [{:keys [action options exit-message ok?]} (process-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message))))
      ;; TODO sort csv and output it)

(defn exit [status msg]
  (println msg)
  (System/exit status))


(defn error-msg
  [args])
  

(defn parse-args
  [args]
  (let [args (map validate-arg (positional->map args))
        invalid-args (filter :error args)
        valid-args (remove :error args)]
    (if invalid-args
      (println (error-msg invalid-args)))))
      ;TODO sort args

  
