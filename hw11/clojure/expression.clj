; review & delay hw 11
; accepted hw10

;***************;
;     HW 10     ;
;***************;

(defn constant [value] (fn [& args] (+ value)))
(defn variable [var] (fn [vars] (get vars var)))
(defn expr-operation [func] (fn [& a]
                              (fn [arg] (apply func
                                               (mapv #(%1 arg) a)))))

(def div #(/ (double %1) (double %2)))
(def negate (expr-operation -))
(def add (expr-operation +))
(def subtract (expr-operation -))
(def divide (expr-operation div))
(def multiply (expr-operation *))
(def avg (expr-operation #(/ (apply + %&) (count %&))))
(def med (expr-operation #(nth (sort %&) (int (/ (count %&) 2)))))

(def tokenToOperation {'+ add '- subtract '* multiply '/ divide 'negate negate 'avg avg 'med med})
(defn parse [mappp var' const']
  (fn [expr] (letfn [(myParse [token] (cond (symbol? token) (var' (str token)) (number? token) (const' token)
                                            :else (apply (mappp (first token)) (mapv myParse (rest token)))
                                 )
            )]
    (myParse (read-string expr))
    )
  ))

(def parseFunction (parse tokenToOperation variable constant))

;***************;
;     HW 11     ;
;***************;
; review & delay hw 11


(definterface Expression
  (evaluate [vars])
  (toString [])
  (diff [varr]))

(declare ZERO)
(declare ONE)
(deftype Const [value]
  Expression
  (evaluate [this other] value)
  (toString [this] (format "%.1f" value))
  (diff [this other] ZERO))
(def ZERO (Const. 0.0))
(def ONE (Const. 1.0))
(deftype Var [variable']
  Expression
  (evaluate [this vars] (vars variable'))
  (toString [this] (str variable'))
  (diff [this diffVar] (if (= variable' diffVar) ONE ZERO)))

(deftype Operation [operation symbol diffRule operands]
  Expression
  (evaluate [this vars] (apply operation (mapv #(.evaluate %1 vars) (vec operands))))
  (toString [this] (str "(" symbol " " (apply clojure.string/join " " (vector (mapv #(.toString %1) (vec operands)))) ")"))
  (diff [this diffVar] (apply diffRule diffVar (vec operands))))

(defn toString [a] (.toString a))
(defn diff [exp varr] (.diff exp varr))
(defn evaluate [exp varr] (.evaluate exp varr))

(defn create-operation [op sym diff']  (fn [& operands] (Operation. op sym diff' operands)))
(defn create-common-diff [func] (fn [varr & args] (apply func (mapv #(.diff % varr) args))))
(def Add (create-operation + "+" (create-common-diff #(apply Add %&))))
(def Subtract (create-operation - "-" (create-common-diff #(apply Subtract %&))))
(declare Multiply)
(defn mul-diff [varr & args]
                 (if (= 1 (count args))
                   (diff (first args) varr)
                   (Add (Multiply (diff (apply Multiply (rest args)) varr) (first args))
                     (apply Multiply (diff (first args) varr) (rest args)))
                   ))

(def Multiply (create-operation * "*" mul-diff))
(declare Divide)
;(def Negate (create-operation - "negate" (fn [var & args] (Negate (diff (first args) var)))))
(def Negate (create-operation - "negate" (create-common-diff #(apply Negate %&))))
(defn divide-diff [varr & args]
                   (if (= (count args) 1)
                     (diff (first args) varr)
                     (Divide
                       (Subtract
                         (Multiply (diff (first args) varr) (apply Multiply (rest args)))
                         (Multiply (first args) (diff (apply Multiply (rest args)) varr)))
                       (Multiply (apply Multiply (rest args)) (apply Multiply (rest args))))))
(def Divide
  (create-operation (fn div'
                      ([first] first)
                      ([first & args] (/ (double first) (apply * args))))
                    "/" divide-diff))
(defn sumdiff' [varr args] (apply Add (mapv #(.diff % varr) args)))
(def Sum (create-operation + "sum" (create-common-diff #(apply Add %&))))
(def Avg (create-operation #(/ (double (apply + %&)) (double (count %&))) "avg"
                           (fn [varr & args] (Divide (sumdiff' varr args) (Const. (count args))))))

(defn Constant [arg] (Const. arg))
(defn Variable [arg] (Var. arg))
(def tokenToObjOperation {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'sum Sum 'avg Avg})
(def parseObject (parse tokenToObjOperation Variable Constant))

(print (toString (diff (Multiply (Constant 4.0) (Variable "z")) "x")))