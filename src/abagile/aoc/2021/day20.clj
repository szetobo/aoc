(ns abagile.aoc.2021.day20
  (:gen-class)
  (:require
    [abagile.aoc.grid :as grid]
    [abagile.aoc.ocr :as ocr]
    [abagile.aoc.util :as util]
    [clojure.string :as cs]))

(def sample (util/read-input "2021/day20.sample.txt"))
(def input  (util/read-input "2021/day20.txt"))

(defn- adjacent
  [[x y]]
  (for [[dx dy] (sort (conj (vals grid/offsets) [0 0]))
        :let [x' (+ x dx) y' (+ y dy)]]
    [x' y']))

(defn parse
  [s]
  (let [[alg image] (cs/split s #"\n\n")]
    [(->> alg (map {\. 0 \# 1}) vec) (->> (ocr/parse image))]))

(defn enhance
  [bg alg grid]
  (let [rows (->> grid keys (map first))
        cols (->> grid keys (map second))]
    (->> (for [row (range (- (apply min rows) 2) (+ (apply max rows) 2))
               col (range (- (apply min cols) 2) (+ (apply max cols) 2))
               :let [nbrs (adjacent [row col])]]
           [[row col] (->> nbrs (map #(str (grid % bg))) (apply str) util/binary-val)])
         (into {})
         (util/fmap alg))))

(comment
  (count sample)
  (->> sample parse)
       ;; first)
       ;; second (into (sorted-map)))
  (let [[alg image] (parse sample)]
    (->> (reduce #(enhance (if (or (zero? (alg 0)) (even? %2)) 0 1) alg %1) image (range 2))
         (reduce-kv #(cond-> %1 (pos? %3) (conj %2)) #{})
         ocr/draw
         util/transpose (map #(apply str %))))
  (count input)
  (->> input parse))

(defn part1
  []
  (time
    (let [[alg image] (parse input)]
      (->> (reduce #(enhance (if (or (zero? (alg 0)) (even? %2)) 0 1) alg %1) image (range 2))
           (remove #(-> % val zero?)) count))))

(defn part2
  []
  (time
    (let [[alg image] (parse input)]
      (->> (reduce #(enhance (if (or (zero? (alg 0)) (even? %2)) 0 1) alg %1) image (range 50))
           (remove #(-> % val zero?)) count))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
