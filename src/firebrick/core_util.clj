(ns firebrick.core)

(defn sign [x] (if (< x 0) -1 1))

(defn clamp [x minx maxx] (min (max x minx) maxx))

(defn outside-range? [x minx maxx] (or (< x minx) (> x maxx)))

(defn squared [x] (* x x))

(defn cubed [x] (* x x x))

(defn dist2d [x1 y1 x2 y2]
  (let [dx (- x2 x1)
        dy (- y2 y1)]
    (Math/sqrt (+ (* dx dx) (* dy dy)))))
