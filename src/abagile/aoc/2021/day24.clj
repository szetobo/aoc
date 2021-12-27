(ns abagile.aoc.2021.day24
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def sample1 (util/read-input "2021/day24.sample1.txt"))
(def sample2 (util/read-input "2021/day24.sample2.txt"))
(def sample3 (util/read-input "2021/day24.sample3.txt"))
(def input   (util/read-input "2021/day24.txt"))

(defn parse
  [s]
  (->> s (re-seq #"(inp|add|mul|div|mod|eql) ([w-z])(?:\s([w-z]|-?\d+))?")
       (map rest)
       (map (fn [[op var p]] [op var (when p (cond-> p (re-find #"-?\d+" p) read-string))]))))
  
(defn eval-expr
  [{:keys [nos halt] :as state} [op var param]]
  (if halt
    state
    (let [param (when param (if (string? param) (get state param 0) param))]
      (case op
        "inp" (assoc state var (first nos) :nos (drop 1 nos))
        "add" (update state var + param)
        "mul" (update state var * param)
        "div" (if (zero? param)
                (assoc state :halt true)
                (update state var quot param))
        "mod" (if (or (<= param 0) (< (get state var 0) 0))
                (assoc state :halt true)
                (update state var mod param))
        "eql" (update state var (comp {true 1 false 0} =) param)))))

(defn monad
  ([nos exprs]
   (monad nos 0 exprs))
  ([nos z exprs]
   (-> (reduce eval-expr {:nos nos "w" 0 "x" 0 "y" 0 "z" z} exprs)
       (get "z"))))

(def solve
  (memoize
    (fn [guesses z [cur & steps]]
      (cond
        (and (nil? cur) (= z 0)) '(())
        (nil? cur)               '()
        :else (for [w   guesses
                    rst (solve guesses (monad [w] z cur) steps)]
                (conj rst w))))))

(defn extract-vars
  [exprs n]
  (->> (partition 18 exprs) util/transpose (drop (dec n)) first (map last)))

(defn step
  [w z a b c]
  (let [z' (quot z a)]
    (if (= (+ (mod z 26) b) w) z' (+ (* 26 z') w c))))

(def find-num
  (memoize
    (fn [guesses z [cur & steps]]
      (cond
        (and (nil? cur) (= z 0)) '(())
        (nil? cur)               '()
        :else (let [[a b c] cur]
                (for [w   guesses
                      rst (find-num guesses (step w z a b c) steps)]
                  (conj rst w)))))))

(comment
  (parse sample1)
  (parse sample2)
  (parse sample3)
  (count input)
  (->> (parse input) (monad (repeat 14 9) 1))
  (time (->> (parse input)
            (partition 18)
            ;; (drop 10)
            ;; (mapcat identity)
            ;; (monad [3 2] 0))
            (solve (util/range+ 1 9) 0)
            first
            (apply str) read-string))
  (->> (apply map vector (map (partial extract-vars (parse input)) [5 6 16]))
       ;; (drop 6)
       (find-num (util/range+ 1 9) 0)
       first
       (apply str) read-string))

(defn part1
  []
  (time
    ;; (let [exprs (parse input)
    ;;       vars  (apply map vector (map (partial extract-vars exprs) [5 6 16]))]
    ;;   (->> (find-num (util/range+ 9 1) 0 vars) first (apply str) read-string))
    (->> (parse input) (partition 18) (solve (util/range+ 9 1) 0) first (apply str) read-string)))

(defn part2
  []
  (time
    ;; (let [exprs (parse input)
    ;;       vars  (apply map vector (map (partial extract-vars exprs) [5 6 16]))]
    ;;   (->> (find-num (util/range+ 1 9) 0 vars)
    ;;        first (apply str) read-string))
    (->> (parse input) (partition 18) (solve (util/range+ 1 9) 0) first (apply str) read-string)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= -1 (-> (monad [1] (parse sample1)) (get "x"))))
  (is (= 1  (-> (monad [1 3] (parse sample2)) (get "z"))))
  (is (= [0 1 1 1] (-> (monad [7] (parse sample3)) (select-keys ["w" "x" "y" "z"]) vals)))
  (is (= [ 1  1  1  1 26  1  1  26  26  26 26  1 26  26] (-> (parse input) (extract-vars 5))))
  (is (= [10 10 12 11  0 15 13 -12 -15 -15 -4 10 -5 -12] (-> (parse input) (extract-vars 6))))
  (is (= [12 10  8  4  3 10  6  13   8   1  7  6  9   9] (-> (parse input) (extract-vars 16)))))
