(ns firebrick.core
  (:require [quil.core :as q])
  (:import firebrick.core.Vec2f))

(use 'clojure.walk)

(load "core_comp")
(load "core_entity")
(load "core_util")
(load "core_vec")
(load "core_vec2")

(set! *warn-on-reflection* true)

(defcomp Position [^Vec2f v])

(defcomp Velocity [^Vec2f v])

(defcomp Physics [f m])

(defcomp BallData [radius color])

(defn TestComp* [x y]
  (with-meta (Vec2f. x y) {:comp-type :TestComp}))

(meta (TestComp* 1 2))

(defn phys-step [{pos :Position, vel :Velocity, phys :Physics} dt]
  (let [{:keys [m f]} phys
        invm (/ m)
        vel1 (v+ (:v vel) (v* f invm dt))
        pos1 (v+ (:v pos) (v* vel1 dt))]
    [(assoc pos :v pos1)
     (assoc phys :f (Vec2f. 0 0))
     (assoc vel :v vel1)]))

(defn accel [{phys :Physics} a]
  (let [{:keys [f m]} phys]
    (assoc phys :f (v+ f (v* a m)))))

(defn kaka [{pos :Position}]
  (let [^Vec2f v (:v pos)]
    (-> v .x)))

(kaka (make-entity [(Position* (Vec2f. 1 1))] nil))

(defn bounce [{^Position pos :Position, ^Velocity vel :Velocity, phys :Physics} x0 y0 x1 y1]
  (let [^Vec2f velv (:v vel)
        ^Vec2f posv (:v pos)

        vx (-> velv .x)
        vy (-> velv .y)
        x (-> posv .x)
        y (-> posv .y)
        vx1 (* vx (if (outside-range? x x0 x1) -1 1))
        vy1 (* vy (if (outside-range? y y0 y1) -1 1))]
    [(assoc vel :v (Vec2f. vx1 vy1))
     (assoc pos :v (Vec2f. (clamp x x0 x1) (clamp y y0 y1)))]))

(defn ball-logic [ball dt]
  (let [r (-> ball :BallData :radius)]
    (entity-thread ball
                   (accel (Vec2f. 0 50))
                   (phys-step dt)
                   (bounce r r (- 1024 r) (- 768 r)))))

(macroexpand '(entity-thread ball
                   (accel (Vec2f. 0 50))
                   (phys-step dt)
                   (bounce r r (- 1024 r) (- 768 r))))

(defn make-ball [x y vx vy rad col]
  (make-entity [(Position* (Vec2f. x y))
                (Velocity* (Vec2f. vx vy))
                (Physics* (Vec2f. 0 0) 1)
                (BallData* rad col)]
               ball-logic))

;(-> (make-entity [(Position* (Vec2f. 1 1))] nil) :Position :v .x)


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

  (doseq [{{^Vec2f posv :v} :Position ball-data :BallData} (vals @balls)]
    (apply q/fill (:color ball-data))
    (q/ellipse (.x posv) (.y posv) 1 1)))

(q/defsketch example
  :title "Oh so many grey circles"
  :setup setup
  :draw draw
  :size [1024 768])
