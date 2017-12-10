(ns plum.foo)

(require '[clojure.spec.alpha :as s])
(require '[spec-tools.spec :as spec])
(require '[compojure.api.sweet :refer :all])
(require '[ring.util.http-response :refer :all])



(s/def ::a spec/int?)
(s/def ::b spec/int?)
(s/def ::c spec/int?)
(s/def ::d spec/int?)
(s/def ::total spec/int?)
(s/def ::total-body (s/keys ::req-un [::total]))

(s/def ::x spec/int?)
(s/def ::y spec/int?)

(def app
  (api
    {:coercion :spec}

    (context "/math/:a" []
      :path-params [a :- ::a]

      (POST "/plus" []
        :query-params [b :- ::b, {c :- ::c 0}]
        :body [numbers (s/keys :req-un [::d])]
        :return (s/keys :req-un [::total])
        (ok {:total (+ a b c (:d numbers))})))

    (context "/data-math" []
      (resource
        ;; to make coercion explicit
        {:coercion :spec
         :get {:parameters {:query-params (s/keys :req-un [::x ::y])}
               :responses {200 {:schema ::total-body}}
               :handler (fn [{{:keys [x y]} :query-params}]
                          (ok {:total (+ x y)}))}}))))

(-> {:request-method :get
     :uri "/data-math"
     :query-params {:x "1", :y "2"}}
    app
    :body
    slurp
    (cheshire.core/parse-string true))

; {:total 3}
