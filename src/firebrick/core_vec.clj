(ns firebrick.core
  (:import firebrick.core.Vec2f))

(defrecord Vec2 [^double x ^double y])

(defn vec2+ [^Vec2 v1 ^Vec2 v2]
  (Vec2. (+ (.x v1) (.x v2))
         (+ (.y v1) (.y v2))))

(defn getx ^double [^Vec2 v] (.x v))
(defn gety ^double [^Vec2 v] (.y v))



(defn ^Vec2 bazbaz [^Vec2 v1 ^Vec2 v2]
   (Vec2. (+ (getx v1) (getx v2)) (+ (gety v1) (gety v2))))

;(defn ^Vec2 bazbaz [^Vec2 v1 ^Vec2 v2]
;  (let [x1 (float (.x v1))
;        y1 (float (.y v1))
;        x2 (float (.x v2))
;        y2 (float (.y v2))]
;    (Vec2. (+ x1 x2) (+ y1 y2))))

(defn vec2- [v1 v2]
  (Vec2. (- (.x v1) (.x v2))
         (- (.y v1) (.y v2))))

(defn vec2-len [v]
  (Math/sqrt (+ (squared (.x v)) (squared (.y v)))))

(defn vec2-scale [v k]
  (Vec2. (* (.x v) k)
         (* (.y v) k)))


(defn v2+ [& vecs]
  (Vec2. (reduce #(+ %1 (getx %2)) 0 vecs)
         (reduce #(+ %1 (gety %2)) 0 vecs)))

(defn v4+ [& vecs]
  (Vec2. (reduce #(+ %1 (:x %2)) 0 vecs)
         (reduce #(+ %1 (:y %2)) 0 vecs)))

(defn v3+ [& vecs]
  (Vec2. (reduce + 0 (map getx vecs))
         (reduce + 0 (map gety vecs))))

(defn v5+ [& vecs]
  (reduce bazbaz (Vec2. 0 0) vecs))


(defn v6+ [& vecs]
  (reduce #(Vec2f/sum %1 %2) (Vec2f. 0 0) vecs))


(v5+ (Vec2. 1 2) (Vec2. 3 4) (Vec2. 5 6))


(def tmp (map #(Vec2. %1 %2) (range 1 100 2) (range 2 100 2)))
(def tmp2 (map #(Vec2f. %1 %2) (range 1 100 2) (range 2 100 2)))

(def v1 (Vec2. 1 1))
(def v2 (Vec2. 2 2))

;(Vec2. "zaza" "baz")

;(profile :info :FUN ())

;(Vec2f/sum (Vec2f. 1 2) (Vec2f. 3 4))

(time (dotimes [i 10000] (apply v3+ tmp)))
(time (dotimes [i 10000] (apply v5+ tmp)))
(time (dotimes [i 10000] (apply v2+ tmp)))
(time (dotimes [i 10000] (apply v4+ tmp)))
(time (dotimes [i 10000] (apply v6+ tmp2)))

;(comment (profile :info :FUN1 (dotimes [i 100000] (vec2+ v1 v2) ))

;(profile :info :FUN2 (dotimes [i 100000] (v2+ v1 v2) ))

;(profile :info :FUN3 (dotimes [i 100000] (v3+ v1 v2) )))

;(bench (+ 1 2))

;(defn kaka [s v] (+ s (.x v)))

;(reduce kaka 0 [(Vec2. 1 1) (Vec2. 2 2) (Vec2. 3 3)])
;(reduce vec2+ (Vec2. 0 0) [(Vec2. 1 1)])
