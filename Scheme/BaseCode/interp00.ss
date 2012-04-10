;
; A Scheme Meta-Circular Interpreter, Version 0 (interp00.ss)
;

(define scheme-value
  (lambda (expr)
    (expr-value
     expr
     '((car . *car*) (cdr . *cdr*) (cons . *cons*) (eq? . *eq?*) (symbol? . *symbol?*) (apply . *apply*)))))

(define expr-value
  (lambda (expr env)
    (cond
      ((eq? expr #t) #t)
      ((eq? expr #f) #f)
      ((eq? expr '()) '())
      ((symbol? expr) (sym-value expr env))
      ((eq? (car expr) 'quote) (cadr expr))
      ((eq? (car expr) 'cond) (cond-value (cdr expr) env))
      ((eq? (car expr) 'lambda) (cons '*closure* (cons (cadr expr) (cons (caddr expr) (cons env '())))))
      (else (app-value (expr-value (car expr) env) (expr-list-value (cdr expr) env) env)))))

(define expr-list-value
  (lambda (expr-list env)
    (cond
      ((eq? expr-list '()) '())
      (else (cons (expr-value (car expr-list) env) (expr-list-value (cdr expr-list) env))))))

(define sym-value
  (lambda (sym env)
    (cond
      ((eq? sym (caar env)) (cdar env))
      (else (sym-value sym (cdr env))))))

(define cond-value
  (lambda (clauses env)
    (cond
      ((eq? (caar clauses) 'else) (expr-value (cadar clauses) env))
      ((eq? (expr-value (caar clauses) env) #f) (cond-value (cdr clauses) env))
      (else (expr-value (cadar clauses) env)))))

(define app-value
  (lambda (rator rand-list env)
    (cond
      ((symbol? rator)
       (cond
         ((eq? rator '*car*) (caar rand-list))
         ((eq? rator '*cdr*) (cdar rand-list))
         ((eq? rator '*cons*) (cons (car rand-list) (cadr rand-list)))
         ((eq? rator '*eq?*) (eq? (car rand-list) (cadr rand-list)))
         ((eq? rator '*symbol?*) (symbol? (car rand-list)))
         ((eq? rator '*apply*) (app-value (car rand-list) (cadr rand-list) env))))
      ((eq? (car rator) '*closure*) (expr-value (caddr rator) (augmented-env (cadr rator) rand-list (cadddr rator)))))))

(define augmented-env
  (lambda (sym-list rand-list env)
    (cond
      ((eq? sym-list '()) env)
      (else (cons (cons (car sym-list) (car rand-list)) (augmented-env (cdr sym-list) (cdr rand-list) env))))))
