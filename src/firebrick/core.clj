(ns firebrick.core
  (:require [quil.core :as q])
  (:import firebrick.core.Vec2f))

(use 'clojure.walk)

(load "core_comp")
(load "core_entity")
(load "core_util")
(load "core_vec")
(load "core_vec2")

(defcomp Position [x y])

(defcomp Physics [m fx fy vx vy])

(defcomp BallData [radius color])

(defn TestComp* [x y]
  (with-meta (Vec2f. x y) {:comp-type :TestComp}))

(meta (TestComp* 1 2))

(defn phys-step [{pos :Position, phys :Physics} dt]
  (let [{:keys [m fx fy vx vy]} phys
        {:keys [x y]} pos
        invm (/ m)
        vx1 (+ vx (* fx invm dt))
        vy1 (+ vy (* fy invm dt))
        x1 (+ x (* vx1 dt))
        y1 (+ y (* vy1 dt))]
    [(assoc pos :x x1, :y y1)
     (assoc phys :fx 0, :fy 0, :vx vx1, :vy vy1)]))

(defn accel [{phys :Physics} ax ay]
  (let [{:keys [fx fy m]} phys]
    (assoc phys
      :fx (+ fx (* ax m))
      :fy (+ fy (* ay m)))))

(defn bounce [{pos :Position, phys :Physics} x0 y0 x1 y1]
  (let [{:keys [vx vy]} phys
        {:keys [x y]} pos
        vx1 (* vx (if (outside-range? x x0 x1) -1 1))
        vy1 (* vy (if (outside-range? y y0 y1) -1 1))]
    [(assoc phys :vx vx1 :vy vy1)
     (assoc pos :x (clamp x x0 x1), :y (clamp y y0 y1))]))

(defn ball-logic [ball dt]
  (let [r (-> ball :BallData :radius)]
    (entity-thread ball
                   (accel 0 50)
                   (phys-step dt)
                   (bounce r r (- 1024 r) (- 768 r)))))

(macroexpand '(entity-thread ball
                   (accel 0 50)
                   (phys-step dt)
                   (bounce r r (- 1024 r) (- 768 r))))

(defn make-ball [x y vx vy rad col]
  (make-entity [(Position* x y)
                (Physics* 1 0 0 vx vy)
                (BallData* rad col)]
               ball-logic))

(make-entity [(Position* 1 2)] nil)

(make-ball 10 10 20 20 0 "green")
(defn make-entity-map [ents]
  (zipmap (range) ents))


(let [ent (make-ball 2 6 0 0 1 0)]
  (-> ent :Position :y))

;(repel {1 (make-ball 2 2 0 0 1 0)
;        2 (make-ball 0 0 0 0 2 0)})

(reduce + [1 2 3 4])


(defn simulate-balls [ent-map dt]
  (into {} (for [[k v] ent-map] [k (run-entity-logic v dt)])))

(defn generate-random-balls [n]
  (repeatedly n #(make-ball (rand-int 1024) (rand-int 768) (rand-int 100) (rand-int 100) (+ 25 (rand-int 25)) [(rand-int 255) (rand-int 255) (rand-int 255)])))

(def balls (atom (make-entity-map (generate-random-balls 5000))))

(defn setup []
  (q/frame-rate 60)
  (q/background 200)
  (q/text-font (q/create-font "DejaVu Sans" 28 true)))

;(profile-crap)

(defn draw []
  (q/background-float 200)
    (q/text (str (q/current-frame-rate))
          300   ; x
          100)

  (swap! balls simulate-balls 0.05)

  (doseq [{pos :Position ball-data :BallData} (vals @balls)]
    (apply q/fill (:color ball-data))
    (q/ellipse (:x pos) (:y pos) 1 1)))

(q/defsketch example
  :title "Oh so many grey circles"
  :setup setup
  :draw draw
  :size [1024 768])
