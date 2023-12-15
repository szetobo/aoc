(ns abagile.aoc.2023.day14
  (:require
   [abagile.aoc.util :as util]
   [clojure.string :as cs]))

(def sample (->> (util/read-input-split "2023/day14-sample.txt" #"\n")))
(def input (->> (util/read-input-split "2023/day14.txt" #"\n")))


(defn part1
  []
  (time (let [grid (->> input util/transpose (map #(apply str %)))
              col  (->> grid first count)
              row  (->> grid count)]
          (->> grid
               (map (fn [row] (->> (cs/split row #"#")
                                   (map #(apply str (sort-by {\O 0 \. 1} %)))
                                   (cs/join "#"))))
               (map (fn [row] (str row (apply str (repeat (- col (count row)) \#)))))
               (util/transpose)
               (map-indexed #(* (- row %1) (count (filter #{\O} %2))))
               (reduce +)))))


(defn roll-west
  [grid]
  (let [col (->> grid first count)]
    (->> grid
         (map (fn [row] (->> (cs/split row #"#")
                             (map #(apply str (sort-by {\O 0 \. 1} %)))
                             (cs/join "#"))))
         (map (fn [row] (str row (apply str (repeat (- col (count row)) \#))))))))

(defn roll-north
  [grid]
  (let [grid (->> grid util/transpose (map #(apply str %)))]
    (->> grid roll-west util/transpose (map #(apply str %)))))

(defn roll-east
  [grid]
  (let [grid (->> grid (map reverse) (map #(apply str %)))]
    (->> grid roll-west (map reverse) (map #(apply str %)))))

(defn roll-south
  [grid]
  (let [grid (->> grid reverse util/transpose (map #(apply str %)))]
    (->> grid roll-west util/transpose reverse (map #(apply str %)))))

(defn roll-cycle
  [grid]
  (->> grid roll-north roll-west roll-south roll-east))

(defn ttl-loading
  [grid]
  (let [row (->> grid count)]
    (->> grid
         (map-indexed #(* (- row %1) (count (filter #{\O} %2))))
         (reduce +))))

(comment
  (->> sample roll-west)
  (->> sample roll-north)
  (->> sample roll-east)
  (->> sample roll-south)
  (->> (iterate roll-cycle sample) (drop 1) (take 3)))

(defn part2'
  []
  (time (->> (iterate (memoize roll-cycle) input) (take 200)
             (map ttl-loading))))

;; it is discover that after first 93 entries, the ttl-loading is repeating itself in a sequence of
;; [96297 96314 96325 96333 96344 96345 96340 96317 96293]

(defn part2
  []
  (time (get [96297 96314 96325 96333 96344 96345 96340 96317 96293] (mod (- 1000000000 93) 9))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
