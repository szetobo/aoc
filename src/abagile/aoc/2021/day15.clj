(ns abagile.aoc.2021.day15
  (:gen-class)
  (:require
    [abagile.aoc.grid :as grid]
    [abagile.aoc.util :as util]
    [clojure.data.priority-map :refer [priority-map]]))

(def sample (util/read-input "2021/day15.sample.txt"))
(def input  (util/read-input "2021/day15.txt"))

(defn neighbours
  [grid pos]
  (->> (grid/adjacent-4 (-> grid meta :dim) pos)
       (reduce #(assoc %1 %2 (grid %2)) {})))

(defn remove-keys
  [m pred]
  (select-keys m (filter (complement pred) (keys m))))

(defn dijkstra
  [grid start nbr-fn]
  (loop [que (priority-map start 0) visited {}]
    (if-let [[curr curr-cost] (peek que)]
      (let [costs (->> (remove-keys (nbr-fn grid curr) visited) (util/fmap (partial + curr-cost)))]
        (recur (merge-with min (pop que) costs) (assoc visited curr curr-cost)))
      visited)))

(defn expand-map
  [grid n]
  (let [[xs ys] (-> grid meta :dim)
        wrap #(inc (mod (dec %) 9))]
    (-> (into {} (for [x (range (* n xs)) y (range (* n ys))]
                   [[x y] (wrap (+ (grid [(mod x xs) (mod y ys)]) (quot x xs) (quot y ys)))]))
        (with-meta {:dim [(* n xs) (* n ys)]}))))

(comment
  (count sample)
  (-> (grid/parse sample) (neighbours [1 1]))
  (time (-> (grid/parse sample) (dijkstra [0 0] neighbours) (get [9 9])))
  (count input)
  (meta (grid/parse input)))

(defn part1
  []
  (time (let [grid (grid/parse input) dim (-> grid meta :dim)]
          (-> grid (dijkstra [0 0] neighbours) (get (map dec dim))))))

(defn part2
  []
  (time (let [grid (-> (grid/parse input) (expand-map 5)) dim (-> grid meta :dim)]
          (-> grid (dijkstra [0 0] neighbours) (get (map dec dim))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
