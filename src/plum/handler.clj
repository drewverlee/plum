(ns plum.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [plum.person :as person]
            [clojure.spec.alpha :as s]
            [spec-tools.spec :as spec]))

(s/def ::person-map (s/keys :req-un [::person/record]))
(s/def ::total spec/int?)
(s/def ::total-map (s/keys :req-un [::total]))

(s/def ::foo string?)

(def app
  (api
   {:coercion nil}
   {:swagger
    {:ui "/api-docs"
     :spec "/swagger.json"
     :data {:info {:title "Plum API"
                   :description "A restful API for working with person records"}
            :tags [{:name "person", :description "add person records or retrieve sorted collections of them"}]}}}

   (context "/person" []
            :tags ["person"]
            
            (POST "/" []
              :summary "add a person to the people list and return person back."
              :body-params [line :- spec/string?]
              :return spec/string?
              (ok {:line line})))))
  
(-> {:uri "/person"
     :request-method :post
     :body-params {:line "a,b,c,d,04/07/1800"}}
    app
    :body
    slurp
    (cheshire.core/parse-string true))





