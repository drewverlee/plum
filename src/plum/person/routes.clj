(ns plum.person.routes
  (:require [ring.util.http-response :refer [ok]]
            [compojure.api.sweet :refer [context POST GET]]
            [plum.person.model :as model]
            [plum.person.request :as request]
            [plum.person.response :as response]
            [plum.person.sort-fns :as sort-fns]))

(def routes
  (context "/persons" []
          :tags ["persons"]
          :coercion :spec
          (POST "/" []
                :summary "Adds a person and returns it back."
                :body-params [person :- ::request/person]
                :return ::response/person
                (ok (->> person
                         request/->model
                         (model/add-and-echo! model/people)
                         model/->response)))

          (GET "/:sort-fn" []
                :summary "Returns sorted people."
                :path-params [sort-fn :- ::sort-fns/names]
                :return ::response/people
                (ok (->> @model/people
                         (sort-fns/sort-with sort-fn)
                         (map model/->response))))))
