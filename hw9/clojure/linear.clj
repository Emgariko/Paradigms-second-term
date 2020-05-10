(defn vsize-equals? [args] (every? (fn [x] (== (count (first args)) (count x))) args))
(defn msize-equals? [args] (vsize-equals? args))
(defn tsize-equals? [args] (vsize-equals? args))
(defn isVector? [v] (and (every? number? v) (vector? v)))
(defn isMatrix? [m] (and (vector? m) (every? isVector? m) (vsize-equals? m)))
(defn isTensor? [t] (or (number? t)
                        (every? number? t) (and (vsize-equals? t)
                                                (isTensor? (reduce (fn [a, b] (apply conj a b)) t)))
                        )
  )


(defn operation-precond [size-equals? args] (or (every? number? args) (size-equals? args)))
(defn create-operation [isType? size-equals?]
  (fn [operation]
    (letfn [(tensor-calc' [& args]
              {:pre [(operation-precond size-equals? args)]}
              (if (every? number? args) (apply operation args) (apply mapv tensor-calc' args)))]
      (fn [& args] {:pre [(every? isType? args)] :post [(isType? %)]} (apply tensor-calc' args)))))

(def v-operation (create-operation isVector? vsize-equals?))

(def v+ (v-operation +))
(def v- (v-operation -))
(def v* (v-operation *))

(defn scalar [& args] {:pre [(every? isVector? args) (vsize-equals? args) (== (count args) 2)]}
  (reduce + (apply mapv * args)))
(defn v*s [a & args] {:pre [(isVector? a) (every? number? args)]} (mapv (partial * (apply * args)) a))
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
(def m-operation (create-operation isMatrix? msize-equals?))
(def m+ (m-operation +))
(def m* (m-operation *))
(def m- (m-operation -))
(defn transpose [m] (apply mapv vector m))
(defn m*v [m v] {:pre [(isVector? v) (isMatrix? m)]} (mapv (fn [x] (reduce + (v* x v))) m))
(defn m*s [m & ss] {:pre [(isMatrix? m) (every? number? ss)]} (mapv (fn [x] (v*s x (apply * ss))) m))
(defn m*m [& args] {:pre [(every? isMatrix? args)]}
  (reduce (fn [a b] (transpose (mapv (partial m*v a) (transpose b)))) args))

(def t-operation (create-operation isTensor? tsize-equals?))
(def t+ (t-operation +))
(def t- (t-operation -))
(def t* (t-operation *))