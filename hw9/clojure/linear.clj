(defn vsize-equals? [args] (every? (fn [x] (== (count (first args)) (count x))) args))
(def msize-equals? vsize-equals?)
(def tsize-equals? msize-equals?)
(defn isVector? [v] (and (every? number? v) (vector? v)))
(defn isMatrix? [m] (and (vector? m) (every? isVector? m) (vsize-equals? m)))
(defn isTensor? [t] (or (number? t)
                        (every? number? t) (and (vsize-equals? t)
                                                (isTensor? (reduce #(apply conj %1 %2) t)))
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

(defn scalar [& args] {:pre [(every? isVector? args) (vsize-equals? args)]}
  (apply + (apply v* args)))
(defn v*s [a & args] {:pre [(isVector? a) (every? number? args)]} (let [val (apply * args)] (mapv (partial * val) a)))
(declare m*v)
(defn vect [& args]
  {:pre [(every? isVector? args) (vsize-equals? args)]}
  (reduce (fn [a b]
            (let [[x y z] a
                  m [[0 (- z) y]
                     [z 0 (- x)]
                     [(- y) x 0]]]
              (m*v m b))) args))

(def m-operation (create-operation isMatrix? msize-equals?))
(def m+ (m-operation +))
(def m* (m-operation *))
(def m- (m-operation -))
(defn transpose [m]
  {:pre [(isMatrix? m)]}
  (apply mapv vector m))
(defn m*v [m v] {:pre [(isVector? v) (isMatrix? m) (= (count (first m)) (count v))]} (mapv #(apply + (v* % v)) m))
(defn m*s [m & ss] {:pre [(isMatrix? m) (every? number? ss)]}
  (let [value (apply * ss)] (mapv #(v*s %1 value) m)))
(comment ":NOTE: too many transposes")
(comment "fixed")
;(defn m*m [& args] {:pre [(every? isMatrix? args)]}
;  (reduce (fn [a b] {:pre [(= (count (first a)) (count b))]} (transpose (mapv (partial m*v a) (transpose b)))) args))
(defn m*m [& args] {:pre [(every? isMatrix? args)]}  (reduce (fn [a b] {:pre [(= (count (first a)) (count b))]}
                                                               (mapv (partial m*v (transpose b)) a)) args))

(def t-operation (create-operation isTensor? tsize-equals?))
(def t+ (t-operation +))
(def t- (t-operation -))
(def t* (t-operation *))