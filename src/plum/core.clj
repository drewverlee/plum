(ns plum.core
  (:gen-class)
  (:require [plum.command-line :as cli]))

(defn -main
  [& args]
  (->> args
       cli/args->action
       cli/take-action))
