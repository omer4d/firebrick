(ns firebrick.core
  (:require [quil.core :as q]))

(load "core_comp")
(load "core_entity")
(load "core_util")

(defcomp Position [x y])

(defcomp Physics [m fx fy vx vy])

(defcomp BallData [radius color])

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
  (let [r (:radius (:BallData ball))]
    (entity-thread ball
                   (accel 0 50)
                   (phys-step dt)
                   (bounce r r (- 1024 r) (- 768 r)))))

(defn make-ball [x y vx vy rad col]
  (make-entity [(Position* x y)
                (Physics* 1 0 0 vx vy)
                (BallData* rad col)]
               ball-logic))

(defn make-entity-map [ents]
  (zipmap (range) ents))

(defn repel [ents]
  (for [{pos1 :Position phys1 :Physics} ents
        {pos2 :Position phys2 :Physics} ents]
    ()))

(defn simulate-balls [ent-map dt]
  (into {} (for [[k v] ent-map] [k (run-entity-logic v dt)])))

(defn generate-random-balls [n]
  (repeatedly n #(make-ball (rand-int 1024) (rand-int 768) (rand-int 100) (rand-int 100) (+ 25 (rand-int 25)) [(rand-int 255) (rand-int 255) (rand-int 255)])))

(def balls (atom (make-entity-map (generate-random-balls 10))))

(defn setup []
  (q/smooth)
  (q/frame-rate 60)
  (q/background 200))

(defn draw []
  (q/background-float 200)
  (swap! balls simulate-balls 0.05)
  (doseq [{pos :Position ball-data :BallData} (vals @balls)]
    (apply q/fill (:color ball-data))
    (q/ellipse (:x pos) (:y pos) (* 2 (:radius ball-data)) (* 2 (:radius ball-data)))))

(q/defsketch example
  :title "Oh so many grey circles"
  :setup setup
  :draw draw
  :size [1024 768])
