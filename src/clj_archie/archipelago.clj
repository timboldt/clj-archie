(ns clj-archie.archipelago
      (:use [clj-archie.util])
      (:use [clj-archie.island])
      )

(defn display-map [i]
    (println (apply str (interpose "\n" (map str-island i)))))

(defn random-map [num-islands map-width map-height gene]
    (take num-islands (random-island-seq map-width map-height gene)))
