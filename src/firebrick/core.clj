(ns firebrick.core)

(load "core_comp")
(load "core_entity")

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

(defn ball-logic [ball dt]
  (entity-thread ball (accel 0 10) (phys-step dt)))

(defn make-ball [x y rad col]
  (make-entity [(Position* x y)
                (Physics* 1 0 0 0 0)
                (BallData* rad col)]
               ball-logic))

(let [ball (make-ball 0 0 10 "red")]
  ((:logic ball) ball 0.1))
