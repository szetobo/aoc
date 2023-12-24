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
  (time (let [grid          (parse input)
              [rows cols]   (-> grid meta :dim)
              adjacent-fn   (cal-nbr-cost grid)
              [sr sc :as S] (ffirst (filter #(= (last %) \S) grid))
              s-nbrs        (into #{} (map first (adjacent-fn S)))
              grid          (assoc grid S (cond
                                            (= #{[(dec sr) sc] [sr (inc sc)]} s-nbrs) \L
                                            (= #{[(dec sr) sc] [sr (dec sc)]} s-nbrs) \J
                                            (= #{[(dec sr) sc] [(inc sr) sc]} s-nbrs) \|
                                            (= #{[sr (inc sc)] [(inc sr) sc]} s-nbrs) \F
                                            (= #{[sr (inc sc)] [sr (dec sc)]} s-nbrs) \-
                                            (= #{[(inc sr) sc] [sr (dec sc)]} s-nbrs) \7))
              all-pts     (into #{} (keys grid))
              borders     (into #{} (keys (algo/dijkstra S adjacent-fn)))]
          (->> (for [[row col] (set/difference all-pts borders)
                     :let [n-pts (->> (for [row' (range 0 row)] [row' col]) (map borders) (remove nil?))
                           w-pts (->> (for [col' (range 0 col)] [row col']) (map borders) (remove nil?))
                           s-pts (->> (for [row' (range (inc row) rows)] [row' col]) (map borders) (remove nil?))
                           e-pts (->> (for [col' (range (inc col) cols)] [row col']) (map borders) (remove nil?))]]
                 [(let [syms (->> n-pts (map grid) (remove #{\|}))]
                    (+ (->> syms (remove #{\-}) (partition 2) (map #(into #{} %)) (filter #{#{\F \J} #{\7 \L}}) count)
                       (->> syms (filter #{\-}) count)))
                  (let [syms (->> w-pts (map grid) (remove #{\-}))]
                    (+ (->> syms (remove #{\|}) (partition 2) (map #(into #{} %)) (filter #{#{\F \J} #{\7 \L}}) count)
                       (->> syms (filter #{\|}) count)))
                  (let [syms (->> s-pts (map grid) (remove #{\|}))]
                    (+ (->> syms (remove #{\-}) (partition 2) (map #(into #{} %)) (filter #{#{\F \J} #{\7 \L}}) count)
                       (->> syms (filter #{\-}) count)))
                  (let [syms (->> e-pts (map grid) (remove #{\-}))]
                    (+ (->> syms (remove #{\|}) (partition 2) (map #(into #{} %)) (filter #{#{\F \J} #{\7 \L}}) count)
                       (->> syms (filter #{\|}) count)))])
               (filter #(every? odd? %))
               count))))


(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
