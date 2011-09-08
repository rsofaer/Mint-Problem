(ns mint.web
  (:use compojure.core
        ring.adapter.jetty)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))

(defn index-page []
  "<h1> Hello from Raphael!</h1>")
(defroutes main-routes
  (GET "/" [] (index-page))
  (route/resources "/") ;maps to the public/ directory
  (route/not-found "Page not found"))

(defn app [req]
   (handler/site main-routes))

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))]
    (run-jetty app {:port port})))

