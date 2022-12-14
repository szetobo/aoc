(ns abagile.aoc.2022.day13
  (:require
    [clojure.string :as cs]
    [clojure.test :refer [deftest is testing]]))

(def input (->> "resources/2022/day13.txt" slurp cs/split-lines (remove #{""})))

(defn parse [s] (map read-string s))

(defn cmp
  [x y]
  (loop [[x & xs] x [y & ys] y]
    (cond
      (every? nil? [x y])      0
      (and (some? x) (nil? y)) 1
      (and (nil? x) (some? y)) -1
      (every? integer? [x y])  (let [r (compare x y)] (if (zero? r) (recur xs ys) r))
      (every? vector? [x y])   (let [r (cmp x y)]     (if (zero? r) (recur xs ys) r))
      (integer? x)             (let [r (cmp [x] y)]   (if (zero? r) (recur xs ys) r))
      (integer? y)             (let [r (cmp x [y])]   (if (zero? r) (recur xs ys) r)))))

(defn part1
  []
  (->> input parse (partition 2)
       (keep-indexed #(when-not (= (apply cmp %2) 1) (inc %1)))
       (reduce +)))

(defn part2
  []
  (->> input parse (concat [[[2]] [[6]]]) (sort cmp)
       (keep-indexed #(when (#{[[2]] [[6]]} %2) (inc %1)))
       (reduce *)))

(defn -main [& _]
  (println "part 1:" (time (part1)))
  (println "part 2:" (time (part2))))

(comment
  (-main))

(compare 1 1)
(deftest day-13
  (testing "in-order?"
    (is (= -1 (cmp [1 1 3 1 1] [1 1 5 1 1])))
    (is (= -1 (cmp [[1] [2 3 4]] [[1] 4])))
    (is (= 1  (cmp [9] [[8 7 6]])))
    (is (= -1 (cmp [[4 4] 4 4] [[4 4] 4 4 4])))
    (is (= 1  (cmp [7 7 7 7] [7 7 7])))
    (is (= -1 (cmp [] [3])))
    (is (= 1  (cmp [[[]]] [[]])))
    (is (= 1  (cmp [1 [2 [3 [4 [5 6 7]]]] 8 9] [1 [2 [3 [4 [5 6 0]]]] 8 9])))))
