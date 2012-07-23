(ns clj-archie.core
    (:use [clj-archie.island]))

(defn -main
    [& args]
    (let [
        i (random-island 100 100)
        ]
        (println i)
        (println (many-years 10 [0.3 0.3] i))
        (println (many-years 100 [0.3 0.3] i))
        (println (many-years 1000 [0.3 0.3] i))))
