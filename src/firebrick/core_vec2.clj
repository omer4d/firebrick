(ns firebrick.core
  (:import firebrick.core.Vec2f))

(load "core_vec")

(extend-protocol vec-protocol Vec2f
  (v+ ([^Vec2f v1 ^Vec2f v2]                                         (Vec2f/sum v1 v2))
      ([^Vec2f v1 ^Vec2f v2 ^Vec2f v3]                               (Vec2f/sum v1 v2 v3))
      ([^Vec2f v1 ^Vec2f v2 ^Vec2f v3 ^Vec2f v4]                     (Vec2f/sum v1 v2 v3 v4))
      ([^Vec2f v1 ^Vec2f v2 ^Vec2f v3 ^Vec2f v4 ^Vec2f v5]           (Vec2f/sum v1 v2 v3 v4 v5))
      ([^Vec2f v1 ^Vec2f v2 ^Vec2f v3 ^Vec2f v4 ^Vec2f v5 ^Vec2f v6] (Vec2f/sum v1 v2 v3 v4 v5 v6)))

  (v- ([^Vec2f v1 ^Vec2f v2]                                         (Vec2f/diff v1 v2))
      ([^Vec2f v1 ^Vec2f v2 ^Vec2f v3]                               (Vec2f/diff v1 v2 v3))
      ([^Vec2f v1 ^Vec2f v2 ^Vec2f v3 ^Vec2f v4]                     (Vec2f/diff v1 v2 v3 v4))
      ([^Vec2f v1 ^Vec2f v2 ^Vec2f v3 ^Vec2f v4 ^Vec2f v5]           (Vec2f/diff v1 v2 v3 v4 v5))
      ([^Vec2f v1 ^Vec2f v2 ^Vec2f v3 ^Vec2f v4 ^Vec2f v5 ^Vec2f v6] (Vec2f/diff v1 v2 v3 v4 v5 v6)))

  (vdot ^double [^Vec2f v1 ^Vec2f v2] (Vec2f/dot v1 v2))
  (vnormal [^Vec2f v] (Vec2f/normal v))
  (vlen [^Vec2f v] (Vec2f/length v))
  (vunit [^Vec2f v] (Vec2f/unit v))
  (vunit-normal [^Vec2f v] (Vec2f/unitNormal v))
  (vlerp [^Vec2f v1 ^Vec2f v2 ^double k] (Vec2f/lerp v1 v2 k)))
