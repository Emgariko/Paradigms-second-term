; delay & review

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

(definterface Expression
  (evaluate [vars])
  (toString [])
  (diff [var]))

(deftype Const [value]
  Expression
  (evaluate [this other] value)
  (toString [this] (str value))
  (diff [this other] 0))
(deftype Variable [variable]
  Expression
  (evaluate [this vars] (vars variable))
  (toString [this] (str variable))
  (diff [this diffVar] (if (= (variable diffVar)) 1 0)))

(deftype Operation [operation symbol diffRule operands]
  Expression
  (evaluate [this vars] (apply operation (mapv #(.evaluate %1 vars) (vec operands))))
  (toString [this] (apply clojure.string/join " " "(" symbol (apply str (mapv #(.toString %1) (vec operands)))))
  (diff [this diffVar] (apply diffRule diffVar (vec operands))))

(def Add (partial Operation + "+" ()))

;(print (.toString (Const. 10.0123)))
;(print (.evaluate (Variable. "x" ) {"x" 2}))
;(print (.toString (Variable. "x")))