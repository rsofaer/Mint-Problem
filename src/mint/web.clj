(ns mint.web
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))

(defroutes main-routes
  (GET "/" [] "<h1>Hello from Raphael!</h1>")
  (route/resources "/") ;maps to the public/ directory
  (route/not-found "Page not found"))

(defn app [req]
   (handler/site main-routes))

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))]
    (run-jetty app {:port port})))

