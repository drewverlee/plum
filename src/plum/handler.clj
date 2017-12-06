(ns plum.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [plum.domain :refer :all]
            [schema.core :as s]
            [plum.person :as person]
            [clojure.string :as str]))

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Plum"
                    :description "Plum sorts records"}
             :tags [{:name "person", :description "functions pertaining to person records.
person records contain the following attributes: " (str/join "," (map name person/attributes))}]}}}

    (context "/person" []
      :tags ["person"]

      (GET "/records" []
        :body []))))
        



(resource
 {:post
  {:parameter {query-params (s/keys :req-un person/record)}}})

(s/val)
