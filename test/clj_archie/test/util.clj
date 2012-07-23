(ns clj-archie.test.util
  (:use [clj-archie.util])
  (:use [clojure.test]))

(deftest test-diminished-return
    (testing "Diminished return"
        (testing "with invalid value"
            (is (= 0 (diminished-return 0 100)))
            (is (= 0 (diminished-return -1 100))))
        (testing "with invalid scale"
            (is (= 0 (diminished-return 100 0)))
            (is (= 0 (diminished-return 100 -1)))
            (is (= 0 (diminished-return -1 -1)))
            (is (= 0 (diminished-return 0 0))))
        (testing "with varying values against a known scale"
            (is (= 2 (diminished-return 1 12345)))
            (is (= 83 (diminished-return 42 12345)))
            (is (= 1750 (diminished-return 999 12345)))
            (is (= 123444 (diminished-return 678910 12345))))
        (testing "with equal values"
            (is (= 1 (diminished-return 1 1)))
            (is (= 42 (diminished-return 42 42)))
            (is (= 32768 (diminished-return 32768 32768)))
            (is (= Integer/MAX_VALUE (diminished-return Integer/MAX_VALUE Integer/MAX_VALUE))))))

(deftest test-distance
    (testing "Distance between points"
        (testing "with identical points"
            (is (== 0 (distance-from [0 0] [0 0])))
            (is (== 0 (distance-from [-99 -99] [-99 -99])))
            (is (== 0 (distance-from [99 -99] [99 -99])))
            (is (== 0 (distance-from [-99 99] [-99 99])))
            (is (== 0 (distance-from [1.2345 6.789] [1.2345 6.789]))))
        (testing "with horizontal points"
            (is (== 100 (distance-from [0 0] [100 0])))
            (is (== 198 (distance-from [-99 -99] [99 -99])))
            (is (== 2 (distance-from [-50 42] [-48 42])))
            (is (== 50 (distance-from [100 99] [50 99])))
            (is (== 1.25 (distance-from [1.25 6.789] [2.50 6.789]))))
        (testing "with vertical points"
            (is (== 100 (distance-from [0 50] [0 -50])))
            (is (== 100 (distance-from [-42 -50] [-42 50])))
            (is (== 1 (distance-from [42 -99] [42 -98])))
            (is (== 0.5 (distance-from [1.2345 6.0] [1.2345 6.5]))))
        (testing "with equalateral triangles"
            (is (== 5 (distance-from [0 0] [3.0 4.0])))
            (is (== (* 5.0 Integer/MAX_VALUE) (distance-from [0 0] [(* 3.0 Integer/MAX_VALUE) (* 4.0 Integer/MAX_VALUE)]))))))
