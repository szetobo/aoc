(ns abagile.aoc.2020.day10
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split-lines "2020/day10.txt")
                (map util/parse-int)))

(def sample1 [16 10 15 5 1 11 7 19 6 12 4])
(def sample2 [28 33 18 42 31 14 46 20 48 47 24 23 49 45 19 38 39 11 1 32 25 35 8 17 7 9 4 2 34 10 3])

(comment
  (-> input
      (as-> $ {:min (apply min $)
               :max (apply max $)
               :count (count $)})
      (as-> $ (assoc $ :rating (+ 3 (:max $))))))

(def ways
  (memoize (fn [n coll]
            (let [cnt (count coll)]
              (if (= n (dec cnt))
                1
                (reduce + (for [j (range (inc n) (min cnt (+ n 4)))
                                :when (<= (- (coll j) (coll n)) 3)]
                            (ways j coll))))))))

(def sample3 (into [0] (concat (sort sample2) [(+ 3 (apply max sample2))])))

(comment
  (ways 0 sample1)
  (ways 0 sample3))

(defn part1 []
  (-> input
      (as-> $ (concat $ [0 (+ 3 (apply max $))]))
      sort
      vec
      (#(reduce (fn [{:keys [d1 d3]} [x y]]
                  {:d1 (if (= (- y x) 1) (inc d1) d1)
                   :d3 (if (= (- y x) 3) (inc d3) d3)})
                {:d1 0 :d3 0}
                (partition 2 1 %)))
      (#(reduce * (vals %)))))

(defn part2 []
  (-> input
      (as-> $ (concat $ [0 (+ 3 (apply max $))]))
      sort
      vec
      (#(ways 0 %))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
