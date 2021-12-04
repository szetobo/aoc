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

(def input (util/read-input-split-lines "2021/day03.txt"))

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
       util/spy
       util/transpose
       util/spy
       (map #(map str %))
       util/spy
       (map gamma-epsilon-rate)
       util/spy
       util/transpose
       util/spy
       (map util/binary-val)
       util/spy))

(defn part1
  []
  (time (let [[gamma epsilon] (->> input
                                   util/transpose
                                   (map #(map str %))
                                   (map gamma-epsilon-rate)
                                   util/transpose
                                   (map util/binary-val))]
          (* gamma epsilon))))

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

;; ==========================================================================================================
;; copy the implmenetation from https://github.com/callum-oakley/advent-of-code/blob/main/src/aoc/2021/03.clj
;; just try to understood & learn the way of thinking from others
;; ==========================================================================================================

(defn parse [s] (map #(Integer/parseInt % 2) s))

(defn most-common-bit [xs i]
  (let [freq (frequencies (map #(bit-test % i) xs))]
    (max-key #(get freq % 0) false true)))

(defn part1'
  [no-of-bits xs]
  (->> (reduce (fn [[gamma epsilon] i]
                 (if (most-common-bit xs i)
                   [(bit-set gamma i) epsilon]
                   [gamma             (bit-set epsilon i)]))
         [0 0]
         (range no-of-bits))
       (apply *)))

(defn rating
  [no-of-bits fx xs]
  (loop [i (dec no-of-bits) [head & tail :as xs] xs]
    (if-not (seq tail)
      head
      (let [target (fx xs i)]
        (recur (dec i) (filter #(= (bit-test % i) target) xs))))))

(defn part2'
  [no-of-bits xs]
  (* (rating no-of-bits most-common-bit xs)
    (rating no-of-bits (complement most-common-bit) xs)))

(comment
  (most-common-bit (parse input) 0)
  (part1' 12 (parse input))
  (* (rating 12 most-common-bit (parse input)) (rating 12 (complement most-common-bit) (parse input))))
