(ns firebrick.core)

(defn strs->sym [& elems] (symbol (apply str elems)))

(defmacro defcomp2 [comp-name fields]
  `(defn ~(strs->sym comp-name "*") [~@fields]
     ~(assoc
        (zipmap (map keyword fields) fields)
        :comp-type
        (keyword comp-name))))

(defmacro defcomp [comp-name fields]
  `(do
     (defrecord ~comp-name [~@fields])
     (defn ~(strs->sym comp-name "*") [~@fields]
       (with-meta (~(strs->sym comp-name ".") ~@fields) ~{:comp-type (keyword comp-name)}))))

(defn get-comp-type [com]
  (-> com meta :comp-type))
