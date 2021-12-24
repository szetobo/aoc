(ns abagile.aoc.2021.day15
  (:gen-class)
  (:require
    [abagile.aoc.grid :as grid]
    [abagile.aoc.util :as util]))

(def sample (util/read-input "2021/day15.sample.txt"))
(def input  (util/read-input "2021/day15.txt"))

(defn nbr-risks
  [cave pos]
  (->> (grid/adjacent-4 (grid/bounded cave) pos)
       (reduce #(assoc %1 %2 (cave %2)) {})))

(defn expand-cave
  [cave n]
  (let [[rs cs] (-> cave meta :dim)
        wrap #(inc (mod (dec %) 9))]
    (-> (into {} (for [r (range (* n rs)) c (range (* n cs))]
                   [[r c] (wrap (+ (cave [(mod r rs) (mod c cs)]) (quot r rs) (quot c cs)))]))
        (with-meta {:dim [(* n rs) (* n cs)]}))))

(comment
  (count sample)
  (-> (grid/parse sample) (nbr-risks [1 1]))
  (time (-> (grid/dijkstra [0 0] (partial nbr-risks (grid/parse sample))) (get [9 9])))
  (count input)
  (meta (grid/parse input)))

(defn part1
  []
  (time (let [cave (grid/parse input) dim (-> cave meta :dim)]
          (-> (grid/dijkstra [0 0] (partial nbr-risks cave)) (get (map dec dim))))))

(defn part2
  []
  (time (let [cave (-> (grid/parse input) (expand-cave 5)) dim (-> cave meta :dim)]
          (-> (grid/dijkstra [0 0] (partial nbr-risks cave)) (get (map dec dim))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
