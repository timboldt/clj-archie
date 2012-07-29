(ns clj-archie.island
      (:use [clj-archie.util]))

(comment "Island structure"
    :population                 ;population
    :happiness                  ;happiness and well-being (100 is normal)
    :food-stored                ;food in storage
    :rawmat-stored              ;raw materials in storage
    :goods-stored               ;goods in storage
    :location                   ;vector containing location of island [x y]
    :food-production-factor     ;# workers at which food production is 1:1
    :rawmat-production-factor   ;# workers at which raw resource production is 1:1
    :goods-production-factor    ;# workers at which manufactured goods production is 1:1)
    :gene                       ;a string representing the genome of this island
)

;-----------------------------
; Random island generation
;-----------------------------

(defn random-island [map-width map-height gene]
    (let [
        pop (+ 50 (rand-int 500))
        ninety-percent (/ (* 10 pop) 9)
        twenty-percent (/ pop 5)]
        (hash-map
            :population pop
            :happiness (random-plus-minus 100 0.1)
            :food-stored (random-plus-minus pop 0.1)
            :rawmat-stored (random-plus-minus pop 0.1)
            :goods-stored (random-plus-minus pop 0.1)
            :location [(rand map-width) (rand map-height)]
            :food-production-factor (random-plus-minus pop 0.3)
            :rawmat-production-factor (random-plus-minus pop 0.9)
            :goods-production-factor (random-plus-minus pop 0.5)
            :gene gene
            )))

(defn random-island-seq [map-width map-height gene]
    (cons
        (random-island map-width map-height gene)
        (lazy-seq (random-island-seq map-width map-height gene))
        ))

;-----------------------------
; Genetics
;-----------------------------

(defn- decode-genome-char [c]
    (- (int c) 64)
    )

(defn- gene-values [[a b c d]]
    (list (+ a b c d) (+ b c d) (+ c d) d)
    )

(defn decode-genome [gene]
    (map #(gene-values (map decode-genome-char %)) (partition 4 gene))
    )

;-----------------------------
; Pretty printing
;-----------------------------

(defn str-island [island]
    (str
        "population=" (pad-num (:population island) 8)
        "happiness=" (pad-num (:happiness island) 8)
        "food,rawmat,goods=" (pad-num (list (:food-stored island) (:rawmat-stored island) (:goods-stored island)) 20)
        "location=" (:location island) " "
        "genome=" (:gene island)
        ))

;-----------------------------
; Intermediate results
;-----------------------------

(defn food-produced [workers {factor :food-production-factor population :population}]
    (assert (>= workers 0))
    (* 2 (diminished-return (min workers population) factor)))

(defn rawmat-produced [workers {factor :rawmat-production-factor population :population}]
    (assert (>= workers 0))
    (diminished-return (min workers population) factor))

(defn goods-produced [workers {factor :goods-production-factor population :population rawmat :rawmat-stored}]
    (assert (>= workers 0))
    (min rawmat (diminished-return (min workers population) factor)))

(defn food-consumed [{food :food-stored population :population}]
    (min food population))

(defn goods-consumed [{goods :goods-stored population :population}]
    (min goods population))

(defn calc-population [{food :food-stored population :population}]
    (if (<= population 0)
        0
        (let [
            starvation (long (max (- (* 0.75 population) food) 0))
            births (long (* 0.03 population))
            deaths (long (* 0.02 population))
            ]
            (constrain-within
                (+
                    (rand-int 2)
                    population
                    (- starvation)
                    births
                    (- deaths))
                0 (* 2 population)))))

(defn calc-happiness [{happiness :happiness food :food-stored goods :goods-stored population :population}]
    (if (<= population 0)
        0
        (let [
            food-quad (long (/ (* 4 food) population))
            goods-quad (long (/ (* 4 goods) population))
            ]
            (constrain-within
                (+
                    happiness
                    (case food-quad
                        0 -50
                        1 -25
                        2 -10
                        3 0
                        4 10
                        20)
                    (case goods-quad
                        0 -20
                        1 -10
                        2 -5
                        3 0
                        4 5
                        10))
                0 200))))


;-----------------------------
; Year iteration
;-----------------------------

(defn next-year [[pct-food pct-raw] island]
    ;>>>>> +/- trade of food/rawmat/goods and immigration/emmigration
    (let [
        food-workers (max 0 (* pct-food (:population island)))
        rawmat-workers (max 0 (* pct-raw (:population island)))
        goods-workers (- (:population island) food-workers rawmat-workers)]
        (hash-map
            :population (calc-population island)
            :happiness (calc-happiness island)
            :food-stored (+ (food-produced food-workers island) (- (food-consumed island)) (:food-stored island))
            :rawmat-stored (+ (rawmat-produced rawmat-workers island) (- (goods-produced goods-workers island)) (:rawmat-stored island))
            :goods-stored (+ (goods-produced goods-workers island) (- (goods-consumed island)) (:goods-stored island))
            :location (:location island)
            :food-production-factor (:food-production-factor island)
            :rawmat-production-factor (:rawmat-production-factor island)
            :goods-production-factor (:goods-production-factor island)
            :gene (:gene island)
            )))

(defn many-years [n [pct-food pct-raw] island]
    (if (> n 0)
        (many-years (dec n) [pct-food pct-raw] (next-year [pct-food pct-raw] island))
        island))
