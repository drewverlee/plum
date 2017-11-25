(ns plum.command-line-test
  (:require [plum.command-line :as cli]
            [clojure.test :refer :all]
            [test-with-files.core :refer [with-files tmp-dir]]
            [clojure.java.io :as io]))

(deftest args->action
  (let [input-csv "/input.csv"
        input-csv-path (str tmp-dir input-csv)
        output-csv "/output.csv"
        output-csv-path (str tmp-dir output-csv)]
    (with-files [[input-csv "a,b"]
                 [output-csv ""]]
      (testing "when missing arguments the returned map contains the exit action"
        (is (= :exit (:action (cli/args->action ["sort"]))))
        (is (= :exit (:action (cli/args->action ["sort" "last-name" input-csv-path])))))
      (testing "when given bad arguments the returned map contains the exit action"
        (is (= :exit (:action (cli/args->action ["bad-arg"]))))
        (is (= :exit (:action (cli/args->action ["sort" "last-name" input-csv-path "file-that-doesnt-exist"])))))
      (testing "when given the help option we exit"
        (is (= :exit (:action (cli/args->action ["--help"])))))
      (testing "When given good arguments the returned map doesn't contain the exit action"
        (is (= :sort (:action (cli/args->action ["sort" "last-name" input-csv-path output-csv-path]))))))))

