(ns plum.command-line-test
  (:require 
            [clojure.test :refer :all]
            [test-with-files.core :refer [with-files tmp-dir]]
            [clj-time.core :as t]
            [clojure.string :as str]
            [plum.person :as person]
            [plum.command-line :as cli]))
            

(deftest args->action
  (let [input-csv "/input.csv"
        input-csv-path (str tmp-dir input-csv)
        output-csv "/output.csv"
        output-csv-path (str tmp-dir output-csv)]
    (with-files [[input-csv "a,b"]
                 [output-csv ""]]
      (testing "when missing arguments the returned map contains the exit action"
        (is (= :exit (:action (cli/args->action ["sort"]))) "because missing 3 args")
        (is (= :exit (:action (cli/args->action ["sort" "last-name" input-csv-path]))) "because missing 1 arg"))
      (testing "when given bad arguments the returned map contains the exit action"
        (is (= :exit (:action (cli/args->action ["bad-arg"]))) "because bad-arg isn't in list")
        (is (= :exit (:action (cli/args->action ["sort" "last-name" input-csv-path "file-that-doesnt-exist"]))) "because that file doesnt exist"))
      (testing "when given the help option we exit"
        (is (= :exit (:action (cli/args->action ["--help"])))))
      (testing "When given good arguments the returned map doesn't contain the exit action"
        (is (= :sort (:action (cli/args->action ["sort" "last-name" input-csv-path output-csv-path "--separator" ","]))))))))

(deftest take-action
  (let [input-csv "/input.csv"
        input-csv-path (str tmp-dir input-csv)
        output-csv "/output.csv"
        output-csv-path (str tmp-dir output-csv)

        expected-output-csv "/expected-output.csv"
        expected-output-csv-path (str tmp-dir expected-output-csv)
        separator \,
        base-person {:first-name "alan" :gender "male" :favorite-color "blue" :date-of-birth (t/date-time 1984 4 4)}
        xaviar (person/->vals-as-str separator (merge {:last-name "xaviar"} base-person))
        smith (person/->vals-as-str separator (merge {:last-name "smith"} base-person))
        build-file (fn [& strings] (str (str/join \newline strings) \newline))]
    (with-files [[input-csv (build-file smith xaviar)]
                 [output-csv (build-file "")]
                 ;; we include headers in the output
                 [expected-output-csv (build-file "last-name,first-name,gender,favorite-color,date-of-birth" xaviar smith)]]

      (testing "when given a sort action on a csv it outputs to the correctly sorted file"

        ;;Take side effect of creating file!
        (cli/take-action {:action :sort :arguments ["sort" "last-name" input-csv-path output-csv-path] :options {:separator separator}})
        ;;Test that the files are properly sorted
        (is (= (slurp expected-output-csv-path)
               (slurp output-csv-path))
            "should be sorted by last name descending")))))
