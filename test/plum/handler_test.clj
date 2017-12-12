(ns plum.handler-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as cheshire]
            [ring.mock.request :as mock]
            [plum.handler :as handler]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(defn create-body [body]
  (cheshire/generate-string body))
  

;; see docs on testing compojure-api endpoints: https://github.com/metosin/compojure-api/wiki/Testing-api-endpoints
(deftest api
  (testing "POST /persons :with person string returns a person as json "
    (let [response (handler/app (-> (mock/request :post "/persons/") 
                                    (mock/content-type "application/json")
                                    (mock/body (create-body {:person "john,smith,male,blue,04/07/1950"})))) 
          body (parse-body (:body response))]
      (is (= "smith" (get-in {:person {:last-name "smith"}} [:person :last-name]))))))




