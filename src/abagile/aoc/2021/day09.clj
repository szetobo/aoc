(ns abagile.aoc.2021.day09
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]))

(def sample-input (util/read-input "2021/day09.sample.txt"))

(def input (util/read-input "2021/day09.txt"))

(defn parse
  [xs s]
  (->> s (re-seq #"\d") (map read-string)
       (map-indexed #(vector [(int (/ %1 xs)) (mod %1 xs)] %2))
       (into (sorted-map))))

(defn adjacent-points
  [[xs ys] [x y]]
  (for [[dx dy] [[-1 0] [0 1] [1 0] [0 -1]]
        :let [x' (+ x dx) y' (+ y dy)]
        :when (and (> xs x' -1) (> ys y' -1))]
    [x' y']))

(defn low-points
  [[xs ys] m]
  (remove nil? (for [x (range xs) y (range ys)
                     :let [v   (m [x y])
                           apt (adjacent-points [xs ys] [x y])]]
                 (when (= (count apt) (count (filter #(> (m %) v) apt))) [[x y] v]))))

(defn basins
  [[xs ys] floor [x y]]
  (loop [res #{} pts #{[x y]}]
    (let [pt  (first pts)
          apt (->> (adjacent-points [xs ys] pt) (remove #(or (contains? res %) (>= (floor %) 9))))
          pts (apply conj (disj pts pt) apt)
          res (conj res pt)]
      (if (empty? pts) res (recur res pts)))))

(comment
  (parse 5 sample-input))

(defn part1
  []
  (time
    (let [[xs _ :as gs] [100 100]]
      (->> input (parse xs) (low-points gs) (map (comp inc last)) (reduce +)))))

(defn part2
  []
  (time
    (let [[xs _ :as gs]  [100 100]
          floor (parse xs input)]
      (->> (map #(->> % first (basins gs floor) count) (low-points gs floor))
           (sort >) (take 3) (reduce *)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
