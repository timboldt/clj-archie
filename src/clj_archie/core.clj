(ns clj-archie.core
    (:use [clj-archie.island])
    (:use [clj-archie.archipelago])
    )

(def map-width 800)
(def map-height 600)

(defn -main
    [& args]
    (display-map (random-map 100 map-width map-height)))

(comment
    (println (map str-island (take 5 (random-island-seq 100 100))))

    (let [
        i (random-island 100 100)
        ]
        (println (str-island i))
        (println (str-island (many-years 10 [0.3 0.3] i)))
        (println (str-island (many-years 100 [0.3 0.3] i)))
        (println (str-island (many-years 1000 [0.3 0.3] i))))
)