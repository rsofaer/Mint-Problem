(ns mint.core
  (:use org.danlarkin.json)
  (:use clojure.contrib.math))



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
  let [prices (range 1 99)
       exchanges (data "exchanges")
       denoms (data "denoms")]
       ;
       (every? valid_exchange? (repeat denoms) prices exchanges))

(defn wf_denoms? [data]
  (let [denoms (get data "denominations")]
    (and (vector? denoms) (= (count denoms) 5) (every? integer? denoms))))

(defn wf_exchange? [e]
  (and (vector? e)
       (= (count e) 2)
       (integer? (e 0))
       (let [path (e 1)]
         (and (= (count path) 5) (every? integer? path)))))

(defn wf_exchanges? [data]
  (let [exchs (get data "exchanges")]
    (and (vector? exchs) 
         (= (count exchs) 99) 
         (every? wf_exchange? exchs))))

(defn well_formed? [data]
   (and (wf_denoms? data) (wf_exchanges? data)))

