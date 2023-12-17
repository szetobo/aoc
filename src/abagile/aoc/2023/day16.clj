(ns abagile.aoc.2023.day16
  (:require
   [abagile.aoc.algo :as algo]
   [abagile.aoc.grid :as grid]
   [abagile.aoc.util :as util]))

(def input (->> (util/read-input "2023/day16.txt")))

(defn parse
  [s]
  (let [cols (->> s (re-find #".+\n") count dec)
        elms (->> s (re-seq #".") (map first))
        grid (into (sorted-map) (map-indexed #(vector [(quot %1 cols) (rem %1 cols)] %2) elms))]
     (with-meta grid {:dim [(quot (count elms) cols) cols]})))

(defn cal-nbr-cost
  [grid]
  (let [adjacent-fn (fn [os pt dir]
                      (->> (grid/adjacent (map grid/offsets os) (grid/bounded grid) pt)
                           (map #(conj % dir))))]
    (fn [[row col dir]]
      (let [pt [row col]]
        (->> (case (grid pt)
               \. (case dir
                    :north (adjacent-fn [:south] pt dir)
                    :south (adjacent-fn [:north] pt dir)
                    :east  (adjacent-fn [:west]  pt dir)
                    :west  (adjacent-fn [:east]  pt dir))
               \| (case dir
                    :north (adjacent-fn [:south] pt dir)
                    :south (adjacent-fn [:north] pt dir)
                    (concat (adjacent-fn [:north] pt :south) (adjacent-fn [:south] pt :north)))
               \- (case dir
                    :east  (adjacent-fn [:west] pt dir)
                    :west  (adjacent-fn [:east] pt dir)
                    (concat (adjacent-fn [:east] pt :west) (adjacent-fn [:west] pt :east)))
               \\ (case dir
                    :north (adjacent-fn [:east] pt :west)
                    :south (adjacent-fn [:west] pt :east)
                    :west  (adjacent-fn [:south] pt :north)
                    :east  (adjacent-fn [:north] pt :south))
               \/ (case dir
                    :north (adjacent-fn [:west] pt :east)
                    :south (adjacent-fn [:east] pt :west)
                    :west  (adjacent-fn [:north] pt :south)
                    :east  (adjacent-fn [:south] pt :north)))
             (reduce #(assoc %1 %2 1) {}))))))

(defn part1
  []
  (time (let [grid (parse input)
              nbr-cost-fn (cal-nbr-cost grid)]
          (->> (algo/dijkstra [0 0 :west] nbr-cost-fn)
               keys (map pop) (into #{}) count))))


(defn part2
  []
  (time (let [grid (parse input)
              [row col] (->> grid meta :dim)
              nbr-cost-fn (cal-nbr-cost grid)]
          (->> (concat
                (for [op [[0 :north] [(dec row) :south]]
                      c (range col)
                      :let [[r dir] op]]
                  (->> (algo/dijkstra [r c dir] nbr-cost-fn) keys (map pop) (into #{}) count))
                (for [op [[0 :west] [(dec col) :east]]
                      r (range row)
                      :let [[c dir] op]]
                  (->> (algo/dijkstra [r c dir] nbr-cost-fn) keys (map pop) (into #{}) count)))
               (apply max)))))


(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
