(ns abagile.aoc.2021.day09
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]))

(def sample-input (util/read-input "2021/day09.sample.txt"))

(def input (util/read-input "2021/day09.txt"))

(defn cell-val [m [x y]] ((m x) y))

(defn adjacent-cells
  [[xs ys] [x y]]
  (for [x' [(dec x) x (inc x)]
        y' [(dec y) y (inc y)]
        :when (and (> xs x' -1) (> ys y' -1)
                   (or (= x' x) (= y' y))
                   (not= [x y] [x' y']))]
    [x' y']))

(defn low-points
  [[xs ys] m]
  (->> (for [x (range xs) y (range ys)
             :let [v  (cell-val m [x y])
                   ac (adjacent-cells [xs ys] [x y])]]
         (when (= (count ac) (->> (filter #(> (cell-val m %) v) ac) count)) [[x y] v]))
       (remove nil?)))

(defn basins
  [[xs ys] m [x y]]
  (loop [rs #{} pts #{[x y]}]
    (let [pt  (first pts)
          adj (->> (adjacent-cells [xs ys] pt)
                   (remove #(contains? rs %))
                   (filter #(< (cell-val m %) 9)))
          pts (apply conj (disj pts pt) adj)
          rs  (cond-> rs (< (cell-val m pt) 9) (conj pt))]
      (if (seq pts)
        (recur rs pts)
        rs))))

(defn part1
  []
  (time
    (let [gs [100 100]
          m  (->> input (re-seq #"\d") (map read-string) (partition (first gs)) (mapv vec))]
      (->> m (low-points gs) (map (comp inc last)) (reduce +)))))

(defn part2
  []
  (time
    (let [gs  [100 100]
          m   (->> input (re-seq #"\d") (map read-string) (partition (first gs)) (mapv vec))
          lps (->> m (low-points gs))]
      (->> (map #(->> % first (basins gs m) count) lps)
           (sort >)
           (take 3)
           (reduce *)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
