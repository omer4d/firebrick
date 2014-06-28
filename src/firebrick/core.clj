(ns firebrick.core
  (:require [quil.core :as q]))

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

(defn setup []
  (q/smooth)                          ;; Turn on anti-aliasing
  (q/frame-rate 60)                    ;; Set framerate to 1 FPS
  (q/background 200))                 ;; Set the background colour to
                                      ;; a nice shade of grey.
(defn draw []
  (q/stroke (q/random 255))             ;; Set the stroke colour to a random grey
  (q/stroke-weight (q/random 10))       ;; Set the stroke thickness randomly
  (q/fill (q/random 255))               ;; Set the fill colour to a random grey

  (let [diam (q/random 100)             ;; Set the diameter to a value between 0 and 100
        x    (q/random (q/width))       ;; Set the x coord randomly within the sketch
        y    (q/random (q/height))]     ;; Set the y coord randomly within the sketch
    (q/ellipse x y diam diam)))         ;; Draw a circle at x y with the correct diameter

(q/defsketch example                  ;; Define a new sketch named example
  :title "Oh so many grey circles"    ;; Set the title of the sketch
  :setup setup                        ;; Specify the setup fn
  :draw draw                          ;; Specify the draw fn
  :size [323 200])
                    ;; You struggle to beat the golden ratio
