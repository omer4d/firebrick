(ns firebrick.core)

(defcomp CoreComp [logic ^Boolean death-flag id])

(defn entity-assoc! [ent comps]
  (if (sequential? comps)
    (apply assoc! ent (interleave (map get-comp-type comps) comps))
    (assoc! ent (get-comp-type comps) comps)))

(defn entity-apply! [ent f & args]
  (entity-assoc! ent (apply f ent args)))

(defn make-entity-applier [form]
  (if (sequential? form)
    (cons 'entity-apply! form)
    (list 'entity-apply! form)))

(defmacro entity-thread! [ent & forms]
  `(-> ~ent ~@(map make-entity-applier forms)))

(defmacro entity-thread [ent & forms]
  `(persistent! (-> (transient ~ent) ~@(map make-entity-applier forms))))

(defn make-entity [comps logic]
  (assoc
    (zipmap (map get-comp-type comps) comps)
    :CoreComp (CoreComp* logic false -1)))

(defn run-entity-logic [ent dt]
  ((:logic (:CoreComp ent)) ent dt))

(defn add-comp [ent comps]
  (if (sequential? comps)
    (assoc ent (interleave (map get-comp-type comps) comps))
    (assoc ent (get-comp-type comps) comps)))

(defn entity-dead? [ent]
  (-> ent :CoreComp :death-flag))

(defn entity-id [ent]
  (-> ent :CoreComp :id))
