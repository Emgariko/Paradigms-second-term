(defn vector-operation [operation args] (apply mapv operation args))
(defn
  
  v+ [& args] (vector-operation + args))
(defn v- [& args] (vector-operation - args))
(defn v* [& args] (vector-operation * args))

(defn scalar [a b] (reduce + (mapv * a b)))
(defn v*s [a & args] (mapv (partial * (apply * args)) a))
(defn vect [& args]
  (reduce (fn [a b] (vector
                      (- (* (nth a 1) (nth b 2)) (* (nth b 1) (nth a 2)))
                      (- (* (nth b 0) (nth a 2)) (* (nth a 0) (nth b 2)))
                      (- (* (nth a 0) (nth b 1)) (* (nth b 0) (nth a 1)))
                      )
            )
          args)
  )

(defn matrix-operation [vector-op args] (apply mapv vector-op args))
(defn m+ [& args] (matrix-operation v+ args))
(defn m- [& args] (matrix-operation v- args))
(defn transpose [m] (apply mapv vector m))
(defn m*v [m v] (mapv (fn [x] (reduce + (v* x v))) m))
(defn m*s [m & ss] (mapv (fn [x] (v*s x (apply * ss))) m))
(defn m* [& args] (reduce (fn [a b] (mapv (fn [x y] (mapv * x y)) a b)) args))
(defn m*m [& args] (reduce (fn [a b] (transpose (mapv (partial m*v a) (transpose b)))) args))
