(ns abagile.aoc.2020.day18
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.walk :as w]))

(def input (->> (util/read-input-split-lines "2020/day18.txt")))

(def sample ["1 + 2 * 3 + 4 * 5 + 6"
             "1 + (2 * 3) + (4 * (5 + 6))"])

(def precedence (atom '{* 0, / 0 + 1, - 1}))

(defn order-ops
  "((A x B) y C) or (A x (B y C)) depending on precedence of x and y"
  [[A x B y C & more]]
  (let [ret (if (<= (@precedence x) (@precedence y))
              (list (list A x B) y C)
              (list A x (list B y C)))]
    (if-not more ret (recur (concat ret more)))))

(defn add-parens
  "Tree walk to add parens.  All lists are length 3 afterwards."
  [s]
  (w/postwalk
    #(if-not (seq? %)
       %
       (let [c (count %)]
         (cond (even? c) (throw (Exception. "Must be an odd number of forms"))
               (= c 1) (first %)
               (= c 3) %
               (>= c 5) (order-ops %))))
    s))

(defn make-ast
  "Parse a string into a list of numbers, ops, and lists"
  [s]
  (-> (format "'(%s)" s)
      (.replaceAll "([*+-/])" " $1 ")
      load-string
      add-parens))

(def ops {'* *
          '+ +
          '- -
          '/ /})

(def eval-ast
  (partial w/postwalk #(if-not (seq? %)
                         %
                         (let [[a o b] %]
                           ((ops o) a b)))))

(def evaluate #(eval-ast (make-ast %)))

(comment
  (evaluate (first sample))
  (evaluate (second sample)))

(defn part1 []
  (reset! precedence '{* 0, / 0 + 0, - 0})
  (time (->> (map evaluate input)
             (reduce +))))

(defn part2 []
  (reset! precedence '{* 1, / 1 + 0, - 0})
  (time (->> (map evaluate input)
             (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
