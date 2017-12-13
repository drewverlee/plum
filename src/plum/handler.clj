(ns plum.handler
  (:require [compojure.api.sweet :refer :all]
            [com.gfredericks.like-format-but-with-named-args :refer [named-format]]
            [ring.util.http-response :refer :all]
            [plum.person.routes :as person]
            [plum.person.model :as model]))

(def app
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "Plum"
                   :description "Operations over person records."}
            :tags [{:name "persons", :description (named-format "Add a person record OR get sorted collections of them.
                                                                 Person records have the following attributes: %attributes~s"
                                                                 {:attributes model/attr})}]}}}
   person/routes))

;; EXAMPLES

;;Example of how POST a person
;; (-> {:uri "/persons"
;;      :request-method :post
;;      :body-params {:person "smith,john,male,blue,04/07/1800"}}
;;     app
;;     :body
;;     slurp
;;     (cheshire.core/parse-string true))

;; returns a person
;;=> {:last-name "smith", :first-name "john", :gender "male", :favorite-color "blue", :date-of-birth "04/07/1800"}

;;Example of how to GET a sorted collection of people
;; (-> {:uri "/persons/date-of-birth"
;;      :request-method :get}
;;     app
;;     :body
;;     slurp
;;     (cheshire.core/parse-string true))

;; returns sorted people
;; => ({:last-name "smith", :first-name "john", :gender "male", :favorite-color "blue", :date-of-birth "04/07/1800"}
;;     {:last-name "smith", :first-name "john", :gender "male", :favorite-color "blue", :date-of-birth "04/07/1800"})
