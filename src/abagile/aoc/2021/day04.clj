(ns abagile.aoc.2021.day04
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.set :as cset]
    [clojure.string :as cs]))

(def sample-input (util/read-input "2021/day04.sample.txt"))

(def input (util/read-input "2021/day04.txt"))

(defn parse
  [s]
  (let [[head & tails] (cs/split s #"\n\n")]
    [(map read-string (cs/split head #","))
     (->> tails
          (remove cs/blank?)
          (map #(->> % (re-seq #"\d+") (map read-string))))]))

(parse sample-input)
(parse input)

;; physically board is just a sequence of 25 numbers, but conceptually it is a 5x5 matrix

(def mark-cell #(+ % 1000))

(def cell-marked? #(>= % 1000))

(defn mark-board
  [n board]
  (map #(cond-> % (= n %) mark-cell) board))

(defn board-win?
  [board]
  (let [rows (partition 5 board)
        cols (util/transpose rows)]
    (->> (concat rows cols)
         (filter #(every? cell-marked? %))
         seq
         boolean)))

(defn calc-score
  [n board]
  (* n (apply + (filter (complement cell-marked?) board)))) 

(defn winner-score
  [nos boards]
  (loop [[n & tails] nos boards boards]
    (let [boards (map #(mark-board n %) boards)
          wins   (map board-win? boards)
          score  (->> (map #(when %1 (calc-score n %2)) wins boards) (remove nil?))]
      (if (seq score)
        (first score)
        (recur tails boards)))))

(defn loser-score
  [nos boards]
  (loop [[n & tails] nos boards boards]
    (let [boards (map #(mark-board n %) boards)
          wins   (map board-win? boards)
          freq   (frequencies wins)]
      (if (zero? (get freq false 0))
        (->> (first boards)
             (calc-score n))
        (let [filtered (->> (map #(when-not %1 %2) wins boards) (remove nil?))]
          (recur tails filtered))))))


(comment
  (parse input)
  (mark-board 3 [22 12 3 4 8])
  (apply winner-score (parse sample-input))
  (apply loser-score (parse sample-input)))

(defn part1
  []
  (time (apply winner-score (parse input))))

(defn part2
  []
  (time (apply loser-score (parse input)))) 

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))


;; =====================================================================================
;; copy the implementation from https://github.com/motform/advent-of-clojure/blob/master/src/advent-of-clojure/2021/04.clj
;; just try to understood & learn the way of thinking from others
;; basically the solution using logic programming to check bingo & calculate board score
;; =====================================================================================

(defn transform-board
  [boards]
  (map
    #(let [rows  (partition 5 %)
           cols  (util/transpose rows)]
       (concat (map set rows) (map set cols)))
    boards))

(defn parse'
  [s]
  (let [[nos boards] (parse s)]
    [nos (transform-board boards)]))

(defn winner-board
  [nos board]
  (when (some #(cset/superset? (set nos) %) board) board))

(defn calc-score'
  [nos board n]
  (* n (apply + (cset/difference (apply cset/union (take 5 board)) nos))))

(defn bingo
  [nos boards]
  (loop [nums #{} [n & nos] nos]
    (let [nums (conj nums n)]
      (if-let [winner (some #(winner-board nums %) boards)]
        (calc-score' nums winner n)
        (recur nums nos)))))

(defn last-bingo
  [nos boards]
  (loop [nums #{} boards boards [n & nos] nos]
    (let [nums     (conj nums n)
          filtered (remove #(winner-board nums %) boards)]
      (if (empty? filtered)
        (calc-score' nums (first boards) n)
        (recur nums filtered nos)))))

(comment
  (map #(winner-board [13 22 17 0 11] %) (last (parse' sample-input)))
  (parse' sample-input)
  (apply bingo (parse' sample-input))
  (apply last-bingo (parse' sample-input))
  (time (apply bingo (parse' input)))
  (time (apply last-bingo (parse' input))))
