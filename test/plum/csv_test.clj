(ns plum.csv-test
  (:require [plum.csv :as csv]
            [clojure.test :refer :all]
            [test-with-files.core :refer [with-files tmp-dir]]
            [plum.person :as person]
            [clj-time.core :as t]))


(deftest ->person
  (testing "when given a csv the return value is a vector of hash maps of people"

    (let [input-csv "/input.csv"
          input-csv-path (str tmp-dir input-csv)]
      (with-files [[input-csv ""]]
        (is (= [] (csv/->person \, input-csv-path))
            "should be empty.")))

    (let [input-csv "/input.csv"
          input-csv-path (str tmp-dir input-csv)]
      (with-files [[input-csv "ln,fn,g,fc,4/2/1994\nln,fn,g,fc,4/2/1994"]]
        (is (= [{:last-name "ln" :first-name "fn" :gender "g" :favorite-color "fc" :date-of-birth (t/date-time 1994 4 2)}
                {:last-name "ln" :first-name "fn" :gender "g" :favorite-color "fc" :date-of-birth (t/date-time 1994 4 2)}]
               (csv/->person \, input-csv-path))
            "should contain people records")))))
