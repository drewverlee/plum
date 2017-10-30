(ns plum.core
  (:gen-class)
  (:require [plum.command-line :as cl]))

(defn -main
  [& args]
  (println
   (cl/parse-args args)))
