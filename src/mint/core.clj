(ns mint.core
  (:use clojure.contrib.math))

(def prices (range 1 100))

(defn wf_denoms? [data]
  (let [denoms (data :denominations)]
    (and (vector? denoms) (= (count denoms) 5) (every? integer? denoms))))

(defn wf_exchange? [exchange]
  (and (vector? exchange)
       (= (count exchange) 5)
       (every? integer? exchange)))

(defn wf_exchanges? [data]
  (let [exchs (data :exchanges)]
    (and (vector? exchs) 
         (= (count exchs) 99) 
         (every? wf_exchange? exchs))))

(defn well_formed? [data]
   (and (not (or (nil? data) (= "" data))) (wf_denoms? data) (wf_exchanges? data)))

(defn abs_sum [coll]
  (reduce + (map abs coll)))

(defn valid_exchange? [denoms price exchange]
  (let [sum (reduce + (map * denoms exchange))]
    (= (rem (+ 100 sum) 100) price))) ; the sum of the exchange + 100 mod 100 should be the price

(defn valid_exchanges? [data]
  (every? true?(map valid_exchange? (repeat (data :denominations)) prices (data :exchanges))))

; Score is sum of the costs of all non-multiples of 5 + sum of N * costs of the multiples of 5. 
; For example, if the cost of every entry were 2. 
; Then the total score would be (99-19)*2 + (19*N*2). 
(defn score [price exchange n]
  (if (= (rem price 5) 0)
      (* (abs_sum exchange) n)
      (abs_sum exchange)))

(defn total_score [data n]
   (reduce + (map score prices (data :exchanges) (repeat n))))

(defn process_output [data n]
  (let [well_formed (well_formed? data)
        valid (and well_formed (valid_exchanges? data))]
      {:well-formed well_formed :valid valid :score (if (and well_formed valid) (total_score data n) 0 )}))

(let [m (.getDeclaredMethod clojure.lang.LispReader
                            "matchNumber"
                            (into-array [String]))]
  (.setAccessible m true)
  (defn parse-number [s]
    (.invoke m clojure.lang.LispReader (into-array [s]))))
