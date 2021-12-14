(ns abagile.aoc.2021.day14
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]))

(def sample (util/read-input "2021/day14.sample.txt"))
(def input  (util/read-input "2021/day14.txt"))

(defn parse
  [s]
  [(->> (cs/split s #"\n\n") first)
   (->> s (re-seq #"([A-Z]{2}) -> ([A-Z])") (map rest) (reduce (fn [m [k v]] (assoc m (seq k) (first v))) {}))])

(defn step-fn
  [rules]
  (fn [freqs]
    (reduce-kv (fn [m [x y :as k] v]
                 (let [z (rules k)]
                   (merge-with + m {(list x z) v} {(list z y) v})))
      {} freqs)))

(def max-diff #(- (apply max %) (apply min %)))

(comment
  (count sample)
  (parse sample)
  (count input)
  (parse input))

(defn part1
  []
  (time
    (let [[template rules] (parse input)
          freqs (->> template (partition 2 1) frequencies)
          last-char (last template)]
       (->> (iterate (step-fn rules) freqs) (drop 10) first
            (reduce-kv (fn [m [x _] v] (merge-with + m {x v})) {last-char 1})
            vals max-diff))))

(defn part2
  []
  (time
    (let [[template rules] (parse input)
          freqs (->> template (partition 2 1) frequencies)
          last-char (last template)]
       (->> (iterate (step-fn rules) freqs) (drop 40) first
            (reduce-kv (fn [m [x _] v] (merge-with + m {x v})) {last-char 1})
            vals max-diff))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
