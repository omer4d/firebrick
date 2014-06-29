(ns firebrick.core)

(defn sign [x] (if (< x 0) -1 1))

(defn clamp [x minx maxx] (min (max x minx) maxx))

(defn outside-range? [x minx maxx] (or (< x minx) (> x maxx)))
