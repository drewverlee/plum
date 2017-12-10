(ns plum.db)

;; stateful operations
(def db (atom []))

(defn add! [person]
  (swap! db (fn [db] (conj db person)))
  person)
