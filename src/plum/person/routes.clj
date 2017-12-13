(ns plum.person.routes
  "Contains api routes for dealing with persons."
  (:require [ring.util.http-response :refer [ok]]
            [com.gfredericks.like-format-but-with-named-args :refer [named-format]]
            [compojure.api.sweet :refer [context POST GET]]
            [plum.person.model :as model]
            [plum.person.core :as c]
            [plum.person.request :as request]
            [plum.person.response :as response]
            [plum.person.sort-fns :as sort-fns]
            [clojure.string :as str]))

(def routes
  (context "/persons" []
          :tags ["persons"]
          :coercion :spec
          (POST "/" []
                :summary (named-format "Takes person attributes joined by a separator as a string and returns it back to the client as json.
                                        separators include: %separator~s."
                                       {:separator c/sep})
                :body-params [person :- ::request/person]
                :return ::response/person
                (ok (->> person
                         request/->model
                         (swap! model/people conj)
                         last
                         model/->response)))

          (GET "/:sort-fn" []
                :summary (named-format "Returns sorted people as json.
                                        Sort functions include: %sort-fns~s "
                                       {:sort-fns (str/join "," sort-fns/names)})
                :path-params [sort-fn :- ::sort-fns/names]
                :return ::response/people
                (ok (->> @model/people
                         (sort-fns/sort-with sort-fn)
                         (map model/->response))))))
