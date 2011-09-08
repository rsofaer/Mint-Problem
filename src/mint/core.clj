(ns mint.core
  (:use org.danlarkin.json)
  (:use clojure.contrib.math))

(defn prices [] (range 1 99))

(defn wf_denoms? [data]
  (let [denoms (data "denominations")]
    (and (vector? denoms) (= (count denoms) 5) (every? integer? denoms))))

(defn wf_exchange? [e]
  (and (vector? e)
       (= (count e) 2)
       (integer? (e 0))
       (let [path (e 1)]
         (and (= (count path) 5) (every? integer? path)))))

(defn wf_exchanges? [data]
  (let [exchs (data "exchanges")]
    (and (vector? exchs) 
         (= (count exchs) 99) 
         (every? wf_exchange? exchs))))

(defn well_formed? [data]
   (and (wf_denoms? data) (wf_exchanges? data)))

;exchange number should equal sum of absolute values of 
(defn valid_exchange_number? [num path]
  (= num (reduce + (map abs path))))

(defn change_adds_up? [denoms price path]
  (let [sum (reduce + (map * denoms path))]
    (= (rem (+ 100 sum) 100) price))) ; the sum of the exchange + 100 mod 100 should be the price

(defn valid_exchange? [denoms price exchange]
  (let [path (exchange 1)]
    (and (valid_exchange_number? (exchange 0) path)
         (change_adds_up? denoms price path))))

(defn valid_exchanges? [data]
  (every? valid_exchange? (repeat (data "denoms")) prices (data "exchanges")))

; Score is sum of the costs of all non-multiples of 5 + sum of N * costs of the multiples of 5. 
; For example, if the cost of every entry were 2. 
; Then the total score would be (99-19)*2 + (19*N*2). 
(defn score [price exchange_num n]
  (if (= (rem price 5) 0)
      (* exchange_num n)
      n))

(defn total_score [data n]
  (let [exchange_nums (map get (data "exchanges") (repeat 0))]
    (reduce + (map score prices exchange_nums (repeat n)))))

