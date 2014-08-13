(ns firebrick.core)

(defn circles-intersect? [^Vec2f p1 ^double r1 ^Vec2f p2 ^double r2]
  (< (sqdist p1 p2) (* (+ r1 r2) (+ r1 r2))))

(defn separate-circles [^Vec2f p1 ^double r1 ^Vec2f p2 ^double r2]
  "Minimal translation vector to push the second circle out of the first."
  (let [d2 (sqdist p1 p2)]
    (if (< d2 (* (+ r1 r2) (+ r1 r2)))
      (let [d (Math/sqrt d2)]
        (v* (v- p2 p1) (/ 1.0 d) (+ r1 r2 (- d))))
      (Vec2f. 0 0))))

(circles-intersect? (Vec2f. 0 0) 100 (Vec2f. 200 0) 100)

(separate-circles (Vec2f. 0 0) 100 (Vec2f. 150 0) 100)
