(ns firebrick.core)

(defrecord Vec2 [x y])

(defn vec2+ [v1 v2]
  (Vec2. (+ (.x v1) (.x v2))
         (+ (.y v1) (.y v2))))

(defn vec2- [v1 v2]
  (Vec2. (- (.x v1) (.x v2))
         (- (.y v1) (.y v2))))

(defn vec2-len [v]
  (Math/sqrt (+ (squared (.x v)) (squared (.y v)))))

(defn vec2-scale [v k]
  (Vec2. (* (.x v) k)
         (* (.y v) k)))
