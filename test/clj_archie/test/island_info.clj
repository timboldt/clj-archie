(ns clj-archie.test.island-info
  (:use [clj-archie.island-info])
  (:use [clojure.test]))

(def my-island-1 {
    :population 1000
    :food-stored 10
    :rawmat-stored 20
    :goods-stored 30
    :location [30 40]
    :food-production-factor 200
    :rawmat-production-factor 500
    :goods-production-factor 700 })

