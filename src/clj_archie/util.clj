(ns clj-archie.util)

(defn diminished-return [val scale]
    (if (or (<= val 0) (<= scale 0))
        0
        (let [
            factor (/ (double val) (double scale))
            position (/ (dec (Math/sqrt (inc (* 8.0 factor)))) 2.0) ]
            (long (Math/round (* (double scale) position))))))

(defn distance-between [[x1 y1] [x2 y2]]
    (Math/sqrt
        (+
            (Math/pow (- x2 x1) 2)
            (Math/pow (- y2 y1) 2))))

(defn pad-num [x pad-chars]
    (let [
        sval (str x)
        slen (count sval)]
        (str sval (reduce str (repeat (- pad-chars slen) " ")))))

(defn constrain-within [x a b]
    (assert (<= a b))
    (min (max x a) b))

(defn random-plus-minus [val plus-minus-pct]
    (assert (>= val 0))
    (assert (<= 0.0 plus-minus-pct))
    (assert (>= 1.0 plus-minus-pct))
    (long (Math/round (* val (+ (- 1.0 plus-minus-pct) (rand (* 2.0 plus-minus-pct)))))))

