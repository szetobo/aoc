(ns abagile.aoc.2023.day10
  (:require
   [abagile.aoc.algo :as algo]
   [abagile.aoc.grid :as grid]
   [abagile.aoc.util :as util]
   [clojure.set :as set]))

(def input (->> (util/read-input "2023/day10.txt")))

(defn parse
  [s]
  (let [cols (->> s (re-find #".+\n") count dec)
        elms (->> s (re-seq #".") (map first))
        grid (into (sorted-map) (map-indexed #(vector [(quot %1 cols) (rem %1 cols)] %2) elms))]
     (with-meta grid {:dim [(quot (count elms) cols) cols]})))

(def offsets {:north [-1 0] :east [0 -1] :south [1 0] :west [0 1]})

(defn cal-nbr-cost
  [grid]
  (let [adjacent-fn (fn [os pt] (grid/adjacent (map offsets os) (grid/bounded grid) pt))]
    (fn [[_row _col :as pt]]
      (->> (case (grid pt)
             \. {}
             \| (adjacent-fn [:north :south] pt)
             \- (adjacent-fn [:east :west] pt)
             \L (adjacent-fn [:north :west] pt)
             \J (adjacent-fn [:north :east] pt)
             \7 (adjacent-fn [:south :east] pt)
             \F (adjacent-fn [:south :west] pt)
             \S (concat
                 (filter #(#{\| \7 \F} (grid %)) (adjacent-fn [:north] pt))
                 (filter #(#{\- \L \F} (grid %)) (adjacent-fn [:east] pt))
                 (filter #(#{\| \J \L} (grid %)) (adjacent-fn [:south] pt))
                 (filter #(#{\- \J \7} (grid %)) (adjacent-fn [:west] pt))))
           (reduce #(assoc %1 %2 1) {})))))

(defn part1
  []
  (time (let [grid (parse input)
              S    (ffirst (filter #(= (last %) \S) grid))]
          (-> (algo/dijkstra S (cal-nbr-cost grid))
              count (/ 2)))))


(defn part2
  []
  (time (let [grid        (parse input)
              [rows cols] (-> grid meta :dim)
              S           (ffirst (filter #(= (last %) \S) grid))
              all-pts     (into #{} (keys grid))
              borders     (into #{} (keys (algo/dijkstra S (cal-nbr-cost grid))))]
          (->> (for [[row col :as pt] (set/difference all-pts borders)]
                     ;; :let [n-pts (->> (for [row' (range 0 row)] [row' col]) (into #{}))
                     ;;       e-pts (->> (for [col' (range 0 col)] [row col']) (into #{}))
                     ;;       s-pts (->> (for [row' (range (inc row) rows)] [row' col]) (into #{}))
                     ;;       w-pts (->> (for [col' (range (inc col) cols)] [row col']) (into #{}))]
                     ;; :when (not (every? even? (map #(count (set/intersection borders %)) [n-pts s-pts e-pts w-pts])))]
                 pt)
               count))))


(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
