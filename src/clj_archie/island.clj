(ns clj-archie.island
      (:use [clj-archie.util]))

(comment "
island structure:
        --
        foodStored :: Integer,                  -- food in storage
        rawMaterialStored :: Integer,           -- raw materials in storage
        goodsStored :: Integer,                 -- goods in storage
        --
        -- Invariant properties
        --
        location :: (Integer, Integer),         -- location of island: (x,y)
        foodProductionFactor :: Integer,        -- # workers at which food production is 1:1
        rawResourceProductionFactor :: Integer, -- # workers at which raw resource production is 1:1
        goodsProductionFactor :: Integer        -- # workers at which manufactured goods production is 1:1)
")

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