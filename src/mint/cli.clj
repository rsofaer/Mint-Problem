(ns mint.cli
  (:require [mint.core :as core]
            [org.danlarkin.json :as json])
  (:gen-class)
  (:use clojure.contrib.command-line
        clojure.contrib.shell-out))
(import '(java.io BufferedReader InputStreamReader)) 
(defn cmd [p] (.. Runtime getRuntime (exec (str p)))) 
(defn cmdout [o] 
  (let [r (BufferedReader. 
             (InputStreamReader. 
               (.getInputStream o)))] 
    (slurp r))) 

(defn process-file [n file] 
  (let [command (str "timeout -s 9 120 " file " " n)
        output (json/decode (cmdout (cmd command)))]
  (core/process_output output n)))
(defn -main [& args]
  (with-command-line args
      "Mint Problem Scorer"
      [[n "This is the multiple by which multiples of 5 are more likely than other exchanges."]
       [file "The executable file which will take n as input and print a json output file to stdout"]]
    (println "N: " n)
    (println "File: " file)

    (println "Score: " ((process-file (core/parse-number n) file) :score))
    ))

