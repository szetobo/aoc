(ns abagile.aoc.2015.day18
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def input (->> (util/read-input-split-lines "2015/day18.txt")))

(def sample [".#.#.#"
             "...##."
             "#....#"
             "..#..."
             "#.#..#"
             "####.."])

(defn build-map [coll]
  (mapv #(vec (for [cell (seq %)] (case cell \# 9 \. 0))) coll))

(defn adjust-grid [grid]
  (let [xs (-> grid count)
        ys (-> grid first count)]
    (-> grid
        (assoc-in [0 0] 9)
        (assoc-in [0 (dec ys)] 9)
        (assoc-in [(dec xs) 0] 9)
        (assoc-in [(dec xs) (dec ys)] 9))))

(defn get-cell [grid x y] ((grid x) y))

(defn adjacent-cells [x y]
  (for [x' [(dec x) x (inc x)]
        y' [(dec y) y (inc y)]
        :when (or (not= x' x) (not= y' y))]
    [x' y']))

(defn sum-of-adjacent-cells [grid x y]
  (reduce + (for [[x' y'] (adjacent-cells x y)
                  :when (and (>= x' 0) (>= y' 0)
                             (< x' (-> grid count)) (< y' (-> grid first count)))]
              (get-cell grid x' y'))))

(defn step1 [grid]
  (let [xs (-> grid count)
        ys (-> grid first count)]
    (->> (for [x (range xs)
               y (range ys)
               :let [on (= 9 (get-cell grid x y))
                     sum (sum-of-adjacent-cells grid x y)]]
           (if on
             (if (<= 18 sum 27) 9 0)
             (if (= sum 27) 9 0)))
         (partition ys)
         (map vec)
         vec)))

(defn run [step grid n]
  (first (drop n (iterate step grid))))

(defn step2 [grid]
  (let [xs (-> grid count)
        ys (-> grid first count)]
    (->> (for [x (range xs)
               y (range ys)
               :let [on (or (= 9 (get-cell grid x y)))
                     sum (sum-of-adjacent-cells grid x y)]]
           (if (or (and (= x 0) (= y 0))
                   (and (= x 0) (= y (dec ys)))
                   (and (= x (dec xs)) (= y 0))
                   (and (= x (dec xs)) (= y (dec ys))))
             9
             (if (or on)
               (if (<= 18 sum 27) 9 0)
               (if (= sum 27) 9 0))))
         (partition ys)
         (map vec)
         vec)))

(defn part1 []
  (time (->> (run step1 (build-map input) 100)
            flatten
            (map #(= % 9))
            (filter true?)
            count)))

(defn part2 []
  (time (->> (run step2 (adjust-grid (build-map input)) 100)
            flatten
            (map #(= % 9))
            (filter true?)
            count)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-sample
  (let [grid (build-map sample)]
    (is (= grid [[0 9 0 9 0 9]
                 [0 0 0 9 9 0]
                 [9 0 0 0 0 9]
                 [0 0 9 0 0 0]
                 [9 0 9 0 0 9]
                 [9 9 9 9 0 0]]))
    (is (= (get-cell grid 0 1) 9))
    (is (= (sum-of-adjacent-cells grid 4 2) 36))
    (is (= (step1 grid) [[0 0 9 9 0 0]
                         [0 0 9 9 0 9]
                         [0 0 0 9 9 0]
                         [0 0 0 0 0 0]
                         [9 0 0 0 0 0]
                         [9 0 9 9 0 0]]))
    (is (= (run step1 grid 4) [[0 0 0 0 0 0]
                               [0 0 0 0 0 0]
                               [0 0 9 9 0 0]
                               [0 0 9 9 0 0]
                               [0 0 0 0 0 0]
                               [0 0 0 0 0 0]]))
    (is (= (adjust-grid grid) [[9 9 0 9 0 9]
                               [0 0 0 9 9 0]
                               [9 0 0 0 0 9]
                               [0 0 9 0 0 0]
                               [9 0 9 0 0 9]
                               [9 9 9 9 0 9]]))
    (is (= (step2 (adjust-grid grid)) [[9 0 9 9 0 9]
                                       [9 9 9 9 0 9]
                                       [0 0 0 9 9 0]
                                       [0 0 0 0 0 0]
                                       [9 0 0 0 9 0]
                                       [9 0 9 9 9 9]]))))
