(ns clj-archie.island
      (:use [clj-archie.util]))

(comment "island structure"
{   :population                 ;population
    :food-stored                ;food in storage
    :rawmat-stored              ;raw materials in storage
    :goods-stored               ;goods in storage
    :location                   ;vector containing location of island [x y]
    :food-production-factor     ;# workers at which food production is 1:1
    :rawmat-production-factor   ;# workers at which raw resource production is 1:1
    :goods-production-factor    ;# workers at which manufactured goods production is 1:1)
})

(defn- random-plus-minus [val plus-minus-pct]
    (long (Math/round (* val (+ (- 1.0 plus-minus-pct) (rand (* 2.0 plus-minus-pct)))))))

(defn random-island [map-width map-height]
    (let [
        pop (+ 50 (rand-int 500))
        ninety-percent (/ (* 10 pop) 9)
        twenty-percent (/ pop 5)]
        (hash-map
            :population pop
            :food-stored (random-plus-minus pop 0.1)
            :rawmat-stored (random-plus-minus pop 0.1)
            :goods-stored (random-plus-minus pop 0.1)
            :location [(rand map-width) (rand map-height)]
            :food-production-factor (random-plus-minus pop 0.3)
            :rawmat-production-factor (random-plus-minus pop 0.5)
            :goods-production-factor (random-plus-minus pop 0.5)
            )))

(defn str-island [{population :population location :location}]
    (str "population=" (pad-num population 8) " location=" location))

(comment "
--
-- Intermediate results
--
")

(defn food-produced [workers {factor :food-production-factor population :population}]
    (diminished-return (min workers population) factor))

(defn rawmat-produced [workers {factor :rawmat-production-factor population :population}]
    (diminished-return (min workers population) factor))

(defn goods-produced [workers {factor :goods-production-factor population :population rawmat :rawmat-stored}]
    (min rawmat (diminished-return (min workers population) factor)))

(defn food-consumed [{food :food-stored population :population}]
    (min food population))

(defn goods-consumed [{goods :goods-stored population :population}]
    (min goods population))

(comment "
prop_foodProduced workers pop =
        (foodProduced workers i1) >= 0 &&
        (foodProduced workers i1) <= (diminishedReturn (population i1) (foodProductionFactor i1))
        where
                i1 = (generateTestIslandWithResources pop 0 0 0)
        
prop_foodConsumed pop food =
        pop > 0 ==>
        (foodConsumed i1) >= 0 &&
        (foodConsumed i1) <= (population i1)
        where
                i1 = (generateTestIslandWithResources pop food 0 0)
")


(comment "
--
-- Year iteration
--
")

(defn next-year [[pct-food pct-raw] island]
    (let [
        food-workers (* pct-food (:population island))
        rawmat-workers (* pct-raw (:population island))
        goods-workers (* (- 1.0 (+ pct-food pct-raw)) (:population island))]
        (hash-map
            :population (+ 10 (:population island)) ;>>>>> +/- famine, births, deaths, immigration
            ;>>>>> +/- trade of food/rawmat/goods
            :food-stored (+ (food-produced food-workers island) (- (food-consumed island)) (:food-stored island))
            :rawmat-stored (+ (rawmat-produced rawmat-workers island) (- (goods-produced goods-workers island)) (:rawmat-stored island))
            :goods-stored (+ (goods-produced goods-workers island) (- (goods-consumed island)) (:goods-stored island))
            :location (:location island)
            :food-production-factor (:food-production-factor island)
            :rawmat-production-factor (:rawmat-production-factor island)
            :goods-production-factor (:goods-production-factor island))))

(defn many-years [n [pct-food pct-raw] island]
    (if (> n 0)
        (many-years (dec n) [pct-food pct-raw] (next-year [pct-food pct-raw] island))
        island))

(comment "
(many-years 0 [0.3 0.3] my-island)
(many-years 1 [0.3 0.3] my-island)
(many-years 2 [0.3 0.3] my-island)
(many-years 100 [0.3 0.3] my-island)

test_nextYear_immutables =
        (fst (location i1)) @?= (fst (location i2))
        where
                i1 = (generateTestIslandAtLocation 1 2)
                i2 = (nextYear i1 (50,25,25)))
")