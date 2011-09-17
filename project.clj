(defproject mint "0.0.1"
  :dependencies
    [[org.clojure/clojure "1.2.1"]
     [ring/ring-jetty-adapter "0.3.9"]
     [org.clojure/clojure-contrib "1.2.0"]
     [compojure "0.6.5"]
     [hiccup "0.3.6"]
     [org.danlarkin/clojure-json "1.2-SNAPSHOT"]]
  :dev-dependencies [[lein-ring "0.4.5"]]
  :ring {:handler mint.web/app}
  :main mint.cli)

