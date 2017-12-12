(ns plum.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [plum.test-helpers :as h]
            [plum.handler :as handler]
            [plum.person.model :as person.model]))

;; see docs on testing compojure-api endpoints: https://github.com/metosin/compojure-api/wiki/Testing-api-endpoints

(deftest api
  (testing "POST /persons :with person string returns a person as json."
    ;; we pass in the people atom as a fresh resource each time a test is run
    (h/with-resource person.model/people
      (fn [resource]
        [(let [response (handler/app (-> (mock/request :post "/persons/")
                                        (mock/content-type "application/json")
                                        (mock/body (h/create-body {:person "john,smith,male,blue,04/07/1950"}))))
               body (h/parse-body (:body response))]
           (is (= "smith" (get-in {:person {:last-name "smith"}} [:person :last-name])))
           (is (= 1 (count @resource))))]))))
