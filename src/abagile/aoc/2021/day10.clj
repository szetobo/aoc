(ns abagile.aoc.2021.day10
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]))

(def sample-input (util/read-input "2021/day10.sample.txt"))

(def input (util/read-input "2021/day10.txt"))

(defn parse [s] (map #(cs/split % #"") (cs/split s #"\n")))

(def pairs  {"[" "]" "{" "}" "(" ")" "<" ">"})

(def pair-starts (->> pairs keys set))

(defn check-syntax
  [s]
  (loop [stack '() [h & t] s]
    (cond
      (nil? h)        stack
      (pair-starts h) (recur (conj stack h) t)
      :else           (let [[sh & st] stack]
                        (if (not= (pairs sh) h) h (recur st t))))))

(def scores1 {")" 3 "]" 57 "}" 1197 ">" 25137})

(def scores2 {")" 1 "]" 2 "}" 3 ">" 4})

(def mid-val #(nth % (-> (count %) inc (/ 2) dec)))

(defn part1*
  [puzzle]
  (->> puzzle parse (map check-syntax) (remove list?)
       (map scores1) (reduce +)))

(defn part2*
  [puzzle]
  (->> puzzle parse (map check-syntax) (filter list?)
       (map #(map (comp scores2 pairs) %))
       (map #(reduce (fn [m n] (+ (* m 5) n)) %))
       (sort)
       mid-val))

(comment
  (count sample-input)
  (parse sample-input)
  (part1* sample-input)
  (part2* sample-input)
  (count input)
  (count (parse input))
  (take 10 (parse input)))

(defn part1
  []
  (time (part1* input)))

(defn part2
  []
  (time (part2* input)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
