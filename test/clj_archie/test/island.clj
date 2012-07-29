(ns clj-archie.test.island
  (:use [clj-archie.island])
  (:use [clojure.test]))

(def islands (take 100 (random-island-seq 100 100 "WXYZABCDMNOP")))

(deftest test-random-island
    (testing "Random island"
        (is true)
        ))

(deftest test-random-island-seq
    (testing "Random island sequence"
        (is true)
        ))

(deftest test-str-island
    (testing "Island pretty printing"
        (is true)
        ))

(deftest test-food-produced
    (testing "Food produced"
        (is (thrown? AssertionError (food-produced -1 (first islands))) "Cannot supply negative workers")
        (is (== 0 (food-produced 0 (first islands))))
        (is (every?
            #(<
                (food-produced (:population %) %)
                (* 3 (:population %)))
            islands))
        (is (every?
            #(>=
                (food-produced (:population %) %)
                0)
            islands))
        ))

(deftest test-rawmat-produced
    (testing "xxx"
        (is true)
        ))

(deftest test-goods-produced
    (testing "xxx"
        (is true)
        ))

(deftest test-food-consumed
    (testing "xxx"
        (is true)
        ))

(deftest test-goods-consumed
    (testing "xxx"
        (is true)
        ))

(deftest test-calc-population
    (testing "xxx"
        (is true)
        ))

(deftest test-calc-happiness
    (testing "Calc happiness"
        (is (= 0 (calc-happiness {:happiness 0 :goods-stored 50 :food-stored 50 :population 100})))
        ))

