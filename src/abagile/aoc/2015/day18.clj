(ns abagile.aoc.2015.day18
  (:gen-class)
  (:require
    [abagile.aoc.ocr :as ocr]
    [abagile.aoc.grid :as grid]
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def sample (util/read-input "2015/day18.sample.txt"))
(def input  (util/read-input "2015/day18.txt"))

(defn play
  [nbrs-fn dim on-set grid]
  (let [[rows cols] dim]
    (->> (for [row (range rows) col (range cols)
               :let [v       (grid [row col])
                     nbrs    (nbrs-fn [row col])
                     nbrs-on (reduce + (map grid nbrs))]]
           [[row col] (cond
                        (contains? on-set [row col])    1
                        (and (zero? v) (= nbrs-on 3))   1
                        (and (pos? v) (<= 2 nbrs-on 3)) 1
                        :else 0)])
         (into {}))))

(comment
  (let [grid    (ocr/parse sample)
        nbrs-fn #(grid/adjacent-8 (grid/bounded grid) %)]
    (->> (iterate (partial play nbrs-fn (-> grid meta :dim) #{}) grid) (drop 1)
         first
         (into (sorted-map)) grid/->prn (run! prn)))

  (let [grid    (ocr/parse sample)
        dim     (-> grid meta :dim)
        nbrs-fn #(grid/adjacent-8 (grid/bounded grid) %)
        [r c]   (map dec dim)
        grid    (-> grid (assoc [0 0] 1 [0 c] 1 [r 0] 1 [r c] 1))]
    (->> (iterate (partial play nbrs-fn dim #{[0 0] [0 c] [r 0] [r c]}) grid) (drop 1)
         first
         (into (sorted-map)) grid/->prn (run! prn))))

(defn part1 []
  (time
    (let [grid    (ocr/parse input)
          nbrs-fn #(grid/adjacent-8 (grid/bounded grid) %)]
      (->> (iterate (partial play nbrs-fn (-> grid meta :dim) #{}) grid) (drop 100)
           first
           (filter (fn [[_ v]] (pos? v))) count))))

(defn part2 []
  (time
    (let [grid    (ocr/parse input)
          dim     (-> grid meta :dim)
          nbrs-fn #(grid/adjacent-8 (grid/bounded grid) %)
          [r c]   (map dec dim)
          on-set  #{[0 0] [0 c] [r 0] [r c]}
          grid    (reduce #(assoc %1 %2 1) grid on-set)]
      (->> (iterate (partial play nbrs-fn dim on-set) grid) (drop 100)
           first
           (filter (fn [[_ v]] (pos? v))) count))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest test-sample
  (is (= 0 0)))
