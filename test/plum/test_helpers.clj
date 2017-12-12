(ns plum.test-helpers
  (:require [cheshire.core :as cheshire]))


(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(defn create-body [body]
  (cheshire/generate-string body))

;; we want a fresh state each test
(defn with-resource [resource f]
  (reset! resource [])
  (f resource)
  (reset! resource []))

