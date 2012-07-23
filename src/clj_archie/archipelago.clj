(ns clj-archie.archipelago
      (:use [clj-archie.util])
      (:use [clj-archie.island])
      )

(defn display-map [i]
    (println (apply str (interpose "\n" (map str-island i)))))

(defn random-island-seq [map-width map-height]
    (cons (random-island map-width map-height) (lazy-seq (random-island-seq map-width map-height))))

(defn random-map [num-islands map-width map-height]
    (take num-islands (random-island-seq map-width map-height)))
