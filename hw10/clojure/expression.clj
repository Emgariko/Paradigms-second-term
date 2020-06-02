; review

(defn constant [value] (fn [& args] (+ value)))
(defn variable [var] (fn [vars] (get vars var)))
(defn expr-operation [func] (fn [& a]
                             (fn [arg] (apply func
                                             (mapv #(%1 arg) a)))))
(def negate (expr-operation -))
(def add (expr-operation +))
(def subtract (expr-operation -))
(def divide (expr-operation #(/ (double %1) (double %2))))
(def multiply (expr-operation *))
(def avg (expr-operation #(/ (apply + %&) (count %&))))
(def med (expr-operation #(nth (sort %&) (int (/ (count %&) 2)))))

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
