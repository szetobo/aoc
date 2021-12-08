(ns abagile.aoc.2021.day08
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.set :as cset]
    [clojure.string :as cs]))

(def sample-input (util/read-input-split-lines "2021/day08.sample.txt"))

(def input (util/read-input-split-lines "2021/day08.txt"))

(defn char-set
  [s]
  (map #(set (cs/split % #"")) s))

(defn parse
  [s]
  (->> (map #(cs/split % #" \| ") s)
       (map (fn [x] (map #(-> % (cs/split #" ") char-set) x)))))

(defn make-digit-map
  [signals]
  (let [[d1 d7 d4 d8] (map (fn [s] (first (filter #(= s (count %)) signals))) [2 3 4 7])
        [s5 s6]       (map (fn [s] (filter #(= s (count %)) signals)) [5 6])
        d3 (first (filter #(cset/superset? % d1) s5))
        d9 (first (filter #(cset/superset? % d3) s6))
        d0 (first (filter #(and (not= % d9) (cset/superset? % d1)) s6))
        d6 (first (filter #(and (not= % d9) (not= % d0)) s6))
        d5 (first (filter #(and (not= % d3) (cset/superset? d6 %)) s5))
        d2 (first (filter #(and (not= % d3) (not= % d5)) s5))]
    (zipmap [d0 d1 d2 d3 d4 d5 d6 d7 d8 d9] (range 10))))

(comment
  (count sample-input)
  (take 10 sample-input)
  (count input)
  (take 10 input)
  (->> (parse sample-input)
       (mapcat second))
  (->> (parse sample-input)
       (map (fn [[x y]] [(make-digit-map x) y]))
       (map (fn [[x y]] (map #(get x %) y)))))

(defn part1
  []
  (time (->> (parse input) (mapcat second) (filter #(contains? #{2 3 4 7} (count %))) count)))

(defn part2
  []
  (time (->> (parse input)
             (map (fn [[s d]] [(make-digit-map s) d]))
             (map (fn [[s d]] (map #(get s %) d)))
             (map (fn [ds] (reduce #(+ (* %1 10) %2) 0 ds)))
             (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
