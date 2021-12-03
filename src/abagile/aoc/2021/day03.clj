(ns abagile.aoc.2021.day03
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def sample-input
  ["00100"
   "11110"
   "10110"
   "10111"
   "10101"
   "01111"
   "00111"
   "11100"
   "10000"
   "11001"
   "00010"
   "01010"])

(def input (->> (util/read-input-split-lines "2021/day03.txt")))

(defn gamma-rate
  [coll]
  (let [{ones "1" zeros "0" :or {ones 0 zeros 0}} (frequencies coll)]
    (if (>= ones zeros) "1" "0")))

(defn epsilon-rate
  [coll]
  (let [{ones "1" zeros "0" :or {ones 0 zeros 0}} (frequencies coll)]
    (if (>= ones zeros) "0" "1")))

(defn gamma-epsilon-rate
  [coll]
  (if (= (gamma-rate coll) "1") ["1" "0"] ["0" "1"]))

(comment
  (count input)
  (take 10 input)
  (gamma-rate   ["0" "1" "1" "1" "1" "0" "0" "1" "1" "1" "0" "0"])
  (epsilon-rate ["0" "1" "1" "1" "1" "0" "0" "1" "1" "1" "0" "0"])
  (gamma-rate   ["0" "1" "0" "0" "0" "1" "0" "1" "0" "1" "0" "1"])
  (epsilon-rate ["0" "1" "0" "0" "0" "1" "0" "1" "0" "1" "0" "1"])
  (util/binary-val ["1" "1" "0" "1"])
  (util/binary-val "1101")
  (->> sample-input
       (apply map list)
       (map #(map str %))
       (map gamma-epsilon-rate)
       (apply map list)
       (map util/binary-val)))

(defn part1
  []
  (time (->> input
             (apply map list)
             (map #(map str %))
             (map gamma-epsilon-rate)
             (apply map list)
             (map util/binary-val)
             (apply *))))

(defn calc-rating
  [fx idx [fst & rst :as items]]
  ;; (prn idx items)
  (if (nil? rst)
    fst
    (let [coll     (map #(str (nth % idx)) items)
          rating   (fx coll)
          filtered (filter #(= (str (nth % idx)) rating) items)]
        ;; (prn rating (count filtered) coll)
        (calc-rating fx (inc idx) filtered))))

(def o2-rating  (partial calc-rating gamma-rate 0))
(def co2-rating (partial calc-rating epsilon-rate 0))

(comment
  (o2-rating sample-input)
  (co2-rating sample-input)
  ((juxt o2-rating co2-rating) input))

(defn part2
  []
  (time (->> input
             ((juxt o2-rating co2-rating))
             (map util/binary-val)
             (apply *))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
