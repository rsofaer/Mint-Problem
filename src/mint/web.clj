(ns mint.web
  (:use compojure.core
        ring.adapter.jetty
        hiccup.core
        hiccup.form-helpers)
  (:require [compojure.route :as route]
            [mint.core :as core]
            [org.danlarkin.json :as json]
            [clojure.contrib.duck-streams :as ds]
            [ring.middleware.multipart-params :as mp]
            [compojure.handler :as handler]))

(let [m (.getDeclaredMethod clojure.lang.LispReader
                            "matchNumber"
                            (into-array [String]))]
  (.setAccessible m true)
  (defn parse-number [s]
    (.invoke m clojure.lang.LispReader (into-array [s]))))

(defn index-page [req]
  (html
   [:head
    [:title "The Mint Problem"]]
   [:body
    [:h1 "The Mint Problem" ]
    [:h3 "An Output Validator"]
    [:p "A sample output file can be seen " [:a {:href "/sample_output.json"} "here"] "." ]
    [:p "It consists of a JSON document with an array of coin denominations and an array of 99 exchanges," [:br]
        " each in the form [n_pennies, n_nickels, n_dimes, n_quarters, n_half_dollars]"]
    [:p "Your final solution file should output to stdout, but you can upload your output here to make sure it will be accepted."]]
    [:form {:action "/" :method "post" :enctype "multipart/form-data"} 
       (file-upload "output")
       [:p [:label "Pick a value for N:"]
           [:input {:type "number" :name "multiple" :value 1 :min 0.1 :step 0.1}]]
       (submit-button "submit")]))

(defn response-template [results] 
  (html 
    [:head 
      [:title "The Mint Problem"]]
    [:body
     [:h1 "Your Results:"]

    (if (false? (results :well-formed))
      [:p "Your results were not formatted correctly.  Try looking at " [:a {:href "/sample_output.json"} "the sample data"] " for a guide" ]
      [:p "Your results were formatted correctly."])
      ]
    (if (false? (results :valid))
      [:p "Your exchanges were not valid.  Make sure the combination of the number of coins and the denominations of the coins add up to the correct prices."]
      [:p "Your exchanges were valid."])
    [:p "Your score is: " (results :score)] ))

(defn response-page [upload multiple]
  (let [output-data (try 
                      (json/decode (slurp (upload :tempfile)))
                      (catch Exception e {:error "Maybe this wasn't a json document?"}))]
    (response-template (core/process_output output-data (parse-number multiple)))))

(defroutes main-routes
  (GET "/" [] index-page)
  (mp/wrap-multipart-params
    (POST "/" [output multiple] (response-page output multiple)))
  (route/resources "/") ;maps to the public/ directory
  (route/not-found "Page not found"))

(def app
   (handler/site main-routes))

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))]
    (run-jetty app {:port port})))

