(ns abagile.aoc.2021.day15
  (:gen-class)
  (:require
    [abagile.aoc.grid :as grid]
    [abagile.aoc.util :as util]))

(def sample (util/read-input "2021/day15.sample.txt"))
(def input  (util/read-input "2021/day15.txt"))

(defn adjacents
  [cave pos]
  (->> (grid/adjacent-4 (-> cave meta :dim) pos)
       (reduce #(assoc %1 %2 (cave %2)) {})))

(defn expand-cave
  [cave n]
  (let [[xs ys] (-> cave meta :dim)
        wrap #(inc (mod (dec %) 9))]
    (-> (into {} (for [x (range (* n xs)) y (range (* n ys))]
                   [[x y] (wrap (+ (cave [(mod x xs) (mod y ys)]) (quot x xs) (quot y ys)))]))
        (with-meta {:dim [(* n xs) (* n ys)]}))))

(comment
  (count sample)
  (-> (grid/parse sample) (adjacents [1 1]))
  (time (-> (grid/dijkstra [0 0] (partial adjacents (grid/parse sample))) (get [9 9])))
  (count input)
  (meta (grid/parse input)))

(defn part1
  []
  (time (let [cave (grid/parse input) dim (-> cave meta :dim) adj-risks (partial adjacents cave)]
          (-> (grid/dijkstra [0 0] adj-risks) (get (map dec dim))))))

(defn part2
  []
  (time (let [cave (-> (grid/parse input) (expand-cave 5)) dim (-> cave meta :dim) adj-risks (partial adjacents cave)]
          (-> (grid/dijkstra [0 0] adj-risks) (get (map dec dim))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
