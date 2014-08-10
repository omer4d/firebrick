(ns firebrick.core)

(load "core_entity")

(defn make-game []
  {:next-id 0
   :ent-map {}
   :spawn-queue []})

(defn spawn [{queue :spawn-queue :as game} entities]
  (assoc game :spawn-queue
    (if (sequential? entities)
      (into queue entities)
      (conj queue entities))))

(defn consume-spawn-queue [{:keys [next-id ent-map spawn-queue] :as game}]
  (assoc game
    :ent-map (into ent-map (zipmap (iterate inc next-id) spawn-queue))
    :spawn-queue []
    :next-id (+ next-id (count spawn-queue))))

(defn remove-dead-ents [ent-map]
  (into {} (filter #((complement entity-dead?) (val %)) ent-map)))

(defn run-logic [ent-map dt]
  (into {} (for [[k v] ent-map] [k (run-entity-logic v dt)])))

(defn game-step [game dt]
  (let [game1 (consume-spawn-queue game)]
    (assoc game1 :ent-map
      (-> (:ent-map game1)
          (remove-dead-ents)
          (run-logic dt)))))
