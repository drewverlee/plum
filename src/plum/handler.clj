(ns plum.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [clojure.spec.alpha :as s]
            [compojure.api.coercion.spec :as cs]
            [spec-tools.spec :as spec]
            [clojure.string :as str]
            [clj-time.coerce :as c]
            [expound.alpha :as expound]
            [plum.person.core :as p-core]
            [plum.db :as db]
            [plum.person.model :as p-model]
            [plum.person.request :as p-request]
            [plum.person.response :as p-response]))

(def app
  (api
   (context "/person" []
            :tags ["person"]
            :coercion :spec
            (POST "/" []
              :summary "add a person to the people list and return person back."
              :body-params [person-line :- ::p-request/person]
              :return ::p-response/person
              (ok (->> person-line
                       p-request/->model
                       db/add!
                       p-model/->response))))))

;;Example of how to inspect a route
;; (-> {:uri "/person"
;;      :request-method :post
;;      :body-params {:person-line "a,b,c,d,04/07/1800"}}
;;     app
;;     :body
;;     slurp
;;     (cheshire.core/parse-string true))

;;=> {:last-name "a", :first-name "b", :gender "c", :favorite-color "d", :date-of-birth "04/07/1800"}













