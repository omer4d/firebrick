(ns firebrick.core
  (:import firebrick.core.Grid))

(require '[clojure.core.reducers :as r])

(load "core_entity")
(load "core_behaviors")
(load "core_geom2d")

(defn make-game []
  {:next-id 0
   :ent-map {}
   :spawn-queue []})

;(into [] (persistent! (r/map inc (transient [1 2 3 4]))))

(defn spawn [{queue :spawn-queue :as game} entities]
  (assoc game :spawn-queue
    (if (sequential? entities)
      (into queue entities)
      (conj queue entities))))

(defn set-entity-id [ent id]
  (assoc-in ent [:CoreComp :id] id))

(defn consume-spawn-queue [{:keys [next-id ent-map spawn-queue] :as game}]
  (let [ids (iterate inc next-id)]
    (assoc game
      :ent-map (into ent-map (zipmap ids (map set-entity-id spawn-queue ids)))
      :spawn-queue []
      :next-id (+ next-id (count spawn-queue)))))

(defn remove-dead-ents [ent-map]
  (into {} (r/filter #((complement entity-dead?) (second %)) ent-map)))

(defn run-logic [ent-map dt]
  (r/map #(identity [(first %) (run-entity-logic (second %) dt)]) ent-map))

(defn make-grid [cols rows] (Grid. cols rows))

(def get-grid (memoize make-grid))

(defn grid-at [^Grid grid ^long i ^long j]
  (.at grid i j))

(defn grid-set [^Grid grid ^long i ^long j ent]
  (.set grid i j ent))

(defn filled-grid [ent-map]
  (let [grid (get-grid 11 80)]
    (doseq [{grid-comp :GridComp :as ent} (filter #(identity (:GridComp %)) (vals ent-map))]
      (grid-set grid (:i grid-comp) (:j grid-comp) ent))
    grid))

(defn even-di [] [-1 0 -1 1 -1 0])
(defn even-dj [] [-1 -1 0 0 1 1])
(defn odd-di [] [0 1 -1 1 0 1])
(defn odd-dj [] [-1 -1 0 0 1 1])

(defn update-ent [ent-map ent]
  (assoc! ent-map (entity-id ent) ent))

(defn get-neighbors [grid i j]
  (let [dis (if (even? (long j)) (even-di) (odd-di))
        djs (if (even? (long j)) (even-dj) (odd-dj))]
    (filter identity (map #(grid-at grid (+ i %1) (+ j %2)) dis djs))))

(defn bub-neighbors [{{i :i, j :j} :GridComp} grid]
  (get-neighbors grid i j))

(defn into! [to from]
  (reduce conj! to from))

(defn mmap [f m] (into {} (for [[k v] m] [k (f v)])))

(let [v (mmap transient {:ent1 {:a 1, :b 2, :c 3}, :ent2 {:d 4, :e 5, :f 6}})]
  (-> v :ent1 (assoc! :z 666))
  (mmap persistent! v))

  ;(conj! (:ent1 v) {:z 6})
  ;(into {} (map persistent! v)))

;(let [src (vec (range 0 20 4))]
;  (map-indexed (fn [i v] (- (nth src (inc i)) v)) src))

;(map-indexed (fn [i v] v) [1 1 1 1])


;; (defn handle-bub-collisions [ent-map]
;;   (doseq [[k1 v1 :as e1] ent-map
;;           [k2 v2 :as e2] ent-map
;;           :when (< k1 k2)]
;;     (println e1 e2)))

(defn separate-bubs [bub1 bub2]
  (let [mtv (separate-circles (-> bub1 :Position :v) 15 (-> bub2 :Position :v) 15)]
    (if (< (vdot mtv mtv) 0.01)
      ((entity-thread! bub1 (move (v* mtv -0.5)) (setvel (Vec2f. 0 0)))
       (entity-thread! bub2 (move (v* mtv 0.5)) (setvel (Vec2f. 0 0))))
      nil)))

(defn bub-collide-with [bub others]
  (doseq [other others]
    (separate-bubs bub other)))

(defn handle-grid-collisions [ent-map]
  (let [ents (mmap transient ent-map)
        grid (filled-grid ents)]
    (doseq [ent (filter #(identity (:GridComp %)) (vals ents))]
      (bub-collide-with ent (bub-neighbors ent grid)))
    (mmap persistent! ents)))

;(persistent! (into! (transient {:a 1 :b 2}) [[:c 3] [:d 4] [:e 5]]))

(defn game-step [game dt]
  (let [game1 (consume-spawn-queue game)
        live-ents (remove-dead-ents (:ent-map game1))]
    (assoc game1 :ent-map
      (handle-grid-collisions (into live-ents (-> live-ents (run-logic dt)))))))
