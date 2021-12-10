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
    (if (nil? h)
      stack
      (if (contains? pair-starts h)
        (recur (conj stack h) t)
        (let [[sh & st] stack]
          (if (not= (pairs sh) h)
            h
            (recur st t)))))))

(def scores1 {")" 3 "]" 57 "}" 1197 ">" 25137})

(def scores2 {")" 1 "]" 2 "}" 3 ">" 4})

(def mid-val #(nth % (-> (count %) inc (/ 2) dec)))

(comment
  (count sample-input)
  (parse sample-input)
  (->> (parse sample-input) (map check-syntax) (remove list?) (map scores1) (reduce +))
  (->> (parse sample-input) (map check-syntax) (filter list?)
       (map #(->> (map pairs %) (map scores2)))
       (map #(reduce (fn [m n] (+ (* m 5) n)) %))
       (sort)
       mid-val)
  (count input)
  (count (parse input))
  (take 10 (parse input)))

(defn part1
  []
  (time (->> (parse input) (map check-syntax) (remove list?) (map scores1) (reduce +))))

(defn part2
  []
  (time (->> (parse input) (map check-syntax) (filter list?)
             (map #(->> (map pairs %) (map scores2)))
             (map #(reduce (fn [m n] (+ (* m 5) n)) %))
             (sort)
             mid-val)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
