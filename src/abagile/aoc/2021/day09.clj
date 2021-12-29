(ns abagile.aoc.2021.day09
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [abagile.aoc.grid :as grid]))

(def sample (util/read-input "2021/day09.sample.txt"))
(def input  (util/read-input "2021/day09.txt"))

(defn low-points
  [caves]
  (let [[rows cols] (-> caves meta :dim)
        nbrs-fn     #(grid/adjacent-4 (grid/bounded rows cols) %)]
    (for [row (range rows) col (range cols)
          :let [v    (caves [row col])
                nbrs (nbrs-fn [row col])]
          :when (= (count nbrs) (count (filter #(> (caves %) v) nbrs)))]
      [[row col] v])))

(defn basins
  [caves [x y]]
  (let [nbrs-fn #(grid/adjacent-4 (grid/bounded caves) %)]
    (loop [res #{} pts #{[x y]}]
      (let [pt   (first pts)
            nbrs (->> (nbrs-fn pt) (remove #(or (contains? res %) (>= (caves %) 9))))
            pts  (apply conj (disj pts pt) nbrs)
            res  (conj res pt)]
        (if (empty? pts) res (recur res pts))))))

(comment
  (->> (grid/parse sample) (into (sorted-map))))

(defn part1
  []
  (time
    (->> input grid/parse low-points (map (comp inc last)) (reduce +))))

(defn part2
  []
  (time
    (let [caves   (grid/parse input)
          low-pts (map first (low-points caves))]
      ;; (->> (map (fn [pt]
      ;;             (grid/dijkstra pt #(->> (grid/adjacent-4 (grid/bounded caves) %)
      ;;                                     (filter (fn [pt] (< (caves pt) 9)))
      ;;                                     (reduce (fn [res pt] (assoc res pt 1)) {}))))
      ;;        low-pts)
      ;;      (map count)
      ;;      (sort >) (take 3) (reduce *))
      (->> (map #(count (basins caves %)) low-pts)
           (sort >) (take 3) (reduce *)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
