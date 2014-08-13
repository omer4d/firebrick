(ns firebrick.core)

(load "core_vec2")

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

(defn move [{pos :Position} d]
  (assoc pos :v (v+ (:v pos) d)))

(defn setvel [{vel :Velocity} d]
  (assoc vel :v d))

(defn vspring [{vel :Velocity, pos :Position} base k]
  (assoc vel :v (v+ (:v vel) (v* (v- base (:v pos)) k))))

(defn bounce [{pos :Position, vel :Velocity, phys :Physics} x0 y0 x1 y1]
  (let [^Vec2f velv (:v vel)
        ^Vec2f posv (:v pos)
        vx1 (* (.x velv) (if (outside-range? (.x posv) x0 x1) -1 1))
        vy1 (* (.y velv) (if (outside-range? (.y posv) y0 y1) -1 1))]
    [(assoc vel :v (Vec2f. vx1 vy1))
     (assoc pos :v (Vec2f. (clamp (.x posv) x0 x1) (clamp (.y posv) y0 y1)))]))
