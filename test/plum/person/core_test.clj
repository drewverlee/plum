(ns plum.person.core-test
  (:require [plum.person.core :as c]
            [clojure.test :refer :all]))

(deftest get-separator
  (testing "Given a string one of the Separators (see accepted list) get returns a separator character"
    (is (= \| (c/get-separator "hello|you")))
    (is (= \space (c/get-separator "hello you|there")) "picks the first separator")))
