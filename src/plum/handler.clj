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
            [plum.person.sort-fns :as sort-fns]
            [plum.person.model :as p-model]
            [plum.person.request :as p-request]
            [plum.person.response :as p-response]))

(def app
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "Plum"
                   :description "Operations over people records"}
            :tags [{:name "records", :description "post records of people and get sorted collections of them."}]}}}
   (context "/records" []
            :tags ["records"]
            :coercion :spec
            (POST "/" []
              :summary "Add a person record and echos record back"
              :body-params [person-line :- ::p-request/person] 
              :return ::p-response/person
              (ok (->> person-line
                       p-request/->model
                       db/add!
                       p-model/->response)))
            (GET "/:sort-fn" []
              :summary "returns records sorted by client selected function."
              :path-params [sort-fn :- ::sort-fns/fns]
              :return ::p-response/people
              (ok
               (let [sort-them (sort-fns/get-fn "last-name")
                     people @db/db
                     to-response (fn [coll] (map p-model/->response coll))]
                 (->> people
                      sort-them
                      to-response)))))))

;;Example of how to inspect a route
;; (-> {:uri "/records"
;;      :request-method :post
;;      :body-params {:person-line "a,b,c,d,04/07/1800"}}
;;     app
;;     :body
;;     slurp
;;     (cheshire.core/parse-string true))

;; returns a person record
;;=> {:last-name "a", :first-name "b", :gender "c", :favorite-color "d", :date-of-birth "04/07/1800"}

;;Example of how to get
;; (-> {:uri "/records/date-of-birth"
;;      :request-method :get}
;;     app
;;     :body
;;     slurp
;;     (cheshire.core/parse-string true))

;; returns sorted people
;; => ({:last-name "a", :first-name "b", :gender "c", :favorite-color "d", :date-of-birth "04/07/1800"}
;;     {:last-name "a", :first-name "b", :gender "c", :favorite-color "d", :date-of-birth "04/07/1800"}))

