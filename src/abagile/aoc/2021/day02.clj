(ns abagile.aoc.2021.day02
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split-lines "2021/day02.txt")
                (map #(re-matches #"(forward|down|up) (\d+)" %))
                (map (fn [[_ k v]] [(keyword k) (util/parse-int v)]))))

(comment
  (count input)
  (take 10 input)
  (->> (filter #(= (first %) :forward) input) (map second) (apply +))
  (->> (filter #(= (first %) :up) input) (map second) (apply +))
  (->> (filter #(= (first %) :down) input) (map second) (apply +)))

(defn move
  [{:keys [x y] :or {x 0 y 0}} [dir unit]]
  {:x (cond-> x
        (= dir :forward) (+ unit))
   :y (cond-> y
        (= dir :down) (+ unit)
        (= dir :up)   (- unit))})

(comment
  (move {} [:forward 1])
  (move {} [:down 1])
  (reduce move {} [[:forward 5]
                   [:down 5]
                   [:forward 8]
                   [:up 3]
                   [:down 8]
                   [:forward 2]]))

(defn part1
  []
  (time (let [{:keys [x y]} (reduce move {} input)]
          (* x y))))

(defn move2
  [{:keys [x y aim] :or {x 0 y 0 aim 0}} [dir unit]]
  {:x   (cond-> x
          (= dir :forward) (+ unit))
   :y   (cond-> y
          (= dir :forward) (+ (* aim unit)))
   :aim (cond-> aim
          (= dir :down) (+ unit)
          (= dir :up)   (- unit))})
(comment
  (move2 {} [:forward 1])
  (move2 {} [:down 1])
  (reduce move2 {} [[:forward 5]
                    [:down 5]
                    [:forward 8]
                    [:up 3]
                    [:down 8]
                    [:forward 2]]))

(defn part2
  []
  (time (let [{:keys [x y]} (reduce move2 {} input)]
          (* x y))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
