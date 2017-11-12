(ns plum.user-friendly
  (:require [plum.specs.tools :as spec-tools]
            [plum.command-line.msgs :as msgs]))

(defmulti msg (fn [ctx spec x m] [ctx spec]))

(defmethod msg [:cli ::cli-fns]
  [ctx spec x m]
  (let [{val :val} (spec-tools/get-problem-map-leaf spec x)]
    (named-format "%msg~s {%val~s} in position 0 isn't in the list of accepted functions: %cli-fns~s" {:msg msgs/cli-fn
                                                                                                       :val (first val)
                                                                                                       :cli-fns (str/join "," cli-fn-names)})))


(defmethod msg [:cli ::sort-fn]
  [ctx spec x m]
  (named-format "%msg~s {%val~s} in position {%pos~s} isn't in the list of accepted functions: %sort-fns~s"
                (merge (spec-tools/get-problem-map-leaf spec x) {:msg msgs/sort-csv :sort-fns (str/join "," sort-fn-names)} m)))

(defmethod msg [:cli ::input-csv]
  [ctx spec x m]
  (named-format "%msg~s {%val~s} in position {%pos~s} can't be read!"
                (merge (spec-tools/get-problem-map-leaf spec x) {:msg msgs/input-csv} m)))

(defmethod msg [:cli ::output-csv]
  [ctx spec x m]
  (named-format "%msg~s {%val~s} in position {%pos~s} can't be written to!"
                (merge (spec-tools/get-problem-map-leaf spec x) {:msg msgs/output-csv} m)))

(defmethod msg [:cli ::cli-input]
  [ctx spec x m]
  (let [{reason :reason path :path val :val leaf-spec :spec pos :pos} (spec-tools/get-problem-map-leaf spec x)]
    (if reason
      (named-format "%arg~s failed because %reason~s" {:arg (name (last path)) :reason reason})
      (msg ctx leaf-spec val {:pos pos}))))
