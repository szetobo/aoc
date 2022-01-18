(ns abagile.aoc.2020.day9
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split-lines "2020/day9.txt") (map read-string)))

(def sample [35 20 15 25 47 40 62 55 65 95 102 117 150 182 127 219 299 277 309 576])

(defn weakness1 [p-count input]
  (loop [lst input]
    (let [[preamble [n & _]] (split-at p-count lst)]
      (cond
        (nil? n)
        :not-found

        (every? nil? (for [x preamble
                           y preamble
                           :when (and (not= x y) (= n (+ x y)))]
                       [x y]))
        n

        :else
        (recur (subvec lst 1))))))

(comment
  (weakness1 5 sample))

(defn weakness2 [attack input]
  (loop [lst input]
    (cond
      (< (count lst) 2)
      :not-found

      :else
      (if-let [res (first (for [n (range 2 (inc (count lst)))
                                :let [res (take n lst)]
                                :when (= attack (reduce + res))]
                            res))]
        res
        (recur (subvec lst 1))))))

(comment
  (->> (weakness2 (weakness1 5 sample) sample)
       ((juxt #(apply max %) #(apply min %)))
       (apply +)))


(defn part1 []
  (time (weakness1 25 input)))

(defn part2 []
  (->> (weakness2 (weakness1 25 input) input)
       (#(+ (apply max %) (apply min %)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
