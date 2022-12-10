(ns abagile.aoc.2022.day10
  (:require
   [clojure.string :as cs]))

(def input
  (->> "resources/2022/day10.txt" slurp cs/split-lines
    (map #(mapcat rest (re-seq #"(addx|noop)(?: (-?\d+))?$" %)))
    (map (fn [[d n]] [(keyword d) (when n (read-string n))]))))

(defn part1
  []
  (let [xs (->> input
                (mapcat (fn [[op param]] (case op :addx [0 param] :noop [0])))
                (reduce #(conj %1 (+ (peek %1) %2)) [1]))]
    (->> (map #(* % (nth xs (dec %))) (range 20 221 40))
         (reduce +))))

(defn part2
  []
  (let [xs (->> input
                (mapcat (fn [[op param]] (case op :addx [0 param] :noop [0])))
                (reduce #(conj %1 (+ (peek %1) %2)) [1]))]
    (->> (map #(if (<= (dec %2) (mod %1 40) (inc %2)) "#" ".") (range) xs)
         (partition 40)
         (map #(apply str %)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
