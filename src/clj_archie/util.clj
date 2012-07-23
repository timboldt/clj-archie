(ns clj-archie.util)

(defn diminished-return [val scale]
    (if (or (<= val 0) (<= scale 0))
        0
        (let [
            factor (/ (double val) (double scale))
            position (/ (dec (Math/sqrt (inc (* 8.0 factor)))) 2.0) ]
            (long (Math/round (* (double scale) position))))))

(defn distance-from [[x1 y1] [x2 y2]]
    (Math/sqrt
        (+
            (Math/pow (- x2 x1) 2)
            (Math/pow (- y2 y1) 2))))