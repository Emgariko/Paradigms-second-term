(defn vector-operation [operation args] (apply mapv operation args))
(defn v+ [& args] (vector-operation + args))
(defn v- [& args] (vector-operation - args))
(defn v* [& args] (vector-operation * args))

(defn scalar [a b] (reduce + (mapv * a b)))
(defn v*s [a & args] (mapv (partial * (apply * args)) a))


;(print (v+ [1 0 0] [0 1 0] [0 0 1]))
;(print (scalar [2 3 -1] [3 -1 2]))
(print (v*s [1 2 3] 2 3))