(ns plum.person-test
  (:require [plum.person :as person]
            [clojure.test :refer :all]
            [test-with-files.core :refer [with-files tmp-dir]]
            [clj-time.core :as t]
            [clojure.string :as str]))

(deftest ->vals-as-str
  (testing "when given a person map the returned string contains the values."
    (is (= "" (person/->vals-as-str "," {})))
    (is (= "a" (person/->vals-as-str "," {:foo "a"})))
    (is (= "a,b" (person/->vals-as-str "," {:foo "a" :bar "b"})))
    (is (= "a,07/07/1985" (person/->vals-as-str "," {:foo "a" :date-of-birth (t/date-time 1985 7 7)})), "should format date-of-birth.")))
