(ns abagile.aoc.2021.day10
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def sample-input (util/read-input-split-lines "2021/day10.sample.txt"))

(def input (util/read-input-split-lines "2021/day10.txt"))

(defn check-syntax
  [line]
  (let [closing {\[ \] \{ \} \( \) \< \>} opening? (->> closing keys set)]
    (reduce (fn [stack ch]
              (cond
                (opening? ch)       (conj stack (closing ch))
                (= ch (peek stack)) (pop stack)
                :else               (reduced {:corrupted ch})))
      '()
      line)))

(def scores1 {\) 3 \] 57 \} 1197 \> 25137})

(def scores2 {\) 1 \] 2 \} 3 \> 4})

(def median #(nth (sort %) (quot (count %) 2)))

(defn part1*
  [line]
  (->> line (map check-syntax) (keep :corrupted)
       (map scores1) (reduce +)))

(defn part2*
  [line]
  (->> line (map check-syntax) (remove :corrupted)
       (map #(map scores2 %)) (map #(reduce (fn [m n] (+ (* m 5) n)) %)) median))

(comment
  (count sample-input)
  (take 10 sample-input)
  (part1* sample-input)
  (part2* sample-input)
  (count input)
  (take 10 input))

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
