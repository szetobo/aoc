(ns abagile.aoc.2021.day13
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]))

(def sample (util/read-input "2021/day13.sample.txt"))
(def input  (util/read-input "2021/day13.txt"))

(defn parse
  [s]
  [(->> s (re-seq #"(\d+),(\d+)") (map rest) (map #(mapv util/parse-int %)) (reduce #(assoc %1 %2 1) {}))
   (->> s (re-seq #"fold along ([xy])=(\d+)") (map rest) (map (fn [[xy n]] [xy (util/parse-int n)])))])

(defn fold
  [grid [xy v]]
  (if (= "y" xy)
    (reduce-kv (fn [m [x y] _] (cond-> m
                                 (= y v) (dissoc [x y])
                                 (> y v) (-> (dissoc [x y])
                                             (assoc [x (- (* 2 v) y)] 1))))
      grid grid)
    (reduce-kv (fn [m [x y] _] (cond-> m
                                 (= x v) (dissoc [x y])
                                 (> x v) (-> (dissoc [x y])
                                             (assoc [(- (* 2 v) x) y] 1))))
      grid grid)))

(defn draw
  [grid]
  (let [max-x (->> grid keys (map first) (apply max))
        max-y (->> grid keys (map second) (apply max))]
    (->> (for [y (range (inc max-y))
               x (range (inc max-x))
               :let [v (if (grid [x y]) "#" " ")]]
           v)
         (partition (inc max-x))
         (map #(apply str %)))))

(comment
  (count sample)
  (->> (parse sample)
       (apply reduce #(fold %1 %2))
       (into (sorted-map)))
  (into (sorted-map) (fold (first (parse sample)) ["y" 7]))
  (count input))

(defn part1
  []
  (time (let [[grid actions] (parse input)]
          (count (fold grid (first actions))))))

(defn part2
  []
  (time (->> (parse input)
             (apply reduce #(fold %1 %2))
             draw
             (cs/join "\n")
             println)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
