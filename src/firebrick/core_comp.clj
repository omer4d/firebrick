(ns firebrick.core)

(defn make-sym [& elems] (symbol (apply str elems)))

(defmacro defcomp [comp-name fields]
  `(defn ~(make-sym comp-name "*") [~@fields]
     ~(assoc
        (zipmap (map keyword fields) fields)
        :comp-type
        (keyword comp-name))))
