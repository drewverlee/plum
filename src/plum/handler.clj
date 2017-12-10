(ns plum.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [plum.person :as person]
            [com.gfredericks.like-format-but-with-named-args :as n]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]))



;; (def app
;;   (api
;;     (context "/person" []
;;       :description (n/named-format "person records contain the following attributes: %attr~s" {:attr person/attr})
;;         (resource
;;           {:coercsion :spec
;;            :post {:summary "post a single person row."
;;                   :parameters {:body-params {:person ::person/row}}
;;                   :responses {200 {:schema {:person ::person/record}}}
;;                   :handler (fn [{{:keys [person]} :query-params}]
;;                              (ok {:person (update-in person [:date-of-birth] person/date-time->date-of-birth)}))}}))))


(s/def ::foo string?)

(def app
  (api
   (context "/api" []
     (POST "/person" []
       :body [person ::foo]))))

(-> {:uri "/api/person"
     :request-method :post
     :body-params "a,b,c,d,04/08/1840"}
    app
    :body
    slurp
    (cheshire.core/parse-string true))
