(defn vsize-equals? [args] (every? (fn [x] (== (count (first args)) (count x))) args))
(defn msize-equals? [args] (and (vsize-equals? args)))
(defn isVector? [v] (and (every? number? v) (vector? v)))
(defn isMatrix? [m] (and (vector? m) (every? isVector? m) (vsize-equals? m)))

(defn vector-operation [operation args] {:pre [(vsize-equals? args)]} (apply mapv operation args))
(defn v+ [& args]
  (vector-operation + args))
(defn v- [& args] (vector-operation - args))
(defn v* [& args] (vector-operation * args))

(defn scalar [& args]  {:pre [(vsize-equals? args) (== (count args) 2)]} (reduce + (apply mapv * args)))
(defn v*s [a & args] (mapv (partial * (apply * args)) a))
(defn vect [& args]
  {:pre [(vsize-equals? args)]}
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
(defn m* [& args] {:pre [(msize-equals? args)]} (reduce (fn [a b] (mapv (fn [x y] (mapv * x y)) a b)) args))
(defn m*m [& args] (reduce (fn [a b] (transpose (mapv (partial m*v a) (transpose b)))) args))
;(println (m* (vector (vector 1.1 2.1 3.1) (vector 4.1 5.1 6.1)) (transpose (vector (vector 7.1 8.1 9.1) (vector 0.1 1.1 2.1)))))
