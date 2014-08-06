(ns firebrick.core)

(defprotocol vec-protocol
  (v+ [v1 v2] [v1 v2 v3] [v1 v2 v3 v4] [v1 v2 v3 v4 v5] [v1 v2 v3 v4 v5 v6])
  (v- [v1 v2] [v1 v2 v3] [v1 v2 v3 v4] [v1 v2 v3 v4 v5] [v1 v2 v3 v4 v5 v6])
  (vdot [v1 v2])
  (vnormal [v])
  (vlen [v])
  (vunit [v])
  (vunit-normal [v])
  (vlerp [v]))
