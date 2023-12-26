(ns abagile.aoc.2023.day21
  (:require
   [abagile.aoc.util :as util]
   [clojure.set :as set]
   [clojure.string :as cs]
   [clojure.test :refer [deftest is]]))

;; (def input (->> (util/read-input "2023/dayXX.txt"))
(def sample (->> (util/read-input-split-lines "2023/day21-sample.txt")))
(def input (->> (util/read-input-split-lines "2023/day21.txt")))

(defn parse
  [s]
  (with-meta (mapv vec s) {:rows (count s) :cols (count (first s))}))

(defn nbrs
  [grid pt]
  (let [{:keys [rows cols]} (meta grid)]
    (->> (map #(mapv + pt %) [[-1 0] [1 0] [0 -1] [0 1]])
         (remove (fn [[r c]] (or (neg? r) (>= r rows) (neg? c) (>= c cols) (#{\#} (get-in grid [r c]))))))))

(deftest test1
  (is (= 1 1)))

(defn part1
  []
  (time
   (let [grid (parse input)
         {:keys [rows cols]} (meta grid)
         S    (first (for [r (range rows) c (range cols) :when (#{\S} (get-in grid [r c]))] [r c]))]
    (count (loop [idx 64 pts #{S}]
             (if (zero? idx) pts
               (recur (dec idx) (->> (for [pt pts np (nbrs grid pt)] np)
                                     (into #{})))))))))


(deftest test2
  (is (= 1 1)))

(defn part2
  []
  (time
   (let [grid (parse input)
         {:keys [rows cols]} (meta grid)
         S    (first (for [r (range rows) c (range cols) :when (#{\S} (get-in grid [r c]))] [r c]))]
    (count (loop [idx 65 pts #{S}]
             (if (zero? idx) pts
               (recur (dec idx) (->> (for [pt pts np (nbrs grid pt)] np)
                                     (into #{})))))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
