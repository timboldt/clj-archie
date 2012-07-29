(ns clj-archie.core
    (:use [clj-archie.island])
    (:use [clj-archie.archipelago])
    )

(def map-width 800)
(def map-height 600)
(defn random-gene []
    (apply str (repeatedly 12 #(rand-nth "ABCDEFGHIJKLMNOPQRSTUVWXYZ"))))

(defn -main
    [& args]
    (let [
        i (random-island 100 100 (random-gene))
        f 0.4
        g 0.3
        ]
        (println (str-island i))
        (println (str-island (many-years 1 [f g] i)))
        (println (str-island (many-years 2 [f g] i)))
        (println (str-island (many-years 3 [f g] i)))
        (println (str-island (many-years 4 [f g] i)))
        (println (str-island (many-years 5 [f g] i)))
        (println (str-island (many-years 10 [f g] i)))
        (println (str-island (many-years 20 [f g] i)))
        (println (str-island (many-years 100 [f g] i)))
        (println (str-island (many-years 1000 [f g] i)))
        )
)

(comment "
(display-map (random-map 100 map-width map-height)))
(println (map str-island (take 5 (random-island-seq 100 100))))
")
