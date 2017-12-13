(ns plum.handler-test
  "API server entrypoint"
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [plum.test-helpers :as h]
            [plum.handler :as handler]
            [plum.person.model :as person.model]
            [clojure.spec.alpha :as s]
            [plum.person.routes :as person]
            [plum.person.request :as request]
            [plum.person.sort-fns :as sort-fns]))

;; see docs on testing compojure-api endpoints: https://github.com/metosin/compojure-api/wiki/Testing-api-endpoints

(deftest api

  (testing "POST /persons | with person string returns a person as json." ;; we pass in the people atom as a fresh resource each time a test is run.
    (h/with-resource person.model/people
      (fn [resource]
        [(let [response (handler/app (-> (mock/request :post "/persons/")
                                         (mock/content-type "application/json")
                                         (mock/body (h/create-body {:person "john,smith,male,blue,04/07/1950"}))))
               body (h/parse-body (:body response))]
           (is (= "smith" (get-in {:person {:last-name "smith"}} [:person :last-name])) "Person should be returned to client.")
           (is (= 1 (count @resource)) "Person should be stored."))])))

  (testing "GET /persons/:sort-fn | with a sort function returns sorted people records as json."
      (h/with-resource person.model/people
        (fn [resource]
          [(let [people   (swap! resource concat (h/get-examples ::person.model/people 1))
                 response (handler/app (-> (mock/request :get "/persons/last-name")
                                           (mock/content-type "application/json")))
                 body      (h/parse-body (:body response))]
             (is (= people @resource) "People Atom contains only the people inserted")
             (is (= (map (comp set keys) body)  (map (comp set keys) @resource)) "The response (people) should have the same properties as the stored state.")
             (is (= body (sort-by :last-name sort-fns/descending body)) "The response (people) is sorted by last name"))]))))
