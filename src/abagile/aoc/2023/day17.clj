(ns abagile.aoc.2023.day17
  (:require
   [abagile.aoc.algo :as algo]
   [abagile.aoc.grid :as grid]
   [abagile.aoc.util :as util]))

(def sample (->> (util/read-input "2023/day17-sample.txt")))
(def input (->> (util/read-input "2023/day17.txt")))

(defn parse
  [s]
  (let [cols (->> s (re-find #".+\n") count dec)
        elms (->> s (re-seq #".") (map read-string))
        grid (into (sorted-map) (map-indexed #(vector [(quot %1 cols) (rem %1 cols)] %2) elms))]
     (with-meta grid {:dim [(quot (count elms) cols) cols]})))

(defn cal-nbr-cost
  [grid]
  (let [adjacent-fn (fn [os pt dir cnt]
                      (->> (grid/adjacent (map grid/offsets os) (grid/bounded grid) pt)
                           (map #(conj % dir cnt))))]
    (fn [[row col dir cnt]]
      (let [pt [row col]]
        (->> (concat (when (and (not= dir :down)  (or (not= dir :up)    (< cnt 3))) (adjacent-fn [:north] pt :up    (if (= dir :up)    (inc cnt) 1)))
                     (when (and (not= dir :up)    (or (not= dir :down)  (< cnt 3))) (adjacent-fn [:south] pt :down  (if (= dir :down)  (inc cnt) 1)))
                     (when (and (not= dir :left)  (or (not= dir :right) (< cnt 3))) (adjacent-fn [:east]  pt :right (if (= dir :right) (inc cnt) 1)))
                     (when (and (not= dir :right) (or (not= dir :left)  (< cnt 3))) (adjacent-fn [:west]  pt :left  (if (= dir :left)  (inc cnt) 1))))
             (reduce #(assoc %1 %2 (grid (subvec %2 0 2))) {}))))))

(defn part1
  []
  (time
    (let [grid (parse input)
          [rows cols] (-> grid meta :dim)
          nbr-cost-fn (cal-nbr-cost grid)]
      (->> (algo/dijkstra [0 0 nil 0] nbr-cost-fn)
           (filter #(-> % first (subvec 0 2) (= [(dec rows) (dec cols)])))
           (map second) (apply min)))))


(defn cal-nbr-cost2
  [grid]
  (let [adjacent-fn (fn [os pt dir cnt]
                      (->> (grid/adjacent (map grid/offsets os) (grid/bounded grid) pt)
                           (map #(conj % dir cnt))))]
    (fn [[row col dir cnt]]
      (let [pt [row col]]
        (->> (concat (when (and (not= dir :down)  (if (not= dir :up)    (>= cnt 4) (< cnt 10))) (adjacent-fn [:north] pt :up    (if (= dir :up)    (inc cnt) 1)))
                     (when (and (not= dir :up)    (if (not= dir :down)  (>= cnt 4) (< cnt 10))) (adjacent-fn [:south] pt :down  (if (= dir :down)  (inc cnt) 1)))
                     (when (and (not= dir :left)  (if (not= dir :right) (>= cnt 4) (< cnt 10))) (adjacent-fn [:east]  pt :right (if (= dir :right) (inc cnt) 1)))
                     (when (and (not= dir :right) (if (not= dir :left)  (>= cnt 4) (< cnt 10))) (adjacent-fn [:west]  pt :left  (if (= dir :left)  (inc cnt) 1))))
             (reduce #(assoc %1 %2 (grid (subvec %2 0 2))) {}))))))

(defn part2
  []
  (time
    (let [grid (parse input)
          [rows cols] (-> grid meta :dim)
          nbr-cost-fn (cal-nbr-cost2 grid)]
      (->> (algo/dijkstra [0 0 nil 4] nbr-cost-fn)
           (filter #(-> (first %) (subvec 0 2) (= [(dec rows) (dec cols)])))
           (filter #(-> (first %) last (>= 4)))
           (map second) (apply min)))))


(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
