(ns firebrick.core)

(defn make-entity [comps logic]
  (assoc
    (zipmap (map :comp-type comps) comps)
    :logic logic))

(defn entity-assoc [ent comps]
  (if (sequential? comps)
    (apply assoc ent (interleave (map :comp-type comps) comps))
    (assoc ent (:comp-type comps) comps)))

(defn entity-apply [ent f & args]
  (entity-assoc ent (apply f ent args)))

(defn make-entity-applier [form]
  (if (sequential? form)
    (cons 'entity-apply form)
    (list 'entity-apply form)))

(defmacro entity-thread [ent & forms]
  `(-> ~ent ~@(map make-entity-applier forms)))
