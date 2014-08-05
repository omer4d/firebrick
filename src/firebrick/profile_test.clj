(ns firebrick.core
  (:require [taoensso.timbre :as timbre])
  (:import firebrick.core.Vec2f))

(load "core_comp")
(load "core_entity")
(load "core_util")
(load "core_vec")


(set! *warn-on-reflection* true)

(timbre/refer-timbre)

(def ^firebrick.core.java.Foo jfoo (firebrick.core.java.Foo. 10))

(time
  (let [foo (firebrick.core.java.Foo. 10)]
    (dotimes [i 1000000] (.sim jfoo 0.01 10))))

(println (.posY jfoo))



(defrecord Particle1 [m
                      vx
                      vy
                      x
                      y])

(defn sim1 [^Particle1 {:keys [m vx vy x y]} dt]
  (let [vx1 vx
        vy1 (+ vy (* 10 dt))
        x1 x
        y1 (+ y (* vy1 dt))]
    (Particle1. m vx1 vy1 x1 y1)))


(comment

(time
 (loop [i 0
       p (Particle1. 10 0 0 0 0)]
  (if (< i 1000000)
    (recur (inc i) (sim1 p 0.01))
    p)))

(time (last (take 1000000 (iterate #(sim1 % 0.01) (Particle1. 10 0 0 0 0)))))

(defn thingie-logic [ball dt]
    (entity-thread ball
                   (accel 0 10)
                   (phys-step dt)))


(defn make-thingie []
  (make-entity [(Position* 0 0)
                (Physics* 10 0 0 0 0)]
               thingie-logic))


(time
 (loop [i 0
       p (make-thingie)]
  (if (< i 1000000)
    (recur (inc i) (run-entity-logic p 0.01))
    p)))
)


(Vec2f. 10 10)

(Vec2f/length (Vec2f. 10 10))

;(defmacro apply-repeatedly [f in n]
;  )

;(println (firebrick.core.java.Foo/value))
