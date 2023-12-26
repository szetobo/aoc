(ns abagile.aoc.2023.day23
  (:require
   [abagile.aoc.util :as util]
   [clojure.string :as cs]
   [clojure.test :refer [deftest is]]))

(def sample (->> (util/read-input-split-lines "2023/day23-sample.txt")))
(def input (->> (util/read-input-split-lines "2023/day23.txt")))

(defn parse
  [s]
  (let [grid (->> s (mapv vec))
        rows (count grid)
        cols (count (first grid))]
    (with-meta grid {:rows rows :cols cols})))

(defn nbrs
  [grid [row col]]
  (let [{:keys [rows cols]} (meta grid)]
    (->> (case (get-in grid [row col])
           \^ {[(dec row) col] 1}
           \v {[(inc row) col] 1}
           \< {[row (dec col)] 1}
           \> {[row (inc col)] 1}
           \. {[(dec row) col] 1 [(inc row) col] 1 [row (dec col)] 1 [row (inc col)] 1})
         (remove (fn [[[r c] _]] (or (neg? r) (>= r rows) (neg? c) (>= c cols) (= (get-in grid [r c]) \#))))
         (into (sorted-map)))))
  
(defn solve
  [s]
  (let [grid   (parse s)
        {:keys [rows cols]} (meta grid)
        S      [0 (first (keep-indexed #(when (= %2 \.) %1) (first grid)))]
        E      [(dec rows) (last (keep-indexed #(when (= %2 \.) %1) (last grid)))]
        pts    (->> (for [r (range rows) c (range cols)
                          :let [pt [r c]]
                          :when (and (not= (get-in grid pt) \#) (> (count (nbrs grid pt)) 2))]
                      pt)
                    (into #{S E}))
        graph  (->> (reduce (fn [g [sr sc :as pt]]
                              (loop [g g stack [[sr sc 0]] seen #{pt}]
                                (if-let [[r c n] (peek stack)]
                                  (if (and (pos? n) (get pts [r c]))
                                    (recur (assoc-in g [[sr sc] [r c]] n) (pop stack) seen)
                                    (let [nbrs (->> (nbrs grid [r c])
                                                    (util/remove-keys seen)
                                                    (util/fmap (partial + n)))]
                                      (recur g
                                             (reduce-kv #(conj %1 (conj %2 %3)) (pop stack) nbrs)
                                             (into seen (keys nbrs)))))
                                  g)))
                            (sorted-map) (disj pts E)))]
    (->> [S #{}]
         ((fn dfs [[pt seen]]
            (if (= pt E) [0]
              (for [[np n] (->> (graph pt))
                    :when (nil? (seen np))
                    x (dfs [np (conj seen pt)])]
                 (+ n x)))))
         (apply max))))

(deftest test1
  (is (= 1 1)))

(defn part1
  []
  (time (solve input)))


(deftest test2
  (is (= 1 1)))


(defn part2
  []
  (time (solve (mapv #(cs/escape % {\^ \. \v \. \> \. \< \.}) input))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
