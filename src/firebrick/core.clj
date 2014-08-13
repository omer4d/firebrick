(ns firebrick.core
  (:require [quil.core :as q])
  (:import firebrick.core.Vec2f))

(use 'clojure.walk)

(load "core_comp")
(load "core_entity")
(load "core_util")
(load "core_vec")
(load "core_vec2")
(load "core_game")

(set! *warn-on-reflection* true)

(class (transient {}))

; Components:

(defcomp Position [^Vec2f v])

(defcomp Velocity [^Vec2f v])

(defcomp Physics [f m])

(defcomp BubbleData [group state])

(defcomp GridComp [i j])

; Helper funcs:

(defn jfactor [] (* (Math/sqrt 3) 0.5))

(defn xoffs [j cell-size] (* cell-size 0.5 (mod j 2)))

(defn ->j [y cell-size] (Math/floor (/ y (* cell-size (jfactor)))))

(defn ->i [x y cell-size]
  (let [j (->j y cell-size)]
    (Math/floor (/ (- x (xoffs j)) cell-size))))

(defn ->y [j cell-size] (+ (* j cell-size (jfactor)) (/ cell-size 2)))

(defn ->x [i j cell-size] (+ (* i cell-size) (/ cell-size 2) (xoffs j cell-size)))

(defn grid->world [i j cell-size] (Vec2f. (->x i j cell-size) (->y j cell-size)))

(defn bub-rad [] 15)

(defn bub-diam [] (* (bub-rad) 2))

(defn groups[] [:red :green :blue :cyan :magenta :yellow])

; Bubbles behaviors:

(defn attach-to-grid [{{i :i, j :j} :GridComp :as ent} cell-size]
  (vspring ent (grid->world i j cell-size) 5))

; logic:

(defn attached-bub-logic [bub dt]
  (let [r (bub-rad)]
    (entity-thread bub
                   (attach-to-grid (bub-diam))
                   (phys-step dt)
                   (bounce r r (- 1024 r) (- 768 r)))))

(defn free-bub-logic [bub dt]
  (let [r (bub-rad)]
    (entity-thread bub
                   (accel (Vec2f. 0 50))
                   (phys-step dt)
                   (bounce r r (- 1024 r) (- 768 r)))))

(defn shot-bub-logic [bub dt]
  (let [r (bub-rad)]
    (entity-thread bub
                   (phys-step dt)
                   (bounce r r (- 1024 r) (- 768 r)))))

(defn bub-logic [{{state :state} :BubbleData :as bub} dt]
  (cond
   (= state :free) (free-bub-logic bub dt)
   (= state :attached) (attached-bub-logic bub dt)
   (= state :shot) (shot-bub-logic bub dt)))

; entity factory funcs:

(defn make-bubble [x y group state]
  (make-entity [(Position* (Vec2f. x y))
                (Velocity* (Vec2f. 0 0))
                (Physics* (Vec2f. 0 0) 1)
                (BubbleData* group state)]
               bub-logic))

(defn make-grid-bubble [i j cell-size]
  (add-comp (make-bubble (->x i j cell-size) (->y j cell-size) (rand-nth (groups)) :attached) (GridComp* i j)))

(defn generate-random-grid [cols rows]
  (for [i (range 0 cols)
        j (range 0 rows)]
    (make-grid-bubble i j 30)))

; ENTITY LIST FUNCS:

(def game (atom (spawn (make-game) (generate-random-grid 11 80))))

(defn spawn! [ents] (swap! game spawn ents))

(defn set-ent [game k v] (assoc game :ent-map (assoc (:ent-map game) k v) ))

(defn set-ent! [k v] (swap! game set-ent k v) )

(set-ent! 99 (assoc-in (make-grid-bubble 9 9 30) [:Velocity :v] (Vec2f. 200 200)))

;(spawn! (make-bubble 200 400 :red :shot))

; Drawing:

(class (make-grid-bubble 0 0 30))

(defn setup []
  (q/frame-rate 1000)
  (q/background 200)
  (q/text-font (q/create-font "DejaVu Sans" 28 true)))

;(profile-crap)

(defn get-group-color [group]
  (cond
   (= group :red) [255 0 0]
   (= group :green) [0 255 0]
   (= group :blue) [0 0 255]
   (= group :cyan) [0 255 255]
   (= group :magenta) [255 0 255]
   (= group :yellow) [255 255 0]))

(defn draw []
  (q/background-float 200)

  (swap! game game-step 0.05)

  (doseq [{{^Vec2f posv :v} :Position, {group :group} :BubbleData} (vals (:ent-map @game))]
    (comment (apply q/fill (get-group-color group))
    (q/ellipse (.x posv) (.y posv) (bub-diam) (bub-diam))))

     (q/text (str (q/current-frame-rate))
          300   ; x
          100))

(q/defsketch example
  :title "Oh so many grey circles"
  :setup setup
  :draw draw
  :size [1024 768])
