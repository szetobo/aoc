(ns abagile.aoc.2022.day12
  (:require
    [clojure.data.priority-map :refer [priority-map]]))

(def input (->> "resources/2022/day12.txt" slurp))

(defn parse
  [s]
  (let [cols (->> s (re-find #"[a-zSE]+\n") count dec)
        elms (->> s (re-seq #"[a-zSE]") (map (comp int first)))
        grid (into {} (map-indexed #(vector [(quot %1 cols) (rem %1 cols)] %2) elms))
        S    (->> grid (filter #(= (val %) (int \S))) first key)
        E    (->> grid (filter #(= (val %) (int \E))) first key)
        grid (-> grid (assoc S (int \a)) (assoc E (int \z)))]
     (with-meta grid {:dim [(quot (count elms) cols) cols]
                      :start S :end E})))

(defn dijkstra
  [start nbr-dsts]
  (loop [que (priority-map start 0) visited {}]
    (if-let [[pos dst] (peek que)]
      (let [dsts (->> (nbr-dsts pos)
                      (reduce-kv #(cond-> %1 (nil? (visited %2)) (assoc %2 %3)) {})
                      (reduce-kv #(assoc %1 %2 (+ %3 dst)) {}))]
        (recur (merge-with min (pop que) dsts) (assoc visited pos dst)))
      visited)))

(def offsets {:north [-1 0] :east [0 1] :south [1 0] :west [0 -1]})

(defn bounded
  ([grid]      (apply bounded (-> grid meta :dim)))
  ([rows cols] (fn [[row col]] (and (> rows row -1) (> cols col -1)))))

(defn adjacent
  ([offsets [row col]]   (for [[dr dc] offsets] [(+ row dr) (+ col dc)]))
  ([offsets f [row col]] (filter f (adjacent offsets [row col]))))

(defn nbr-costs
  [grid pos]
  (->> (adjacent (vals offsets) (bounded grid) pos)
       (reduce #(cond-> %1
                  (<= (- (grid %2) (grid pos)) 1) (assoc %2 1))
               {})))

(defn part1
  []
  (let [grid (parse input)
        S (-> grid meta :start)
        E (-> grid meta :end)]
    (-> (dijkstra S (partial nbr-costs grid)) (get E))))

(defn part2
  []
  (let [grid (parse input)
        E (-> grid meta :end)]
    (->> (for [S (->> grid (filter #(= (val %) (int \a))) keys)]
           (-> (dijkstra S (partial nbr-costs grid)) (get E)))
         (remove nil?)
         (apply min))))

(defn -main [& _]
  (println "part 1:" (time (part1)))
  (println "part 2:" (time (part2))))

(comment
  (-main))
