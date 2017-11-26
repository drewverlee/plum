(ns plum.command-line-test
  (:require [plum.command-line :as cli]
            [clojure.test :refer :all]
            [test-with-files.core :refer [with-files tmp-dir]]
            [plum.person :as person]
            [clj-time.core :as t]
            [clojure.string :as str]
            [plum.sort-fns :as sort-fns]))

(deftest args->action
  (let [input-csv "/input.csv"
        input-csv-path (str tmp-dir input-csv)
        output-csv "/output.csv"
        output-csv-path (str tmp-dir output-csv)]
    (with-files [[input-csv "a,b"]
                 [output-csv ""]]
      (testing "when missing arguments the returned map contains the exit action"
        (is (= :exit (:action (cli/args->action ["sort"]))) "missing 3 args")
        (is (= :exit (:action (cli/args->action ["sort" "last-name" input-csv-path]))) "missing 1 arg"))
      (testing "when given bad arguments the returned map contains the exit action"
        (is (= :exit (:action (cli/args->action ["bad-arg"]))) "bad-arg isn't in list")
        (is (= :exit (:action (cli/args->action ["sort" "last-name" input-csv-path "file-that-doesnt-exist"]))) "that file doesnt exist"))
      (testing "when given the help option we exit"
        (is (= :exit (:action (cli/args->action ["--help"])))))
      (testing "When given good arguments the returned map doesn't contain the exit action"
        (is (= :sort (:action (cli/args->action ["sort" "last-name" input-csv-path output-csv-path]))))))))

(deftest take-action
  (let [input-csv "/input.csv"
        input-csv-path (str tmp-dir input-csv)
        output-csv "/output.csv"
        output-csv-path (str tmp-dir output-csv)

        expected-output-csv "/expected-output.csv"
        expected-output-csv-path (str tmp-dir expected-output-csv)
        delimiter \,
        base-person {:first-name "alan" :gender "male" :favorite-color "blue" :date-of-birth (t/date-time 1984 4 4)}
        xaviar (person/->vals-as-str delimiter (merge {:last-name "xaviar"} base-person))
        smith (person/->vals-as-str delimiter (merge {:last-name "smith"} base-person))
        build-file (fn [& strings] (str (str/join \newline strings) \newline))]
    (with-files [[input-csv (build-file smith xaviar)]
                 [output-csv (build-file "")]
                 ;; we include headers in the output
                 [expected-output-csv (build-file "last-name,first-name,gender,favorite-color,date-of-birth" xaviar smith)]]

      (testing "when given a sort action on a csv it outputs to the correctly sorted file"

        ;;Take side effect of creating file!
        (cli/take-action {:action :sort :arguments ["sort" "last-name" input-csv-path output-csv-path]})
        ;;Test that the files are properly sorted
        (is (= (slurp expected-output-csv-path)
               (slurp output-csv-path))
            "should be sorted by last name descending")))))
