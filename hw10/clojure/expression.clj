; delay

(defn constant [value] (fn [& args] (+ value)))
(defn variable [var] (fn [vars] (get vars var)))
(defn expr-operator [func] (fn [& a]
                             (fn [arg] (apply func
                                             (mapv (fn [f] (f arg)) a)))))

(def negate (expr-operator -))
(def add (expr-operator +))
(def subtract (expr-operator -))
(def divide (expr-operator (fn [a b] (/ (double a) (double b)))))
(def multiply (expr-operator *))
(def avg (expr-operator (fn [& a] (/ (apply + a) (count a)))))
(def med (expr-operator (fn [& a] (nth (sort a) (int (/ (count a) 2))))))

(def tokenToOperation {'+ add '- subtract '* multiply '/ divide 'negate negate 'avg avg 'med med})

(defn parseFunction [expr]
  (letfn [(myParse [token] (cond (symbol? token) (variable (str token)) (number? token) (constant token)
                  :else (apply (tokenToOperation (first token)) (mapv myParse (rest token)))
                  )
            )
          ]
    (myParse (read-string expr))
    )
  )

